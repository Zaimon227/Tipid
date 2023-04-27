package com.example.tipid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class activity_fixeddues extends AppCompatActivity {
    TextView tvUsername;
    Button btnBack;
    ImageButton btnAddFixedDue;
    ListView lvFixedDuesRecord;
    dataclass_fixeddues dataclassFixedDues;
    adapter_fixeddues adapterFixedDues;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    String url_addFixedDue = "" + address + "/addFixedDue.php";
    String url_fetchFixedDuesRecord = "" + address + "/fetchFixedDuesRecord.php";

    public static String user_id ="";
    public static String username ="";

    public static ArrayList<dataclass_fixeddues> dataclassFixedDuesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixeddues);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnAddFixedDue = (ImageButton) findViewById(R.id.btnAddFixedDue);

        lvFixedDuesRecord = (ListView) findViewById(R.id.lvFixedDuesRecord);
        adapterFixedDues = new adapter_fixeddues(this, dataclassFixedDuesArrayList);
        lvFixedDuesRecord.setAdapter(adapterFixedDues);

        Intent intent = getIntent();
        user_id = intent.getExtras().getString("id");
        username = intent.getExtras().getString("username");

        tvUsername.setText(username);

        getDuesRecord();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_fixeddues.this, activity_dues.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnAddFixedDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder addFixedDue_builder = new AlertDialog.Builder(activity_fixeddues.this);
                View addFixedDueView = getLayoutInflater().inflate(R.layout.add_fixeddue_dialog, null);

                addFixedDue_builder.setTitle("Add Fixed Due");

                EditText edtAddFixedDueName = (EditText) addFixedDueView.findViewById(R.id.edtAddFixedDueName);
                EditText edtAddFixedDueCost = (EditText) addFixedDueView.findViewById(R.id.edtAddFixedDueCost);

                addFixedDue_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                addFixedDue_builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = edtAddFixedDueName.getText().toString();
                        String cost = edtAddFixedDueCost.getText().toString();

                        if(!name.equals("") && !cost.equals("")){
                            StringRequest request = new StringRequest(Request.Method.POST, url_addFixedDue, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(activity_fixeddues.this, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity_fixeddues.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();

                                    param.put("user_id", user_id);
                                    param.put("name", name);
                                    param.put("cost", cost);

                                    return param;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(activity_fixeddues.this);
                            requestQueue.add(request);

                            dialogInterface.dismiss();
                            finish();
                            startActivity(getIntent());
                        }else {
                            Toast.makeText(getApplicationContext(), "Please answer all required fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                addFixedDue_builder.setView(addFixedDueView);
                AlertDialog addFixedDueDialog = addFixedDue_builder.create();
                addFixedDueDialog.show();
            }
        });
    }

    private void getDuesRecord() {
        StringRequest request = new StringRequest(Request.Method.POST, url_fetchFixedDuesRecord, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataclassFixedDuesArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String name = object.getString("name");
                            String cost = object.getString("cost");
                            String fixeddue_id = object.getString("fixeddue_id");

                            dataclassFixedDues = new dataclass_fixeddues(name, cost, user_id, fixeddue_id);
                            dataclassFixedDuesArrayList.add(dataclassFixedDues);
                            adapterFixedDues.notifyDataSetChanged();
                        }
                    }

                } catch (Exception e) {

                }

                //Toast.makeText(ShowData.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_fixeddues.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                param.put("user_id", user_id);

                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity_fixeddues.this);
        requestQueue.add(request);
    }
}