package com.example.tipid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class activity_savings extends AppCompatActivity {

    TextView tvSavingsTotal, tvGoalName, tvRemainingAmount, tvUsername;
    Button btnEarnings, btnExpenses, btnDues, btnCreateGoal, btnAddBonus;
    ImageButton btnAccount;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    public static String user_id = "";
    public static String username = "";
    public static String date = "";
    public static String goal_name = "";
    public static String goal_amount = "";
    public static String savings_amount = "";

    public static String monthly_earnings = "";
    public static String password = "";
    public static String email = "";
    public static Double num_total_savings;
    public static Double num_daily_earnings;
    public static Double num_total_daily;
    public static Double num_total_dailydues;
    public static Double num_total_dailydebtpayment;
    public static Double num_savings_amount;
    public static Double num_goal_amount;
    public static Double num_remaining_amount;
    public static Double num_bonus;
    public static String add_total_savings = "";

    String url_createGoal = "" + address + "/createGoal.php";
    String url_fetchGoal = "" + address + "/fetchGoal.php";
    String url_fetchEarnings = "" + address + "/fetchEarningsData.php";
    String url_fetchTotalDaily = "" + address + "/fetchTotalDaily.php";
    String url_fetchDailyDues = "" + address + "/fetchDailyDues.php";
    String url_fetchDailyDebtPayment = "" + address + "/fetchDailyDebtPayment.php";
    String url_addSavings = "" + address + "/addSavings.php";
    String url_fetchSavings = "" + address + "/fetchSavings.php";
    String url_fetchAccountData = "" + address + "/fetchAccountData.php";
    String url_fetchDailyBonus = "" + address + "/fetchDailyBonus.php";
    String url_addBonus = "" + address + "/addBonus.php";

    DecimalFormat numbersformat = new DecimalFormat("###,###,###,###,###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings);

        tvSavingsTotal = (TextView) findViewById(R.id.tvSavingsTotal);
        tvGoalName = (TextView) findViewById(R.id.tvGoalName);
        tvRemainingAmount = (TextView) findViewById(R.id.tvRemainingAmount);
        tvUsername = (TextView) findViewById(R.id.tvUsername);

        btnEarnings = (Button) findViewById(R.id.btnEarnings);
        btnExpenses = (Button) findViewById(R.id.btnExpenses);
        btnDues = (Button) findViewById(R.id.btnDues);
        btnCreateGoal = (Button) findViewById(R.id.btnCreateGoal);
        btnAddBonus = (Button) findViewById(R.id.btnAddBonus);
        btnAccount = (ImageButton) findViewById(R.id.btnAccount);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(new Date());

        Intent intent = getIntent();
        user_id = intent.getExtras().getString("id");
        username = intent.getExtras().getString("username");

        tvUsername.setText(username);

        getEarningsAsyncTask getearningsAsyncTask = new getEarningsAsyncTask();
        getearningsAsyncTask.execute();

        btnEarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_savings.this, activity_earnings.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_savings.this, activity_expenses.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnDues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_savings.this, activity_dues.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_savings.this, activity_account.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("monthly_earnings", monthly_earnings);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        btnAddBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder addBonus_builder = new AlertDialog.Builder(activity_savings.this);
                View addBonusView = getLayoutInflater().inflate(R.layout.add_bonus_dialog, null);

                addBonus_builder.setTitle("Update Finances");

                EditText edtAddBonus = (EditText) addBonusView.findViewById(R.id.edtAddBonus);

                addBonus_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                addBonus_builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String amount = edtAddBonus.getText().toString();

                        if(!amount.equals("")) {
                            StringRequest request = new StringRequest(Request.Method.POST, url_addBonus, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {


                                    Toast.makeText(activity_savings.this, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity_savings.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();

                                    param.put("user_id", user_id);
                                    param.put("amount", amount);
                                    param.put("date", date);

                                    return param;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(activity_savings.this);
                            requestQueue.add(request);

                            dialogInterface.dismiss();
                            finish();
                            startActivity(getIntent());
                        }else {
                            Toast.makeText(getApplicationContext(), "Please answer all required fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                addBonus_builder.setView(addBonusView);
                AlertDialog addBonusDialog = addBonus_builder.create();
                addBonusDialog.show();
            }
        });

        btnCreateGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder createGoal_builder = new AlertDialog.Builder(activity_savings.this);
                View payDuesView = getLayoutInflater().inflate(R.layout.create_goal_dialog, null);

                createGoal_builder.setTitle("Create Savings Goal");

                EditText edtGoalName = (EditText) payDuesView.findViewById(R.id.edtGoalName);
                EditText edtGoalAmount = (EditText) payDuesView.findViewById(R.id.edtGoalAmount);

                createGoal_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                createGoal_builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = edtGoalName.getText().toString();
                        String amount = edtGoalAmount.getText().toString();

                        if(!name.equals("") && !amount.equals("")) {
                            StringRequest request = new StringRequest(Request.Method.POST, url_createGoal, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {


                                    Toast.makeText(activity_savings.this, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity_savings.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();

                                    param.put("user_id", user_id);
                                    param.put("name", name);
                                    param.put("amount", amount);

                                    return param;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(activity_savings.this);
                            requestQueue.add(request);

                            dialogInterface.dismiss();
                            finish();
                            startActivity(getIntent());
                        }else {
                            Toast.makeText(getApplicationContext(), "Please answer all required fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                createGoal_builder.setView(payDuesView);
                AlertDialog addExpenseDialog = createGoal_builder.create();
                addExpenseDialog.show();
            }
        });
    }

    private void getSavingGoal() {
        StringRequest request = new StringRequest(Request.Method.POST, url_fetchGoal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {

                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            goal_name = object.getString("name");
                            goal_amount = object.getString("amount");

                        }
                        tvGoalName.setText("Saving Goal: " + goal_name);
                        num_goal_amount = Double.parseDouble(goal_amount);
                        num_remaining_amount = num_goal_amount - num_savings_amount;
                        tvRemainingAmount.setText("Remaining amount to save: " + numbersformat.format(num_remaining_amount));

                        fetchAccountData();
                    }
                } catch (Exception e) {
                }
                //uncomment out Toast for troubleshooting
                //Toast.makeText(activity_savings.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_savings.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(activity_savings.this);
        requestQueue.add(request);
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

                                monthly_earnings = object.getString("monthly_earnings");

                            }
                            double num_monthly_earnings = Double.parseDouble(monthly_earnings);
                            double num_annual_earnings = num_monthly_earnings *12;
                            num_daily_earnings = num_annual_earnings /365;

                            getBonusAsyncTask getbonusAsyncTask = new getBonusAsyncTask();
                            getbonusAsyncTask.execute();

                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_savings.this, response.toString(), Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_savings.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", user_id);
                    param.put("date", date);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_savings.this);
            requestQueue.add(request);

            return null;
        }
    }

    private class getBonusAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchDailyBonus, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            num_bonus = 0.0;
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String amount = object.getString("amount");
                                num_bonus = Double.parseDouble(amount);
                            }
                            num_total_savings = num_daily_earnings + num_bonus;

                            getDailyExpensesAsyncTask getdailyexpensesAsyncTask = new getDailyExpensesAsyncTask();
                            getdailyexpensesAsyncTask.execute();
                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_savings.this, response.toString(), Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_savings.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", user_id);
                    param.put("date", date);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_savings.this);
            requestQueue.add(request);

            return null;
        }
    }

    private class getDailyExpensesAsyncTask extends AsyncTask<Void, Void, Void> {
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


                            }
                            num_total_savings = num_total_savings - num_total_daily;

                            getDailyDuesAsyncTask getdailyduesAsyncTask = new getDailyDuesAsyncTask();
                            getdailyduesAsyncTask.execute();

                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_savings.this, response.toString(), Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_savings.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", user_id);
                    param.put("date", date);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_savings.this);
            requestQueue.add(request);

            return null;
        }
    }

    private class getDailyDuesAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchDailyDues, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            num_total_dailydues = 0.0;
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String cost = object.getString("cost");
                                double num_cost = Double.parseDouble(cost);
                                num_total_dailydues = num_total_dailydues + num_cost;

                            }
                            num_total_savings = num_total_savings - num_total_dailydues;

                            getDailyDebtPayAsyncTask getdailydebtpayAsyncTask = new getDailyDebtPayAsyncTask();
                            getdailydebtpayAsyncTask.execute();

                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_savings.this, response.toString(), Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_savings.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", user_id);
                    param.put("date", date);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_savings.this);
            requestQueue.add(request);

            return null;
        }
    }

    private class getDailyDebtPayAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchDailyDebtPayment, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            num_total_dailydebtpayment = 0.0;
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String amount = object.getString("amount");
                                double num_amount = Double.parseDouble(amount);
                                num_total_dailydebtpayment = num_total_dailydebtpayment + num_amount;

                            }
                            num_total_savings = num_total_savings - num_total_dailydebtpayment;
                            add_total_savings = Double.toString(num_total_savings);

                            addSavingsAsyncTask addsavingsAsyncTask = new addSavingsAsyncTask();
                            addsavingsAsyncTask.execute();
                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_savings.this, response.toString(), Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_savings.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", user_id);
                    param.put("date", date);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_savings.this);
            requestQueue.add(request);

            return null;
        }
    }

    private class addSavingsAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            StringRequest request = new StringRequest(Request.Method.POST, url_addSavings, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    fetchSavingsAsyncTask fetchsavingsAsyncTask = new fetchSavingsAsyncTask();
                    fetchsavingsAsyncTask.execute();


                    Toast.makeText(activity_savings.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_savings.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();

                    param.put("user_id", user_id);
                    param.put("amount", add_total_savings);
                    param.put("date", date);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_savings.this);
            requestQueue.add(request);

            return null;
        }
    }

    private class fetchSavingsAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchSavings, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            num_savings_amount = 0.0;
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                savings_amount = object.getString("amount");
                                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                            num_savings_amount = Double.parseDouble(savings_amount);
                            tvSavingsTotal.setText("" + numbersformat.format(num_savings_amount));

                            getSavingGoal();
                        }

                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_savings.this, response.toString(), Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_savings.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("user_id", user_id);
                    param.put("date", date);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_savings.this);
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
                Toast.makeText(activity_savings.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(activity_savings.this);
        requestQueue.add(request);
    }

}