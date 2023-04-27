package com.example.tipid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adapter_expenses extends ArrayAdapter<dataclass_expenses> {

    Context context;
    List<dataclass_expenses> dataclassExpenses;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    String url_getUsername = "" + address + "/getUsername.php";
    String url_deleteExpense = "" + address + "/deleteExpense.php";

    public static String user_id = "";
    public static String username = "";


    public adapter_expenses(@NonNull Context context, List<dataclass_expenses>dataclass_expensesList) {
        super(context, R.layout.data_item_expense_record, dataclass_expensesList);

        this.context = context;
        this.dataclassExpenses = dataclass_expensesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item_expense_record, null, true);

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvCategory = view.findViewById(R.id.tvCategory);
        TextView tvCost = view.findViewById(R.id.tvCost);
        TextView tvExpenseID = view.findViewById(R.id.tvExpenseID);
        TextView tvUserID = view.findViewById(R.id.tvUserID);
        ImageButton btnDeleteExpense = view.findViewById(R.id.btnDeleteExpense);

        tvUserID.setVisibility(View.INVISIBLE);
        tvExpenseID.setVisibility(View.INVISIBLE);

        tvName.setText(dataclassExpenses.get(position).getName());
        tvCategory.setText(dataclassExpenses.get(position).getCategory());
        tvCost.setText(dataclassExpenses.get(position).getCost());
        tvExpenseID.setText(dataclassExpenses.get(position).getExpense_id());
        tvUserID.setText(dataclassExpenses.get(position).getUser_id());

        String name = tvName.getText().toString();
        String expense_id = tvExpenseID.getText().toString();
        user_id = tvUserID.getText().toString();
        getUsername();

        btnDeleteExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder deleteExpense_builder = new AlertDialog.Builder(view.getRootView().getContext());
                View deleteExpenseView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.delete_expense_dialog, null);

                deleteExpense_builder.setTitle("Delete Expense");

                TextView tvDeleteMessage = (TextView) deleteExpenseView.findViewById(R.id.tvDeleteMessage);

                tvDeleteMessage.setText("Are you sure you want to delete " + name + "?");

                deleteExpense_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                deleteExpense_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        StringRequest request = new StringRequest(Request.Method.POST, url_deleteExpense, new Response.Listener<String>() {
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


                                param.put("expense_id", expense_id);


                                return param;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(request);

                        dialogInterface.dismiss();
                        Intent intent = new Intent(getContext(), activity_expenses.class);
                        intent.putExtra("id", user_id);
                        intent.putExtra("username", username);
                        context.startActivity(intent);
                    }
                });
                deleteExpense_builder.setView(deleteExpenseView);
                AlertDialog deleteExpenseDialog = deleteExpense_builder.create();
                deleteExpenseDialog.show();
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

