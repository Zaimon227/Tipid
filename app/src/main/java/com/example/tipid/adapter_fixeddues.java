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

public class adapter_fixeddues extends ArrayAdapter<dataclass_fixeddues> {
    Context context;
    List<dataclass_fixeddues> dataclassFixedDues;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    String url_getUsername = "" + address + "/getUsername.php";
    String url_deleteFixedDue = "" + address + "/deleteFixedDue.php";

    public static String user_id = "";
    public static String username = "";

    public adapter_fixeddues(@NonNull Context context, List<dataclass_fixeddues>dataclass_fixedduesList) {
        super(context, R.layout.data_item_fixeddue_record, dataclass_fixedduesList);

        this.context = context;
        this.dataclassFixedDues = dataclass_fixedduesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item_fixeddue_record, null, true);

        TextView tvFixedDueName = view.findViewById(R.id.tvFixedDueName);
        TextView tvFixedDueCost = view.findViewById(R.id.tvFixedDueCost);
        TextView tvFixedDueID = view.findViewById(R.id.tvFixedDueID);
        TextView tvFixedDueUserID = view.findViewById(R.id.tvFixedDueUserID);
        ImageButton btnDeleteFixedDue = view.findViewById(R.id.btnDeleteFixedDue);

        tvFixedDueUserID.setVisibility(View.INVISIBLE);
        tvFixedDueID.setVisibility(View.INVISIBLE);

        tvFixedDueName.setText(dataclassFixedDues.get(position).getName());
        tvFixedDueCost.setText(dataclassFixedDues.get(position).getCost());
        tvFixedDueUserID.setText(dataclassFixedDues.get(position).getUser_id());
        tvFixedDueID.setText(dataclassFixedDues.get(position).getFixeddue_id());

        String name = tvFixedDueName.getText().toString();
        String fixeddue_id = tvFixedDueID.getText().toString();
        user_id = tvFixedDueUserID.getText().toString();
        getUsername();

        btnDeleteFixedDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder deleteFixedDue_builder = new AlertDialog.Builder(view.getRootView().getContext());
                View deleteFixedDueView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.delete_message_dialog, null);

                deleteFixedDue_builder.setTitle("Delete Fixed Due");

                TextView tvDeleteMessage = (TextView) deleteFixedDueView.findViewById(R.id.tvDeleteMessage);

                tvDeleteMessage.setText("Are you sure you want to delete " + name + "?");

                deleteFixedDue_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                deleteFixedDue_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        StringRequest request = new StringRequest(Request.Method.POST, url_deleteFixedDue, new Response.Listener<String>() {
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


                                param.put("fixeddue_id", fixeddue_id);


                                return param;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(request);

                        dialogInterface.dismiss();
                        Intent intent = new Intent(getContext(), activity_fixeddues.class);
                        intent.putExtra("id", user_id);
                        intent.putExtra("username", username);
                        context.startActivity(intent);
                    }
                });
                deleteFixedDue_builder.setView(deleteFixedDueView);
                AlertDialog deleteFixedDueDialog = deleteFixedDue_builder.create();
                deleteFixedDueDialog.show();
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
