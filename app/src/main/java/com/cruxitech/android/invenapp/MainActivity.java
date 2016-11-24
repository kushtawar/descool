package com.cruxitech.android.invenapp;

import android.app.AlertDialog;
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
    TextView txtLoginHeader;
    EditText ET_NAME,ET_PASS;
    String login_name,login_pass;
    Button btnLogin;
    AlertDialog.Builder builder = null;

     public static SharedPreferences gameSettings;
     public static SharedPreferences.Editor prefEditor;
public static String methodmain=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.colorbackground);
        session = new SessionManager(getApplicationContext());
        setContentView(com.cruxitech.android.invenapp.R.layout.activity_main);



        if (!(session.isUserLoggedIn())) {



            ET_NAME = (EditText) findViewById(com.cruxitech.android.invenapp.R.id.user_name);
            //    ET_NAME.getBackground().mutate().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            Typeface type = Typeface.createFromAsset(getAssets(), "fonts/font_algerian.ttf");
            txtLoginHeader = (TextView) findViewById(R.id.LoginHeader);

            txtLoginHeader.setTypeface(type);
            ET_NAME = (EditText) findViewById(com.cruxitech.android.invenapp.R.id.user_name);
            ET_PASS = (EditText) findViewById(com.cruxitech.android.invenapp.R.id.user_pass);
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
            Intent inte = new Intent(this, Landingpage.class);

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
    public void userLogin(View view) {
        session.cleareditor();

        login_name = ET_NAME.getText().toString();
        login_pass = ET_PASS.getText().toString();

        if (login_name.trim().length() > 0 && login_pass.trim().length() > 0) {


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


                                            Intent inte = new Intent(MainActivity.this, Landingpage.class);
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

                                    Intent inte = new Intent(MainActivity.this, Landingpage.class);
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



} //class
