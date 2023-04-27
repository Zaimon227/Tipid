package com.example.tipid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class activity_login extends AppCompatActivity {

    EditText edtLoginUsername, edtLoginPassword;
    ImageButton btnCloseLogin, btnSubmitLogin;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    String url_getID = "" + address + "/fetchID.php";
    String url_login = "" + address + "/login.php";

    public static String id ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLoginUsername = (EditText) findViewById(R.id.edtLoginUsername);
        edtLoginPassword = (EditText) findViewById(R.id.edtLoginPassword);

        btnSubmitLogin = (ImageButton) findViewById(R.id.btnSubmitLogin);
        btnCloseLogin = (ImageButton) findViewById(R.id.btnCloseLogin);

        btnCloseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSubmitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username, password;
                username = String.valueOf(edtLoginUsername.getText());
                password = String.valueOf(edtLoginPassword.getText());

                if (!username.equals("") && !password.equals("")) {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;
                            //localhost
                            //PutData putData = new PutData("http://localhost/Tipid/login.php", "POST", field, data);

                            PutData putData = new PutData(url_login, "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("Login Success")) {
                                        getID();

                                    } else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please answer all required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getID() {
        String username = edtLoginUsername.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, url_getID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            id = object.getString("id");

                            Intent intent = new Intent(activity_login.this, activity_earnings.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                }
                //Toast.makeText(activity_login.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_login.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();
                param.put("username", username);

                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity_login.this);
        requestQueue.add(request);
    }

}