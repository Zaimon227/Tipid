package com.example.tipid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.concurrent.TimeUnit;

public class activity_earnings extends AppCompatActivity {

    TextView tvDailyEarning, tvWeeklyEarning, tvMonthlyEarning, tvAnnualEarning, tvUsername;
    TextView tvAdjustedWeeklyEarning, tvAdjustedMonthlyEarning, tvAdjustedAnnualEarning;
    Button btnEarnings_ern, btnExpenses_ern, btnDues_ern, btnSavings_ern;
    ImageButton btnAccount_ern;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    public static String id = "";
    public static String username = "";
    public static String monthly_earnings = "";
    public static String date= "";
    public static String password = "";
    public static String email = "";

    public Double num_total_daily;
    public Double num_total_weekly;
    public Double num_total_monthly = 0.0;
    public Double num_total_monthlydues = 0.0;
    public Double num_total_monthlydebtpayment = 0.0;

    public Double num_daily_earnings;
    public Double num_weekly_earnings;
    public Double num_monthly_earnings;

    public Double num_adjusted_daily = 0.0;
    public Double num_adjusted_weekly = 0.0;
    public Double num_adjusted_monthly = 0.0;

    String url_fetchEarnings = "" + address + "/fetchEarningsData.php";
    String url_fetchTotalDaily = "" + address + "/fetchTotalDaily.php";
    String url_fetchTotalWeekly = "" + address + "/fetchTotalWeekly.php";

    String url_fetchMonthlyExpenses = "" + address + "/fetchMonthlyExpenses.php";
    String url_fetchMonthlyDues = "" + address + "/fetchMonthlyDues.php";
    String url_fetchMonthlyDebtPayment = "" + address + "/fetchMonthlyDebtPayment.php";

    String url_fetchAccountData = "" + address + "/fetchAccountData.php";

    DecimalFormat numbersformat = new DecimalFormat("###,###,###,###,###.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);

        tvDailyEarning = (TextView) findViewById(R.id.tvDailyEarning);
        tvWeeklyEarning = (TextView) findViewById(R.id.tvWeeklyEarning);
        tvMonthlyEarning = (TextView) findViewById(R.id.tvMonthlyEarning);
        tvAnnualEarning = (TextView) findViewById(R.id.tvAnnualEarning);

        tvUsername = (TextView) findViewById(R.id.tvUsername);

        tvAdjustedWeeklyEarning = (TextView) findViewById(R.id.tvAdjustedWeeklyEarning);
        tvAdjustedMonthlyEarning = (TextView) findViewById(R.id.tvAdjustedMonthlyEarning);
        tvAdjustedAnnualEarning = (TextView) findViewById(R.id.tvAdjustedAnnualEarning);

        btnEarnings_ern = (Button) findViewById(R.id.btnEarnings);
        btnExpenses_ern = (Button) findViewById(R.id.btnExpenses);
        btnDues_ern = (Button) findViewById(R.id.btnDues);
        btnSavings_ern = (Button) findViewById(R.id.btnSavings);
        btnAccount_ern = (ImageButton) findViewById(R.id.btnAccount);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        username = intent.getExtras().getString("username");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(new Date());

        getEarningsAsyncTask earningsAsyncTask = new getEarningsAsyncTask();
        earningsAsyncTask.execute();

        fetchAccountData();
        /*try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        btnExpenses_ern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_earnings.this, activity_expenses.class);
                intent.putExtra("id", id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnDues_ern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_earnings.this, activity_dues.class);
                intent.putExtra("id", id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnSavings_ern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_earnings.this, activity_savings.class);
                intent.putExtra("id", id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnAccount_ern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_earnings.this, activity_account.class);
                intent.putExtra("id", id);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("monthly_earnings", monthly_earnings);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

    }

    private class getEarningsAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchEarnings, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                username = object.getString("username");
                                monthly_earnings = object.getString("monthly_earnings");

                                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                            tvUsername.setText(username);
                            //tvMonthlyEarning.setText(monthly_earnings);
                            num_monthly_earnings = Double.parseDouble(monthly_earnings);
                            double num_annual_earnings = num_monthly_earnings *12;
                            num_weekly_earnings = num_annual_earnings /52.143;
                            num_daily_earnings = num_annual_earnings /365;

                            tvMonthlyEarning.setText("" + numbersformat.format(num_monthly_earnings));
                            tvAnnualEarning.setText("" + numbersformat.format(num_annual_earnings));
                            tvWeeklyEarning.setText("" + numbersformat.format(num_weekly_earnings));
                            tvDailyEarning.setText("" + numbersformat.format(num_daily_earnings));

                            getAdjustedDailyAsyncTask getadjusteddailyAsyncTask = new getAdjustedDailyAsyncTask();
                            getadjusteddailyAsyncTask.execute();
                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_earnings.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_earnings.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", id);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_earnings.this);
            requestQueue.add(request);

            return null;
        }
    }

    private class getAdjustedDailyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchTotalDaily, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            num_total_daily = 0.0;
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String cost = object.getString("cost");
                                double num_cost = Double.parseDouble(cost);
                                num_total_daily = num_total_daily + num_cost;

                                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                            num_adjusted_daily = num_daily_earnings - num_total_daily;

                            getAdjustedWeeklyAsyncTask getadjustedweeklyAsyncTask = new getAdjustedWeeklyAsyncTask();
                            getadjustedweeklyAsyncTask.execute();
                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_earnings.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_earnings.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", id);
                    param.put("date", date);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_earnings.this);
            requestQueue.add(request);

            return null;
        }
    }

    private class getAdjustedWeeklyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchTotalWeekly, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            num_total_weekly = 0.0;
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String cost = object.getString("cost");
                                double num_cost = Double.parseDouble(cost);
                                num_total_weekly = num_total_weekly + num_cost;

                                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                            num_adjusted_weekly = num_weekly_earnings - num_total_weekly;
                            tvAdjustedWeeklyEarning.setText("" + numbersformat.format(num_adjusted_weekly));

                            getMonthlyExpensesAsyncTask getmonthlyexpensesAsyncTask = new getMonthlyExpensesAsyncTask();
                            getmonthlyexpensesAsyncTask.execute();
                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_earnings.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_earnings.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", id);
                    param.put("date", date);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_earnings.this);
            requestQueue.add(request);

            return null;
        }
    }

    private class getMonthlyExpensesAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchMonthlyExpenses, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            num_total_monthly = 0.0;
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String cost = object.getString("cost");
                                double num_cost = Double.parseDouble(cost);
                                num_total_monthly = num_total_monthly + num_cost;

                            }

                            num_adjusted_monthly = num_monthly_earnings - num_total_monthly;
                            tvAdjustedMonthlyEarning.setText("" + numbersformat.format(num_adjusted_monthly));

                            getMonthlyDuesAsyncTask getmonthlyduesAsyncTask = new getMonthlyDuesAsyncTask();
                            getmonthlyduesAsyncTask.execute();
                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_earnings.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_earnings.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", id);
                    param.put("date", date);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_earnings.this);
            requestQueue.add(request);

            return null;
        }
    }

    private class getMonthlyDuesAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchMonthlyDues, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            num_total_monthlydues = 0.0;
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String cost = object.getString("cost");
                                double num_cost = Double.parseDouble(cost);
                                num_total_monthlydues = num_total_monthlydues + num_cost;

                            }
                            num_adjusted_monthly = num_adjusted_monthly - num_total_monthlydues;
                            tvAdjustedMonthlyEarning.setText("" + numbersformat.format(num_adjusted_monthly));

                            getMonthlyDebtPayAsyncTask getmonthlydebtpayAsyncTask = new getMonthlyDebtPayAsyncTask();
                            getmonthlydebtpayAsyncTask.execute();

                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_earnings.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_earnings.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", id);
                    param.put("date", date);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_earnings.this);
            requestQueue.add(request);

            return null;
        }
    }

    private class getMonthlyDebtPayAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchMonthlyDebtPayment, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            num_total_monthlydebtpayment = 0.0;
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String amount = object.getString("amount");
                                double num_amount = Double.parseDouble(amount);
                                num_total_monthlydebtpayment = num_total_monthlydebtpayment + num_amount;

                            }
                            num_adjusted_monthly = num_adjusted_monthly - num_total_monthlydebtpayment;
                            tvAdjustedMonthlyEarning.setText("" + numbersformat.format(num_adjusted_monthly));
                            double num_adjusted_annual = num_adjusted_monthly * 12;
                            tvAdjustedAnnualEarning.setText("" + numbersformat.format(num_adjusted_annual));

                            if (num_adjusted_monthly < num_monthly_earnings * 0.5) {
                                Toast.makeText(activity_earnings.this, "Be careful with your spending, your remaining earnings for the month is less than 50% of your monthly earnings", Toast.LENGTH_LONG).show();
                            }

                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_earnings.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_earnings.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", id);
                    param.put("date", date);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_earnings.this);
            requestQueue.add(request);

            return null;
        }
    }

    private void fetchAccountData() {
        StringRequest request = new StringRequest(Request.Method.POST, url_fetchAccountData, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            monthly_earnings = object.getString("monthly_earnings");
                            password = object.getString("password");
                            email = object.getString("email");
                        }
                    }

                } catch (Exception e) {

                }
                //Toast.makeText(activity_earnings.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_earnings.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                param.put("user_id", id);

                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity_earnings.this);
        requestQueue.add(request);
    }

}