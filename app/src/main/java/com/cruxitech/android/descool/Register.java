package com.cruxitech.android.descool;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class Register extends AppCompatActivity implements AsyncResponse{
    EditText ET_NAME,ET_USER_NAME,ET_USER_PASS,ET_USER_CONFIRM_PASS,ET_USER_EMAIL;
    String name,user_name,user_pass,user_confirm_pass,user_email;
    private static boolean failflag=false;
    private static String passwordvalidationerror=null;
    AlertDialog.Builder builder=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.cruxitech.android.descool.R.layout.activity_register);
        ET_NAME = (EditText)findViewById(com.cruxitech.android.descool.R.id.name);
        ET_USER_NAME= (EditText)findViewById(com.cruxitech.android.descool.R.id.new_user_name);
        ET_USER_PASS = (EditText)findViewById(com.cruxitech.android.descool.R.id.new_user_pass);
        ET_USER_CONFIRM_PASS = (EditText)findViewById(R.id.new_user_confirmpass);
        ET_USER_CONFIRM_PASS = (EditText)findViewById(R.id.new_user_confirmpass);
        ET_USER_EMAIL = (EditText)findViewById(R.id.new_user_email);
    }
    public void userReg(View view)
    {

        name = ET_NAME.getText().toString();
        user_name = ET_USER_NAME.getText().toString();
        user_pass =ET_USER_PASS.getText().toString();
        user_confirm_pass=ET_USER_CONFIRM_PASS.getText().toString();
        user_email=ET_USER_EMAIL.getText().toString();

        if (user_pass.equals(user_confirm_pass)) {

            this.checkformvalidation();
            if (failflag) {

            } else {
                String method = "registration";
                BackgroundTask backgroundTask = new BackgroundTask(this, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        if (output.equals(StatusConstants.statusRegistrationSuccessful)) {
                            Log.e("invenapp:", "registerin Register" + output);



                            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                            builder.setMessage("Please login now and use the unique 6 digit code sent on your registered email to activate your account");

                            builder.setTitle(Html.fromHtml("<font color='#D23927'>" + "Registration Successful" + "</font>"));
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Register.this, MainActivity.class));
                                    finish();
                                }
                            });
                            builder.setCancelable(true);
                            builder.create().show();




                        }
                    }
                });
                backgroundTask.execute(method, name, user_name, user_pass, user_email);
               // finish();
            }
        }
        else
        {
            builder = new AlertDialog.Builder(Register.this);
            builder = new AlertDialog.Builder(Register.this);
            builder.setMessage("Password & Confirm Password texts do not match!!");
            builder.setTitle("Password Mismatch");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ET_USER_PASS.setText("");
                    ET_USER_CONFIRM_PASS.setText("");

                }
            });
            builder.setCancelable(true);
            builder.create().show();
        }



    }
    public void processFinish(String output) {
        Log.e("register2", output);
    }


    private void checkformvalidation() {

        Validationrules validation=new Validationrules();

        failflag=false;
        if (validation.isNullorhasNotext_EditText(ET_NAME)==true) {
            // Toast.makeText(AddDevice.this, "Form contains error", Toast.LENGTH_LONG).show();
            failflag=true;
        }
        if (validation.isNullorhasNotext_EditText(ET_USER_NAME)==true) {
            // Toast.makeText(AddDevice.this, "Form contains error", Toast.LENGTH_LONG).show();
            failflag=true;
        }
        if (validation.isPasswordValid(ET_USER_PASS)==false) {
            passwordvalidationerror=Validationrules.passwordfailuremessage;
            failflag=true;
        }
        if (validation.isEmailValid(ET_USER_EMAIL)==false) {

            failflag=true;
        }

    }

}