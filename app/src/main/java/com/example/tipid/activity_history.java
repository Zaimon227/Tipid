package com.example.tipid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class activity_history extends AppCompatActivity {

    Button btnBack;
    TextView tvUsername;
    ListView lvHistory;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    public static String user_id ="";
    public static String username ="";

    String url_fetchExpensesHistory = "" + address + "/fetchExpensesHistory.php";

    dataclass_history dataclassHistory;
    adapter_history adapterHistory;

    public static ArrayList<dataclass_history> dataclassHistoryArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btnBack = (Button) findViewById(R.id.btnBack);
        tvUsername = (TextView) findViewById(R.id.tvUsername);

        Intent intent = getIntent();
        user_id = intent.getExtras().getString("id");
        username = intent.getExtras().getString("username");

        lvHistory = (ListView) findViewById(R.id.lvHistory);
        adapterHistory = new adapter_history (this, dataclassHistoryArrayList);
        lvHistory.setAdapter(adapterHistory);

        tvUsername.setText(username);

        getHistoryRecord();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_history.this, activity_expenses.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    private void getHistoryRecord() {
        StringRequest request = new StringRequest(Request.Method.POST, url_fetchExpensesHistory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataclassHistoryArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String name = object.getString("name");
                            String cost = object.getString("cost");
                            String hdate = object.getString("date");

                            dataclassHistory = new dataclass_history(name, cost, hdate);
                            dataclassHistoryArrayList.add(dataclassHistory);
                            adapterHistory.notifyDataSetChanged();
                        }
                    }

                } catch (Exception e) {

                }
                //Toast.makeText(ShowData.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_history.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(activity_history.this);
        requestQueue.add(request);
    }
}