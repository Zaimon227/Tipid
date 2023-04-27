package com.example.tipid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adapter_dues extends ArrayAdapter<dataclass_dues> {

    Context context;
    List<dataclass_dues> dataclassDues;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    String url_getUsername = "" + address + "/getUsername.php";
    String url_deleteDue = "" + address + "/deleteDue.php";

    public static String user_id = "";
    public static String username = "";

    public adapter_dues(@NonNull Context context, List<dataclass_dues>dataclass_duesList) {
        super(context, R.layout.data_item_due_record, dataclass_duesList);

        this.context = context;
        this.dataclassDues = dataclass_duesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item_due_record, null, true);

        TextView tvDueName = view.findViewById(R.id.tvDueName);
        TextView tvDueCost = view.findViewById(R.id.tvDueCost);
        TextView tvDueID = view.findViewById(R.id.tvDueID);
        TextView tvDueUserID = view.findViewById(R.id.tvDueUserID);
        ImageButton btnDeleteDue = view.findViewById(R.id.btnDeleteDue);

        tvDueUserID.setVisibility(View.INVISIBLE);
        tvDueID.setVisibility(View.INVISIBLE);

        tvDueName.setText(dataclassDues.get(position).getName());
        tvDueCost.setText(dataclassDues.get(position).getCost());
        tvDueUserID.setText(dataclassDues.get(position).getUser_id());
        tvDueID.setText(dataclassDues.get(position).getDue_id());

        String name = tvDueName.getText().toString();
        String due_id = tvDueID.getText().toString();
        user_id = tvDueUserID.getText().toString();
        getUsername();

        btnDeleteDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder deleteDue_builder = new AlertDialog.Builder(view.getRootView().getContext());
                View deleteDueView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.delete_due_dialog, null);

                deleteDue_builder.setTitle("Delete Due");

                TextView tvDeleteMessage = (TextView) deleteDueView.findViewById(R.id.tvDeleteMessage);

                tvDeleteMessage.setText("Are you sure you want to delete " + name + "?");

                deleteDue_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                deleteDue_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        StringRequest request = new StringRequest(Request.Method.POST, url_deleteDue, new Response.Listener<String>() {
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


                                param.put("due_id", due_id);


                                return param;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(request);

                        dialogInterface.dismiss();
                        Intent intent = new Intent(getContext(), activity_dues.class);
                        intent.putExtra("id", user_id);
                        intent.putExtra("username", username);
                        context.startActivity(intent);
                    }
                });
                deleteDue_builder.setView(deleteDueView);
                AlertDialog deleteDueDialog = deleteDue_builder.create();
                deleteDueDialog.show();
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
