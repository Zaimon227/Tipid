package com.example.tipid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adapter_debts extends ArrayAdapter<dataclass_debts> {

    Context context;
    List<dataclass_debts> dataclassDebts;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    String url_payDebt = "" + address + "/payDebt.php";
    String url_getUsername = "" + address + "/getUsername.php";
    String url_deleteDebt = "" + address + "/deleteDebt.php";

    public static String user_id = "";
    public static String username = "";
    public static String date = "";
    public static String debt_id = "";
    public static String name = "";
    public static String old_PaidAmount = "";
    public static String total_debt = "";

    public adapter_debts(@NonNull Context context, List<dataclass_debts>dataclass_debtsList) {
        super(context, R.layout.data_item_debt_record, dataclass_debtsList);

        this.context = context;
        this.dataclassDebts = dataclass_debtsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item_debt_record, null, true);

        TextView tvDebtName = view.findViewById(R.id.tvDebtName);
        TextView tvPaidAmount = view.findViewById(R.id.tvPaidAmount);
        TextView tvTotalDebt = view.findViewById(R.id.tvTotalDebt);
        TextView tvSlash = view.findViewById(R.id.tvSlash);
        TextView tvUserID = view.findViewById(R.id.tvUserID);
        TextView tvDebtID = view.findViewById(R.id.tvDebtID);
        ImageButton btnDebtDetails = view.findViewById(R.id.btnDebtDetails);
        ImageButton btnDeleteDebt = view.findViewById(R.id.btnDeleteDebt);

        tvUserID.setVisibility(View.INVISIBLE);
        tvDebtID.setVisibility(View.INVISIBLE);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(new Date());

        tvDebtName.setText(dataclassDebts.get(position).getName());
        tvPaidAmount.setText(dataclassDebts.get(position).getPaid_amount());
        tvTotalDebt.setText(dataclassDebts.get(position).getTotal_debt());
        tvUserID.setText(dataclassDebts.get(position).getUser_id());
        tvDebtID.setText(dataclassDebts.get(position).getDebt_id());

        user_id = tvUserID.getText().toString();
        getUsername();

        String name = tvDebtName.getText().toString();
        String old_PaidAmount = tvPaidAmount.getText().toString();
        String total_debt = tvTotalDebt.getText().toString();
        String debt_id = tvDebtID.getText().toString();

        btnDebtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), DebtName + " PayDebt button was clicked", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder addDebt_builder = new AlertDialog.Builder(view.getRootView().getContext());
                View payDebtView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.pay_debt_dialog, null);

                addDebt_builder.setTitle("Pay Debt");

                TextView tvPayAmountHint = (TextView) payDebtView.findViewById(R.id.tvPayAmountHint);
                TextView tvPayTotalDebt = (TextView) payDebtView.findViewById(R.id.tvPayTotalDebt);
                TextView tvPayDebtName = (TextView) payDebtView.findViewById(R.id.tvPayDebtName);
                EditText edtPayDebt = (EditText) payDebtView.findViewById(R.id.edtAddDebtName);


                tvPayDebtName.setText(name);
                Double num_old_PaidAmount = Double.parseDouble(old_PaidAmount);
                Double num_TotalDebt = Double.parseDouble(total_debt);
                Double num_remainingBalance = num_TotalDebt - num_old_PaidAmount;
                String remainingBalance = String.valueOf(num_remainingBalance);
                tvPayAmountHint.setText("Remaining Balance: " + remainingBalance);
                tvPayTotalDebt.setText("Total Debt: " + total_debt);

                addDebt_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                addDebt_builder.setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String new_PaidAmount = edtPayDebt.getText().toString();

                        if(!new_PaidAmount.equals("")){
                            Double num_new_PaidAmount = Double.parseDouble(new_PaidAmount);
                            Double num_old_PaidAmount = Double.parseDouble(old_PaidAmount);
                            Double num_current_PaidAmount = num_old_PaidAmount + num_new_PaidAmount;
                            String current_PaidAmount = String.valueOf(num_current_PaidAmount);
                            StringRequest request = new StringRequest(Request.Method.POST, url_payDebt, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    //nameEt.setText("");
                                    //ageEt.setText("");
                                    //genderEt.setText("");
                                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();

                                    param.put("user_id", user_id);
                                    param.put("current_PaidAmount", current_PaidAmount);
                                    param.put("name", name);
                                    param.put("debt_id", debt_id);
                                    param.put("new_PaidAmount", new_PaidAmount);
                                    param.put("date", date);

                                    return param;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(request);

                            dialogInterface.dismiss();
                            Intent intent = new Intent(getContext(), activity_dues.class);
                            intent.putExtra("id", user_id);
                            intent.putExtra("username", username);
                            context.startActivity(intent);

                        }else {
                            Toast.makeText(context, "Please answer all required fields", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                addDebt_builder.setView(payDebtView);
                AlertDialog addExpenseDialog = addDebt_builder.create();
                addExpenseDialog.show();
            }
        });

        btnDeleteDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), DebtName + " PayDebt button was clicked", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder deleteDebt_builder = new AlertDialog.Builder(view.getRootView().getContext());
                View deleteDebtView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.delete_debt_dialog, null);

                deleteDebt_builder.setTitle("Delete Debt");

                TextView tvDeleteMessage = (TextView) deleteDebtView.findViewById(R.id.tvDeleteMessage);

                tvDeleteMessage.setText("Are you sure you want to delete " + name + "?");

                deleteDebt_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                deleteDebt_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                            StringRequest request = new StringRequest(Request.Method.POST, url_deleteDebt, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    //nameEt.setText("");
                                    //ageEt.setText("");
                                    //genderEt.setText("");
                                    Toast.makeText(view.getRootView().getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();

                                    param.put("user_id", user_id);
                                    param.put("debt_id", debt_id);

                                    return param;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                            requestQueue.add(request);

                            dialogInterface.dismiss();
                            Intent intent = new Intent(context, activity_dues.class);
                            intent.putExtra("id", user_id);
                            intent.putExtra("username", username);
                            context.startActivity(intent);

                    }
                });
                deleteDebt_builder.setView(deleteDebtView);
                AlertDialog deleteDebtDialog = deleteDebt_builder.create();
                deleteDebtDialog.show();
            }
        });

        return view;
    }

    private void getUsername() {

        StringRequest request = new StringRequest(Request.Method.POST, url_getUsername, new Response.Listener<String>() {
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
                        }
                    }
                } catch (Exception e) {
                }
                //Toast.makeText(activity_login.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
}
