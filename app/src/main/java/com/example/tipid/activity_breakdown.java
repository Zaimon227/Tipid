package com.example.tipid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class activity_breakdown extends AppCompatActivity {
    TextView tvTotalFood, tvTotalHobbies, tvTotalSchool, tvTotalTransportation, tvTotalMiscellaneous, tvUsername;
    Button btnBack;

    public static String user_id = "";
    public static String username = "";
    public static String date = "";
    public static String category = "";

    public static Double num_cost;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    String url_fetchExpensesBreakdown = "" + address + "/fetchExpensesBreakdown.php";

    DecimalFormat numbersformat = new DecimalFormat("###,###,###,###,###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakdown);

        tvTotalFood = (TextView) findViewById(R.id.tvTotalFood);
        tvTotalHobbies = (TextView) findViewById(R.id.tvTotalHobbies);
        tvTotalSchool = (TextView) findViewById(R.id.tvTotalSchool);
        tvTotalTransportation = (TextView) findViewById(R.id.tvTotalTransportation);
        tvTotalMiscellaneous = (TextView) findViewById(R.id.tvTotalMiscellaneous);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        btnBack = (Button) findViewById(R.id.btnBack);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(new Date());

        Intent intent = getIntent();
        user_id = intent.getExtras().getString("id");
        username = intent.getExtras().getString("username");

        tvUsername.setText(username);

        getFoodExpenses();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_breakdown.this, activity_expenses.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    private void getFoodExpenses() {
        category = "Food/Drinks";

        StringRequest request = new StringRequest(Request.Method.POST, url_fetchExpensesBreakdown, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        num_cost = 0.0;
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String cost = object.getString("cost");
                            num_cost = Double.parseDouble(cost);

                            tvTotalFood.setText("" + numbersformat.format(num_cost));
                        }
                        getHobbiesExpenses();
                    }

                } catch (Exception e) {

                }
                //Toast.makeText(ShowData.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_breakdown.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                param.put("user_id", user_id);
                param.put("category", category);
                param.put("date", date);

                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity_breakdown.this);
        requestQueue.add(request);
    }

    private void getHobbiesExpenses() {
        category = "Hobbies/Sports";

        StringRequest request = new StringRequest(Request.Method.POST, url_fetchExpensesBreakdown, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        num_cost = 0.0;
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String cost = object.getString("cost");
                            num_cost = Double.parseDouble(cost);

                            tvTotalHobbies.setText("" + numbersformat.format(num_cost));
                        }
                        getSchoolExpenses();
                    }

                } catch (Exception e) {

                }
                //Toast.makeText(ShowData.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_breakdown.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                param.put("user_id", user_id);
                param.put("category", category);
                param.put("date", date);

                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity_breakdown.this);
        requestQueue.add(request);
    }

    private void getSchoolExpenses() {
        category = "Education/Work";

        StringRequest request = new StringRequest(Request.Method.POST, url_fetchExpensesBreakdown, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        num_cost = 0.0;
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String cost = object.getString("cost");
                            num_cost = Double.parseDouble(cost);

                            tvTotalSchool.setText("" + numbersformat.format(num_cost));
                        }
                        getTransportationExpenses();
                    }

                } catch (Exception e) {

                }
                //Toast.makeText(ShowData.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_breakdown.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                param.put("user_id", user_id);
                param.put("category", category);
                param.put("date", date);

                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity_breakdown.this);
        requestQueue.add(request);
    }

    private void getTransportationExpenses() {
        category = "Transportation";

        StringRequest request = new StringRequest(Request.Method.POST, url_fetchExpensesBreakdown, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        num_cost = 0.0;
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String cost = object.getString("cost");
                            num_cost = Double.parseDouble(cost);

                            tvTotalTransportation.setText("" + numbersformat.format(num_cost));
                        }
                        getMiscellaneousExpenses();
                    }

                } catch (Exception e) {

                }
                //Toast.makeText(ShowData.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_breakdown.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                param.put("user_id", user_id);
                param.put("category", category);
                param.put("date", date);

                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity_breakdown.this);
        requestQueue.add(request);
    }

    private void getMiscellaneousExpenses() {
        category = "Miscellaneous";

        StringRequest request = new StringRequest(Request.Method.POST, url_fetchExpensesBreakdown, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        num_cost = 0.0;
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String cost = object.getString("cost");
                            num_cost = Double.parseDouble(cost);

                            tvTotalMiscellaneous.setText("" + numbersformat.format(num_cost));
                        }
                    }

                } catch (Exception e) {

                }
                //Toast.makeText(ShowData.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_breakdown.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                param.put("user_id", user_id);
                param.put("category", category);
                param.put("date", date);

                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity_breakdown.this);
        requestQueue.add(request);
    }
}