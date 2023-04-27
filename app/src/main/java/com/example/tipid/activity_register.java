package com.example.tipid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class activity_register extends AppCompatActivity {

    EditText edtRegisterUsername, edtRegisterPassword, edtRegisterConfirmRegisterPassword, edtRegisterEmail, edtRegisterMonthlyEarnings;
    ImageButton btnCloseRegister, btnSubmitRegister, btnPrivacyDocument;
    CheckBox chkbxAgreement;
    MaterialAlertDialogBuilder materialAlertDialogBuilder;

    public static String address = "http://192.168.25.251/Tipid";
    //public static String address = "http://tipid.site/mobile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtRegisterUsername = (EditText) findViewById(R.id.edtRegisterUsername);
        edtRegisterPassword = (EditText) findViewById(R.id.edtRegisterPassword);
        edtRegisterConfirmRegisterPassword = (EditText) findViewById(R.id.edtRegisterConfirmPassword);
        edtRegisterEmail = (EditText) findViewById(R.id.edtRegisterEmail);
        edtRegisterMonthlyEarnings = (EditText) findViewById(R.id.edtRegisterMonthlyEarnings);

        btnCloseRegister = (ImageButton) findViewById(R.id.btnCloseRegister);
        btnSubmitRegister = (ImageButton) findViewById(R.id.btnSubmitRegister);
        btnPrivacyDocument = (ImageButton) findViewById(R.id.btnPrivacyDocument);

        chkbxAgreement = (CheckBox) findViewById(R.id.chkbxAgreement);

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);

        btnCloseRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        chkbxAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    materialAlertDialogBuilder.setTitle("Terms of Service");
                    materialAlertDialogBuilder.setMessage("By clicking agree below, you permit the use of analytics on your data for the purpose of improving the website and better provide financial assistance");
                    materialAlertDialogBuilder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    materialAlertDialogBuilder.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            chkbxAgreement.setChecked(false);
                        }
                    });

                    materialAlertDialogBuilder.show();
                }
            }
        });

        btnPrivacyDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_register.this, activity_privacypolicy.class);
                startActivity(intent);
            }
        });

        btnSubmitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username, password, confirm_password, email, monthly_earnings;
                username = String.valueOf(edtRegisterUsername.getText());
                password = String.valueOf(edtRegisterPassword.getText());
                confirm_password = String.valueOf(edtRegisterConfirmRegisterPassword.getText());
                email = String.valueOf(edtRegisterEmail.getText());
                monthly_earnings = String.valueOf(edtRegisterMonthlyEarnings.getText());

                if(!username.equals("") && !password.equals("") && !email.equals("") && !monthly_earnings.equals("")) {
                    if(confirm_password.equals(password)) {
                        if(chkbxAgreement.isChecked()) {
                            if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                //Start ProgressBar first (Set visibility VISIBLE)
                                Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Starting Write and Read data with URL
                                        //Creating array for parameters
                                        String[] field = new String[4];
                                        field[0] = "username";
                                        field[1] = "password";
                                        field[2] = "email";
                                        field[3] = "monthly_earnings";
                                        //Creating array for data
                                        String[] data = new String[4];
                                        data[0] = username;
                                        data[1] = password;
                                        data[2] = email;
                                        data[3] = monthly_earnings;
                                        PutData putData = new PutData("" + address + "/signup.php", "POST", field, data);
                                        if (putData.startPut()) {
                                            if (putData.onComplete()) {
                                                String result = putData.getResult();
                                                if(result.equals("Registration Success")) {
                                                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), activity_login.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else {
                                                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Agree to Terms of Service", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Confirm Password does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please answer all required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}