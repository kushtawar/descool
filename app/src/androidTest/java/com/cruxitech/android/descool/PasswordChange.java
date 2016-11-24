package com.cruxitech.android.descool;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class PasswordChange extends BaseActivity {
    EditText ET_USER_PASS,ET_USER_CONFIRM_PASS;
    Button btnchangePwd;
    SessionManager session;
    String user_pass,user_confirm_pass;
    private static boolean failflag=false;
    private static String passwordvalidationerror=null;
    AlertDialog.Builder builder=null;
    String loggedinusername,loggedinuniqueuserid,loggedinname,loggedinuseremail=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        session = new SessionManager(getApplicationContext());

        btnchangePwd=(Button)findViewById(R.id.btnchangePwd);
        ET_USER_PASS = (EditText)findViewById(R.id.user_pass);
        ET_USER_CONFIRM_PASS = (EditText)findViewById(R.id.user_confirmpass);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        buttonback.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);


            TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
            mTitle.setText(Html.fromHtml("<font><i>" + "Change Password" + "</i></font>"));



    }
    @Override
    protected void onStart() {
        super.onStart();

        loggedinuniqueuserid=session.getUserDetails().get(SessionManager.KEY_UNIQUEUSERID);
        loggedinuseremail=session.getUserDetails().get(SessionManager.KEY_EMAIL);
        loggedinusername=session.getUserDetails().get(SessionManager.KEY_USERNAME);
    }

    public void changePassword(View view)
    {

        user_pass =ET_USER_PASS.getText().toString();
        user_confirm_pass=ET_USER_CONFIRM_PASS.getText().toString();


        if (user_pass.equals(user_confirm_pass)) {

            this.checkformvalidation();
            if (failflag) {

            } else {
                String method = "sendchangepassword";
                BackgroundTask backgroundTask = new BackgroundTask(this, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        if (output.equals(StatusConstants.statuschangepasswordSuccessful)) {
                            Log.e("invenapp:", "change password" + output);



                            AlertDialog.Builder builder = new AlertDialog.Builder(PasswordChange.this);
                            builder.setMessage("Your password has been changed successfully");

                            builder.setTitle(Html.fromHtml("<font color='#D23927'>" + "Change Password Successful" + "</font>"));
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(PasswordChange.this, Home.class));
                                    finish();
                                }
                            });
                            builder.setCancelable(true);
                            builder.create().show();




                        }
                    }
                });
                backgroundTask.execute(method, loggedinuniqueuserid, user_pass,loggedinuseremail,loggedinusername);
                // finish();
            }
        }
        else
        {
            builder = new AlertDialog.Builder(PasswordChange.this);

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

        if (validation.isPasswordValid(ET_USER_PASS)==false) {
            passwordvalidationerror= Validationrules.passwordfailuremessage;
            failflag=true;
        }


    }

}