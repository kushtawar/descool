package com.cruxitech.android.descool;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class Landingpage extends BaseActivity {


    SessionManager session;

    Button btnAdminView=null;
    Button btnViewmydevices=null;
    Button btnAddNewDevice=null;
    Button btnViewAllDevices=null;
    TextView txtheaderaccountactivate,txtAccountStatus=null;
     LayoutParams layoutparams ;
    public static String methodlanding;
    String loggedinusername,loggedinuniqueuserid,loggedinname,loggedinuseremail=null;
    public static Landingpage landingpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);
        session=new SessionManager(this);
        landingpage=this;

        txtheaderaccountactivate=(TextView)findViewById(R.id.txtHeaderAccountActivate);
        txtAccountStatus=(TextView)findViewById(R.id.txtviewAccountStatus);
        btnAdminView=(Button)findViewById(R.id.btnAdminView);
        btnViewmydevices=(Button)findViewById(R.id.btnViewmydevices);
        btnAddNewDevice=(Button)findViewById(R.id.btnAddNewDevice);
        btnViewAllDevices=(Button)findViewById(R.id.btnViewAllDevices);

        layoutparams = (LayoutParams)btnViewmydevices.getLayoutParams();
        loggedinuniqueuserid=session.getUserDetails().get(SessionManager.KEY_UNIQUEUSERID);
        loggedinusername=session.getUserDetails().get(SessionManager.KEY_USERNAME);
        loggedinname=session.getUserDetails().get(SessionManager.KEY_NAME);
        loggedinuseremail=session.getUserDetails().get(SessionManager.KEY_EMAIL);

        ImageView userimage=(ImageView)findViewById(R.id.userimage);
        TextView toolbarusername=(TextView)findViewById(R.id.toolbarusername);

        toolbarusername.setVisibility(View.VISIBLE);
        userimage.setVisibility(View.VISIBLE);

        //setviewforpermissions();
        //setviewforaccountstatus();
    }


    @Override
    protected void onStart() {
        super.onStart();
        session=new SessionManager(this);
        setviewforpermissions();
        setviewforaccountstatus();
        loggedinusername=session.getUserDetails().get(SessionManager.KEY_USERNAME);

    }

    private void setviewforaccountstatus() {

        if(session.getUserDetails().get(SessionManager.KEY_ISACTIVE).equals("0")){
        btnAdminView.setVisibility(View.GONE);
        btnViewmydevices.setVisibility(View.GONE);
        btnAddNewDevice.setVisibility(View.GONE);
        btnViewAllDevices.setVisibility(View.GONE);
//        mToolbar.setVisibility(View.GONE);
            txtheaderaccountactivate.setVisibility(View.VISIBLE);
            txtAccountStatus.setVisibility(View.VISIBLE);
            txtheaderaccountactivate.setGravity(Gravity.CENTER_HORIZONTAL);
            txtAccountStatus.setGravity(Gravity.CENTER_HORIZONTAL);

        }
        else
        {
            txtheaderaccountactivate.setVisibility(View.GONE);
            txtAccountStatus.setVisibility(View.GONE);

        }

    }


    public void UserManagementView(View view)
    {
        startActivity(new Intent(this, UserManagementHome.class));


    }

    public void ViewMyDevices(View view)
    {
        startActivity(new Intent(this, ViewMyDevices.class));

    }

    public void AddNewDevice(View view)
    {
        startActivity(new Intent(this, AddDevice.class));

    }

    public void ViewAllDevices(View view)
    {
        startActivity(new Intent(this, ViewAllDevices.class));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        buttonback.setVisibility(View.GONE);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
      //  mToolbar.setTitle(Html.fromHtml("<font color='#FFAC6262'><i>" + "Home" + "</i></font>"));
        mTitle.setText(Html.fromHtml("<font><i>" + "Home" + "</i></font>"));

    }





    private void setviewforpermissions() {
        int totalvisible=4;
//ADMIN Button
        if ((session.isperm_ManageUsers()) || (session.isperm_AdminRequests())) {
            btnAdminView.setVisibility(View.VISIBLE);

        } else
        {
            btnAdminView.setVisibility(View.GONE);
            totalvisible--;
        }

//VIEW My Devices Button
        btnViewmydevices.setVisibility(View.VISIBLE);

//ADD New Device
        if ((session.isperm_AddDevice())) {
            btnAddNewDevice.setVisibility(View.VISIBLE);
        } else
        {
            btnAddNewDevice.setVisibility(View.GONE);
            totalvisible--;
        }

//View All
        if ((session.isperm_ViewAll())) {
            btnViewAllDevices.setVisibility(View.VISIBLE);
        } else
        {
            btnViewAllDevices.setVisibility(View.GONE);
            totalvisible--;
        }

        switch(totalvisible){
            case 3:
                layoutparams.setMargins(0,160,0,0);
                break;
            case 2:
                layoutparams.setMargins(0,220,0,0);
                break;
            case 1:
                layoutparams.setMargins(0,280,0,0);
                break;
        }

    }

    public void accountactive(final View view) {





        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(Landingpage.this);
        alert.setMessage("Enter the Code that you would have received on your registered email.");
        alert.setTitle("Activate Account");
        alert.setTitle(Html.fromHtml("<font color='#D23927'>" + "Activate Account" + "</font>"));

        edittext.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        alert.setView(edittext);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String code = edittext.getText().toString();

                String isactive = session.getUserDetails().get(SessionManager.KEY_ISACTIVE);
                String hash = session.getUserDetails().get(SessionManager.KEY_HASHKEY);
                methodbase = "activateaccount";
                BackgroundTask bgtaskpermissions = new BackgroundTask(Landingpage.this, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        Log.e("invenapp:", "activateaccountoutput" + output);
                        session.createUserLoginSession(loggedinuniqueuserid, loggedinusername, loggedinname, loggedinuseremail, "1", null);
                        session.commiteditor();
                        Log.e("invenapp:", "Landing:call refresh");
                        refreshpage();

                    }
                });

                if (hash.equals(code)) {
                    dialog.dismiss();
                    bgtaskpermissions.execute(methodbase, loggedinuniqueuserid, code);
                } else {

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Landingpage.this);
                    builder.setMessage("Codes do not match.Please retry.");

                    builder.setTitle(Html.fromHtml("<font color='#D23927'>" + "Error" + "</font>"));
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            accountactive(view);
                        }
                    });
                    builder.setCancelable(true);
                    builder.create().show();

                }


            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });


        alert.setCancelable(false);
        alert.show();


        //setviewforaccountstatus();



    }


    public void refreshpage() {


        methodbase = "getpermissions";
        BackgroundTask bgtaskpermissions = new BackgroundTask(Landingpage.this, new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("invenapp:output:", output);
                session.createUserRolesPermissions(BackgroundTask.m_list_permissions);


                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
               // Landingpage.this.recreate();


                setviewforpermissions();
            }
        });
        bgtaskpermissions.execute(methodbase, loggedinuniqueuserid);
    }

    @Override
    public void refresh() {
        this.refreshpage();

    }

    public void newhome(View view) {

        startActivity(new Intent(Landingpage.this, Home.class));
        // finish();
    }


}
