package com.example.tipid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class activity_account extends AppCompatActivity {
    TextView tvAccountUsername, tvAccountMonthlyEarnings, tvAccountEmail, tvTotalExpensesNum, tvTotalDuesNum, tvTotalDebtPaymentsNum;
    ImageButton btnChangeUsername, btnChangeMonthlyEarnings, btnChangeEmail;
    Button btnCancel, btnChangePassword;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    String url_updateUsername = "" + address + "/updateUsername.php";
    String url_updatePassword = "" + address + "/updatePassword.php";
    String url_updateMonthlyEarnings = "" + address + "/updateMonthlyEarnings.php";
    String url_updateEmail = "" + address + "/updateEmail.php";
    String url_fetchTotalExpenses = "" + address + "/fetchTotalExpenses.php";
    String url_fetchTotalDues = "" + address + "/fetchTotalDues.php";
    String url_fetchTotalDebtPayments = "" + address + "/fetchTotalDebtPayments.php";

    DecimalFormat numbersformat = new DecimalFormat("###,###,###,###,###.##");

    public static String user_id = "";
    public static String username = "";
    public static String password = "";
    public static String monthly_earnings = "";
    public static String email = "";

    public Double num_total_expenses;
    public Double num_total_dues;
    public Double num_total_debtpayments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        tvAccountUsername = (TextView) findViewById(R.id.tvAccountUsername);
        tvAccountMonthlyEarnings = (TextView) findViewById(R.id.tvAccountMonthlyEarnings);
        tvAccountEmail = (TextView) findViewById(R.id.tvAccountEmail);
        tvTotalExpensesNum = (TextView) findViewById(R.id.tvTotalExpensesNum);
        tvTotalDuesNum = (TextView) findViewById(R.id.tvTotalDuesNum);
        tvTotalDebtPaymentsNum = (TextView) findViewById(R.id.tvTotalDebtPaymentsNum);

        btnChangeUsername = (ImageButton) findViewById(R.id.btnChangeUsername);
        btnChangeMonthlyEarnings = (ImageButton) findViewById(R.id.btnChangeMonthlyEarnings);
        btnChangeEmail = (ImageButton) findViewById(R.id.btnChangeEmail);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);

        Intent intent = getIntent();
        user_id = intent.getExtras().getString("id");
        username = intent.getExtras().getString("username");
        password = intent.getExtras().getString("password");
        monthly_earnings = intent.getExtras().getString("monthly_earnings");
        email = intent.getExtras().getString("email");

        tvAccountUsername.setText(username);
        Double num_monthly_earnings = Double.parseDouble(monthly_earnings);
        tvAccountMonthlyEarnings.setText("" + numbersformat.format(num_monthly_earnings));
        tvAccountEmail.setText(email);

        activity_account.getTotalExpensesAsyncTask totalearningsAsyncTask = new activity_account.getTotalExpensesAsyncTask();
        totalearningsAsyncTask.execute();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_account.this, activity_earnings.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder changeUsername_builder = new AlertDialog.Builder(activity_account.this);
                View changeUsernameView = getLayoutInflater().inflate(R.layout.edit_username_dialog, null);

                changeUsername_builder.setTitle("Change Username");

                TextView tvOldUsername = (TextView) changeUsernameView.findViewById(R.id.tvOldUsername);
                EditText edtNewUsername = (EditText) changeUsernameView.findViewById(R.id.edtNewUsername);

                tvOldUsername.setText(username);

                changeUsername_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                changeUsername_builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newUsername = edtNewUsername.getText().toString();


                        if(!newUsername.equals("")){
                            StringRequest request = new StringRequest(Request.Method.POST, url_updateUsername, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(activity_account.this, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity_account.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();

                                    param.put("user_id", user_id);
                                    param.put("newUsername", newUsername);

                                    return param;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(activity_account.this);
                            requestQueue.add(request);

                            dialogInterface.dismiss();
                        }else {
                            Toast.makeText(getApplicationContext(), "Please answer all required fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                changeUsername_builder.setView(changeUsernameView);
                AlertDialog changeUsernameDialog = changeUsername_builder.create();
                changeUsernameDialog.show();
            }
        });

        btnChangeMonthlyEarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder changeMonthlyEarnings_builder = new AlertDialog.Builder(activity_account.this);
                View changeMonthlyEarningsView = getLayoutInflater().inflate(R.layout.edit_monthlyearnings_dialog, null);

                changeMonthlyEarnings_builder.setTitle("Change Username");

                TextView tvOldMonthlyEarnings = (TextView) changeMonthlyEarningsView.findViewById(R.id.tvOldMonthlyEarnings);
                EditText edtNewMonthlyEarnings = (EditText) changeMonthlyEarningsView.findViewById(R.id.edtNewMonthlyEarnings);

                tvOldMonthlyEarnings.setText(monthly_earnings);

                changeMonthlyEarnings_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                changeMonthlyEarnings_builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newMonthlyEarnings = edtNewMonthlyEarnings.getText().toString();


                        if(!newMonthlyEarnings.equals("")){
                            StringRequest request = new StringRequest(Request.Method.POST, url_updateMonthlyEarnings, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(activity_account.this, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity_account.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();

                                    param.put("user_id", user_id);
                                    param.put("newMonthlyEarnings", newMonthlyEarnings);

                                    return param;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(activity_account.this);
                            requestQueue.add(request);

                            dialogInterface.dismiss();
                        }else {
                            Toast.makeText(getApplicationContext(), "Please answer all required fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                changeMonthlyEarnings_builder.setView(changeMonthlyEarningsView);
                AlertDialog changeMonthlyEarningsDialog = changeMonthlyEarnings_builder.create();
                changeMonthlyEarningsDialog.show();
            }
        });

        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder changeEmail_builder = new AlertDialog.Builder(activity_account.this);
                View changeEmailView = getLayoutInflater().inflate(R.layout.edit_email_dialog, null);

                changeEmail_builder.setTitle("Change Username");

                TextView tvOldEmail = (TextView) changeEmailView.findViewById(R.id.tvOldEmail);
                EditText edtNewEmail = (EditText) changeEmailView.findViewById(R.id.edtNewEmail);

                tvOldEmail.setText(email);

                changeEmail_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                changeEmail_builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newEmail = edtNewEmail.getText().toString();


                        if(!newEmail.equals("")){
                            StringRequest request = new StringRequest(Request.Method.POST, url_updateEmail, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(activity_account.this, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity_account.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();

                                    param.put("user_id", user_id);
                                    param.put("newEmail", newEmail);

                                    return param;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(activity_account.this);
                            requestQueue.add(request);

                            dialogInterface.dismiss();
                        }else {
                            Toast.makeText(getApplicationContext(), "Please answer all required fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                changeEmail_builder.setView(changeEmailView);
                AlertDialog changeEmailDialog = changeEmail_builder.create();
                changeEmailDialog.show();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder changePassword_builder = new AlertDialog.Builder(activity_account.this);
                View changePasswordView = getLayoutInflater().inflate(R.layout.edit_password_dialog, null);

                changePassword_builder.setTitle("Change Username");

                EditText edtVerifyOldPassword = (EditText) changePasswordView.findViewById(R.id.edtVerifyOldPassword);
                EditText edtNewPassword = (EditText) changePasswordView.findViewById(R.id.edtNewPassword);

                changePassword_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                changePassword_builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String verifyPassword = edtVerifyOldPassword.getText().toString();
                        String newPassword = edtNewPassword.getText().toString();

                        if(!verifyPassword.equals("") && !newPassword.equals("")){
                            StringRequest request = new StringRequest(Request.Method.POST, url_updatePassword, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(activity_account.this, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity_account.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();

                                    param.put("user_id", user_id);
                                    param.put("password", password);
                                    param.put("verifyPassword", verifyPassword);
                                    param.put("newPassword", newPassword);

                                    return param;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(activity_account.this);
                            requestQueue.add(request);

                            dialogInterface.dismiss();
                        }else {
                            Toast.makeText(getApplicationContext(), "Please answer all required fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                changePassword_builder.setView(changePasswordView);
                AlertDialog changePasswordDialog = changePassword_builder.create();
                changePasswordDialog.show();
            }
        });
    }

    private class getTotalExpensesAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchTotalExpenses, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            num_total_expenses = 0.0;
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String cost = object.getString("cost");
                                double num_cost = Double.parseDouble(cost);
                                num_total_expenses = num_total_expenses + num_cost;

                            }
                            tvTotalExpensesNum.setText("" + numbersformat.format(num_total_expenses));

                            activity_account.getTotalDuesAsyncTask totalduesAsyncTask = new activity_account.getTotalDuesAsyncTask();
                            totalduesAsyncTask.execute();
                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_earnings.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_account.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", user_id);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_account.this);
            requestQueue.add(request);

            return null;
        }
    }

    private class getTotalDuesAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchTotalDues, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            num_total_dues = 0.0;
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String cost = object.getString("cost");
                                double num_cost = Double.parseDouble(cost);
                                num_total_dues = num_total_dues + num_cost;

                            }
                            tvTotalDuesNum.setText("" + numbersformat.format(num_total_dues));

                            activity_account.getTotalDebtPayAsyncTask totaldebtpayAsyncTask = new activity_account.getTotalDebtPayAsyncTask();
                            totaldebtpayAsyncTask.execute();

                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_earnings.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_account.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", user_id);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_account.this);
            requestQueue.add(request);

            return null;
        }
    }

    private class getTotalDebtPayAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchTotalDebtPayments, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            num_total_debtpayments = 0.0;
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String amount = object.getString("amount");
                                double num_amount = Double.parseDouble(amount);
                                num_total_debtpayments = num_total_debtpayments + num_amount;

                            }
                            tvTotalDebtPaymentsNum.setText("" + numbersformat.format(num_total_debtpayments));

                        }
                    } catch (Exception e) {
                    }
                    //uncomment out Toast for troubleshooting
                    //Toast.makeText(activity_earnings.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_account.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", user_id);

                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity_account.this);
            requestQueue.add(request);

            return null;
        }
    }

}