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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class activity_dues extends AppCompatActivity {

    TextView tvUsername;
    Button btnEarnings, btnExpenses, btnSavings, btnPayDues, btnDuesHistory, btnFixed;
    ListView lvDebtRecord, lvDuesRecord;
    ImageButton btnAddDebt, btnAccount;
    dataclass_debts dataclassDebts;
    adapter_debts adapterDebts;
    dataclass_dues dataclassDues;
    adapter_dues adapterDues;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    public static String user_id ="";
    public static String username ="";
    public static String date ="";
    public static String password = "";
    public static String email = "";
    public static String monthly_earnings = "";
    public static String name = "";
    public static String cost = "";

    String url_addDebt = "" + address + "/addDebt.php";
    String url_fetchDebtRecord = "" + address + "/fetchDebtRecord.php";
    String url_payDues = "" + address + "/payDues.php";
    String url_fetchAccountData = "" + address + "/fetchAccountData.php";
    String url_fetchDuesRecord = "" + address + "/fetchDuesRecord.php";
    String url_fetchFixedDuesRecord = "" + address + "/fetchFixedDuesRecord.php";
    String url_payFixedDues = "" + address + "/payFixedDues.php";

    public static ArrayList<dataclass_debts> dataclassDebtsArrayList = new ArrayList<>();
    public static ArrayList<dataclass_dues> dataclassDuesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dues);

        tvUsername = (TextView) findViewById(R.id.tvUsername);

        btnEarnings = (Button) findViewById(R.id.btnEarnings);
        btnExpenses = (Button) findViewById(R.id.btnExpenses);
        btnSavings = (Button) findViewById(R.id.btnSavings);
        btnPayDues = (Button) findViewById(R.id.btnPayDues);
        btnDuesHistory = (Button) findViewById(R.id.btnDuesHistory);
        btnFixed = (Button) findViewById(R.id.btnFixed);

        btnAddDebt = (ImageButton) findViewById(R.id.btnAddDebt);
        btnAccount = (ImageButton) findViewById(R.id.btnAccount);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(new Date());

        lvDebtRecord = (ListView) findViewById(R.id.lvDebtRecord);
        adapterDebts = new adapter_debts(this, dataclassDebtsArrayList);
        lvDebtRecord.setAdapter(adapterDebts);

        lvDuesRecord = (ListView) findViewById(R.id.lvDuesRecord);
        adapterDues = new adapter_dues(this, dataclassDuesArrayList);
        lvDuesRecord.setAdapter(adapterDues);

        Intent intent = getIntent();
        user_id = intent.getExtras().getString("id");
        username = intent.getExtras().getString("username");

        tvUsername.setText(username);

        getDebtsRecord();
        payFixedDues();

        btnEarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_dues.this, activity_earnings.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_dues.this, activity_expenses.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnSavings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_dues.this, activity_savings.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_dues.this, activity_account.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("monthly_earnings", monthly_earnings);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        btnDuesHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_dues.this, activity_dueshistory.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnFixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_dues.this, activity_fixeddues.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnPayDues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder payDues_builder = new AlertDialog.Builder(activity_dues.this);
                View payDuesView = getLayoutInflater().inflate(R.layout.pay_dues_dialog, null);

                payDues_builder.setTitle("Pay Monthly Due");

                EditText edtPayDueName = (EditText) payDuesView.findViewById(R.id.edtPayDueName);
                EditText edtPayDueCost = (EditText) payDuesView.findViewById(R.id.edtPayDueCost);

                payDues_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                payDues_builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = edtPayDueName.getText().toString();
                        String cost = edtPayDueCost.getText().toString();

                        if(!name.equals("") && !cost.equals("")){
                            StringRequest request = new StringRequest(Request.Method.POST, url_payDues, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(activity_dues.this, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity_dues.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();

                                    param.put("user_id", user_id);
                                    param.put("name", name);
                                    param.put("cost", cost);
                                    param.put("date", date);

                                    return param;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(activity_dues.this);
                            requestQueue.add(request);

                            dialogInterface.dismiss();
                            finish();
                            startActivity(getIntent());
                        }else {
                            Toast.makeText(getApplicationContext(), "Please answer all required fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                payDues_builder.setView(payDuesView);
                AlertDialog addExpenseDialog = payDues_builder.create();
                addExpenseDialog.show();

            }
        });

        btnAddDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder addDebt_builder = new AlertDialog.Builder(activity_dues.this);
                View addDebtView = getLayoutInflater().inflate(R.layout.add_debt_dialog, null);

                addDebt_builder.setTitle("Add Debt Record");

                EditText edtDebtName = (EditText) addDebtView.findViewById(R.id.edtAddDebtName);
                EditText edtTotalDebt = (EditText) addDebtView.findViewById(R.id.edtAddDebtCost);

                addDebt_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                addDebt_builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = edtDebtName.getText().toString();
                        String total_debt = edtTotalDebt.getText().toString();
                        String paid_amount = "0";

                        if(!name.equals("") && !total_debt.equals("") && !paid_amount.equals("")){
                            StringRequest request = new StringRequest(Request.Method.POST, url_addDebt, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(activity_dues.this, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity_dues.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();

                                    param.put("user_id", user_id);
                                    param.put("name", name);
                                    param.put("paid_amount", paid_amount);
                                    param.put("total_debt", total_debt);
                                    param.put("date", date);

                                    return param;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(activity_dues.this);
                            requestQueue.add(request);

                            dialogInterface.dismiss();
                            finish();
                            startActivity(getIntent());
                        }else {
                            Toast.makeText(getApplicationContext(), "Please answer all required fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                addDebt_builder.setView(addDebtView);
                AlertDialog addExpenseDialog = addDebt_builder.create();
                addExpenseDialog.show();
            }
        });

    }

    private void getDebtsRecord() {
        StringRequest request = new StringRequest(Request.Method.POST, url_fetchDebtRecord, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataclassDebtsArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String name = object.getString("name");
                            String paid_amount = object.getString("paid_amount");
                            String total_debt = object.getString("total_debt");
                            String debt_id = object.getString("debt_id");

                            dataclassDebts = new dataclass_debts(name, paid_amount, total_debt, user_id, debt_id);
                            dataclassDebtsArrayList.add(dataclassDebts);
                            adapterDebts.notifyDataSetChanged();
                        }
                        getDuesRecord();
                    }

                } catch (Exception e) {

                }

                //Toast.makeText(ShowData.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_dues.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(activity_dues.this);
        requestQueue.add(request);
    }

    private void getDuesRecord() {
        StringRequest request = new StringRequest(Request.Method.POST, url_fetchDuesRecord, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataclassDuesArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String name = object.getString("name");
                            String cost = object.getString("cost");
                            String due_id = object.getString("due_id");

                            dataclassDues = new dataclass_dues(name, cost, user_id, due_id);
                            dataclassDuesArrayList.add(dataclassDues);
                            adapterDues.notifyDataSetChanged();
                        }
                        fetchAccountData();
                    }

                } catch (Exception e) {

                }

                //Toast.makeText(ShowData.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_dues.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(activity_dues.this);
        requestQueue.add(request);
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
                Toast.makeText(activity_dues.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(activity_dues.this);
        requestQueue.add(request);
    }

    private void payFixedDues() {
        StringRequest request = new StringRequest(Request.Method.POST, url_fetchFixedDuesRecord, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            name = object.getString("name");
                            cost = object.getString("name");

                            StringRequest request = new StringRequest(Request.Method.POST, url_payFixedDues, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    //Toast.makeText(activity_dues.this, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity_dues.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();

                                    param.put("user_id", user_id);
                                    param.put("name", name);
                                    param.put("cost", cost);
                                    param.put("date", date);

                                    return param;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(activity_dues.this);
                            requestQueue.add(request);
                        }
                    }

                } catch (Exception e) {

                }
                //Toast.makeText(activity_earnings.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_dues.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(activity_dues.this);
        requestQueue.add(request);
    }

}