package com.example.tipid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class activity_expenses extends AppCompatActivity {

    Button btnEarnings, btnDues, btnSavings, btnHistory, btnBreakdown;
    TextView tvUsername, tvRecordLabel, tvExpensesTotal;
    ImageButton btnAddExpense, btnAccount;
    ListView lvExpensesRecord;
    dataclass_expenses dataclassExpenses;
    adapter_expenses adapterExpenses;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    public static String user_id = "";
    public static String username = "";
    public static String date = "";
    public static String password = "";
    public static String email = "";
    public static String monthly_earnings = "";

    public Double num_total_expenses;

    DecimalFormat numbersformat = new DecimalFormat("###,###,###,###,###.##");

    String url_addExpense = "" + address + "/addExpense.php";
    String url_fetchDailyExpense = "" + address + "/fetchDailyExpense.php";
    String url_fetchAccountData = "" + address + "/fetchAccountData.php";

    public static ArrayList<dataclass_expenses> dataclassExpensesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        btnEarnings = (Button) findViewById(R.id.btnEarnings);
        btnDues = (Button) findViewById(R.id.btnDues);
        btnSavings = (Button) findViewById(R.id.btnSavings);
        btnHistory = (Button) findViewById(R.id.btnHistory);
        btnBreakdown = (Button) findViewById(R.id.btnBreakdown);
        btnAddExpense = (ImageButton) findViewById(R.id.btnAddExpense);
        btnAccount = (ImageButton) findViewById(R.id.btnAccount);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvRecordLabel = (TextView) findViewById(R.id.tvRecordLabel);
        tvExpensesTotal = (TextView) findViewById(R.id.tvExpensesTotal);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(new Date());

        lvExpensesRecord = (ListView) findViewById(R.id.lvExpensesRecord);
        adapterExpenses = new adapter_expenses (this, dataclassExpensesArrayList);
        lvExpensesRecord.setAdapter(adapterExpenses);

        Intent intent = getIntent();
        user_id = intent.getExtras().getString("id");
        username = intent.getExtras().getString("username");

        tvUsername.setText(username);
        tvRecordLabel.setText("Expenses Record of " + date);

        activity_expenses.getDailyExpensesAsyncTask dailyexpensesAsyncTask = new activity_expenses.getDailyExpensesAsyncTask();
        dailyexpensesAsyncTask.execute();
        /*try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        btnEarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_expenses.this, activity_earnings.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnDues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_expenses.this, activity_dues.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnSavings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_expenses.this, activity_savings.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_expenses.this, activity_account.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("monthly_earnings", monthly_earnings);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_expenses.this, activity_history.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnBreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_expenses.this, activity_breakdown.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder addExpense_builder = new AlertDialog.Builder(activity_expenses.this);
                View addExpenseView = getLayoutInflater().inflate(R.layout.add_expense_dialog, null);

                addExpense_builder.setTitle("Add Expense Record");

                EditText edtExpenseName = (EditText) addExpenseView.findViewById(R.id.edtAddDebtName);
                EditText edtExpenseCost = (EditText) addExpenseView.findViewById(R.id.edtPayDueCost);

                Spinner spnExpenseCategory = (Spinner) addExpenseView.findViewById(R.id.spnExpenseCategory);
                ArrayAdapter<String> addExpenseAdapter = new ArrayAdapter<String>(activity_expenses.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.DailyExpenseCategories));
                addExpenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnExpenseCategory.setAdapter(addExpenseAdapter);

                addExpense_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                addExpense_builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = edtExpenseName.getText().toString();
                        String category = spnExpenseCategory.getSelectedItem().toString();
                        String cost = edtExpenseCost.getText().toString();

                        if(!name.equals("") && !category.equals("") && !cost.equals("")){

                            StringRequest request = new StringRequest(Request.Method.POST, url_addExpense, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(activity_expenses.this, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity_expenses.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();

                                    param.put("user_id", user_id);
                                    param.put("name", name);
                                    param.put("category", category);
                                    param.put("cost", cost);
                                    param.put("date", date);

                                    return param;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(activity_expenses.this);
                            requestQueue.add(request);

                            dialogInterface.dismiss();
                            finish();
                            startActivity(getIntent());
                        }else {
                            Toast.makeText(getApplicationContext(), "Please answer all required fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                addExpense_builder.setView(addExpenseView);
                AlertDialog addExpenseDialog = addExpense_builder.create();
                addExpenseDialog.show();
            }
        });
    }

    private class getDailyExpensesAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, url_fetchDailyExpense, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    dataclassExpensesArrayList.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (success.equals("1")) {
                            num_total_expenses = 0.0;
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String name = object.getString("name");
                                String category = object.getString("category");
                                String cost = object.getString("cost");
                                String expense_id = object.getString("id");

                                Double num_cost = Double.parseDouble(cost);
                                num_total_expenses = num_total_expenses + num_cost;

                                dataclassExpenses = new dataclass_expenses(name, category, cost, expense_id, user_id);
                                dataclassExpensesArrayList.add(dataclassExpenses);
                                adapterExpenses.notifyDataSetChanged();
                            }
                            tvExpensesTotal.setText("Total: " + numbersformat.format(num_total_expenses));
                            fetchAccountData();
                        }

                    } catch (Exception e) {

                    }
                    //Toast.makeText(ShowData.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_expenses.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

            RequestQueue requestQueue = Volley.newRequestQueue(activity_expenses.this);
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
                //Toast.makeText(activity_expenses.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_expenses.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(activity_expenses.this);
        requestQueue.add(request);
    }

}