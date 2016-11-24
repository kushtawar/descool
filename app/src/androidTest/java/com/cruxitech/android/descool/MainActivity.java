package com.cruxitech.android.descool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AsyncResponse {


    SessionManager session;
    static TextView txtLoginHeader,backtologin,userReg,forgotpass;
    static EditText ET_NAME,ET_PASS,ET_EMAIL;
    String login_name,login_pass,email;
    static Button btnLogin,btnSubmit;
    AlertDialog.Builder builder = null;

    private static boolean failflag=false;

     public static SharedPreferences gameSettings;
     public static SharedPreferences.Editor prefEditor;
public static String methodmain=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.colorbackground);
        session = new SessionManager(getApplicationContext());
        setContentView(com.cruxitech.android.descool.R.layout.activity_main);

        ET_EMAIL = (EditText) findViewById(R.id.new_user_email);
        btnSubmit= (Button)findViewById(R.id.btnSubmit);
        backtologin=(TextView)findViewById(R.id.backtologin);
        userReg=(TextView)findViewById(R.id.userReg);
        forgotpass=(TextView)findViewById(R.id.forgotPass);

        if (!(session.isUserLoggedIn())) {



            ET_NAME = (EditText) findViewById(com.cruxitech.android.descool.R.id.user_name);
            //    ET_NAME.getBackground().mutate().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            Typeface type = Typeface.createFromAsset(getAssets(), "fonts/font_algerian.ttf");
            txtLoginHeader = (TextView) findViewById(R.id.LoginHeader);

            txtLoginHeader.setTypeface(type);
            ET_NAME = (EditText) findViewById(com.cruxitech.android.descool.R.id.user_name);
            ET_PASS = (EditText) findViewById(com.cruxitech.android.descool.R.id.user_pass);
            btnLogin = (Button) findViewById(R.id.btnLogin);
            Drawable drawable_username = ET_NAME.getBackground(); // get current EditText drawable
            drawable_username.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP); // change the drawable color

            Drawable drawable_password = ET_PASS.getBackground(); // get current EditText drawable
            drawable_password.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP); // change the drawable color


            if (Build.VERSION.SDK_INT > 16) {
                ET_NAME.setBackground(drawable_username); // set the new drawable to EditText
                ET_PASS.setBackground(drawable_password); // set the new drawable to EditText
            } else {
                //noinspection deprecation
                ET_NAME.setBackgroundDrawable(drawable_username); // use setBackgroundDrawable because setBackground required API 16
                //noinspection deprecation
                ET_NAME.setBackgroundDrawable(drawable_password); // use setBackgroundDrawable because setBackground required API 16
            }

            ET_PASS.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        btnLogin.performClick();
                        return true;
                    }
                    return false;
                }
            });

        }
        else
        {
            Intent inte = new Intent(this, Home.class);

            startActivity(inte);
            finish();
        }

    } //End of Oncreate function

    @Override
    protected void onStart() {
        super.onStart();


    }

    public void userReg(View view)
    {
        startActivity(new Intent(this, Register.class));
        //finish();

    }

    public void forgotpassword(View view)
    {


        ET_EMAIL.setVisibility(View.VISIBLE);
        ET_EMAIL.setText(null);
        btnSubmit.setVisibility(View.VISIBLE);
        backtologin.setVisibility(View.VISIBLE);


        ET_NAME.setVisibility(View.GONE);
        ET_PASS.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);
        forgotpass.setVisibility(View.GONE);

    }

    public void setBacktologin(View view)
    {

setBacktologinview();
    }

    private void setBacktologinview() {

        ET_EMAIL.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
        backtologin.setVisibility(View.GONE);

        ET_NAME.setVisibility(View.VISIBLE);
        ET_PASS.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        forgotpass.setVisibility(View.VISIBLE);
    }


    public void userLogin(View view) {
        session.cleareditor();

        login_name = ET_NAME.getText().toString();
        login_pass = ET_PASS.getText().toString();




        if (login_name.trim().length() > 0 && login_pass.trim().length() > 0) {

            login_pass = CommonProcs.getpassword(ET_PASS.getText().toString());


             methodmain = "login";
            BackgroundTask backgroundTask = new BackgroundTask(MainActivity.this, new AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Log.e("invenapp:method:", new CommonProcs().getMethodname() + ":output:" + output);

                    if (output.equals(StatusConstants.statusLoginSuccessful)) {
                        try {

                            ArrayList<LoginBean> loginarray = BackgroundTask.login_orders;

                            if (loginarray.size()== 1) {
                                String uniqueuserid=loginarray.get(0).getUniqueuserid();
                              session.createUserLoginSession(loginarray.get(0).getUniqueuserid(), loginarray.get(0).getName(), loginarray.get(0).getUsername(), loginarray.get(0).getEmail(),loginarray.get(0).getActivestatus(),loginarray.get(0).getHashkey());


                                if (loginarray.get(0).getActivestatus().equals("1")) {

                                    methodmain = "getpermissions";
                                    BackgroundTask bgtaskpermissions = new BackgroundTask(MainActivity.this, new AsyncResponse() {
                                        @Override
                                        public void processFinish(String output) {
                                            Log.e("invenapp:output:", output);
                                            session.createUserRolesPermissions(BackgroundTask.m_list_permissions);


                                            Intent inte = new Intent(MainActivity.this, Home.class);
                                            inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(inte);
                                            finish();

                                        }
                                    });
                                    bgtaskpermissions.execute(methodmain, uniqueuserid);






                                }
                                else if(loginarray.get(0).getActivestatus().equals("0"))
                                {

                                    session.commiteditor();

                                    Intent inte = new Intent(MainActivity.this, Home.class);
                                    inte.setAction(Intent.ACTION_MAIN);
                                    inte.addCategory(Intent.CATEGORY_LAUNCHER);
                                        startActivity(inte);
                                        finish();

                                }



                            } else {


                            }

                        } catch (Exception ex) {
                            Log.e("invenapp:exception:",ex.getMessage());
                        }
                    }
                    else
                    {
                        builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Login failed..Username/Password is incorrect");
                        builder.setTitle("Login Failed");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ET_PASS.setText("");
                            }
                        });
                        builder.setCancelable(true);
                        builder.create().show();
                    }


                }


            });
            backgroundTask.execute(methodmain, login_name, login_pass);




        } else {
            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Username and Password must be filled");
            builder.setTitle("Login Failed");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ET_PASS.setText("");

                }
            });
            builder.setCancelable(true);
            builder.create().show();
        }


    }





        public void processFinish(String output) {
        Log.e("login2",output);
    }



    public void sendCoderesetpassword(final View view) {

        Validationrules validation=new Validationrules();

        failflag=false;

        if (validation.isEmailValid(ET_EMAIL)==false) {

            failflag=true;
        }


if (!failflag) {

    sendchangedpwd();


}

    }
    public void createpopupblank(Context ctx, String setmessage,String settitle){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(setmessage);

        builder.setTitle(Html.fromHtml("<font color='#D23927'>" + settitle + "</font>"));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                setBacktologinview();
            }
        });
        builder.setCancelable(true);
        builder.create().show();
    }

    private void sendchangedpwd() {

        String emailid = ET_EMAIL.getText().toString();

        methodmain = "sendforgotpassword";
        BackgroundTask bgtaskpermissions = new BackgroundTask(MainActivity.this, new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("invenapp:", "sendforgotpassword" + output);
if(output.equals(StatusConstants.statusforgotpasswordSuccessful)) {
    new MainActivity().createpopupblank(MainActivity.this, "A new password has been sent on your email.\n" +
            "Password can be changed at any time from Menu->Settings.", "Password Sent");
}
            }
        });


            bgtaskpermissions.execute(methodmain, emailid);



    }


} //class
