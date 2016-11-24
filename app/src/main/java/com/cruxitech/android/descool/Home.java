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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends BaseActivity {

    SessionManager session;
    TextView txtheaderaccountactivate,txtAccountStatus=null;
    public static Home homepage;


    ListView homelist=null;


    ArrayList<Integer> homecatimg=new ArrayList<>();
    ArrayList<String> homecatname=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        session=new SessionManager(this);
        homepage=this;

        txtheaderaccountactivate=(TextView)findViewById(R.id.txtHeaderAccountActivate);
        txtAccountStatus=(TextView)findViewById(R.id.txtviewAccountStatus);


        loggedinuniqueuserid=session.getUserDetails().get(SessionManager.KEY_UNIQUEUSERID);
        loggedinusername=session.getUserDetails().get(SessionManager.KEY_USERNAME);
        loggedinname=session.getUserDetails().get(SessionManager.KEY_NAME);
        loggedinuseremail=session.getUserDetails().get(SessionManager.KEY_EMAIL);

        ImageView userimage=(ImageView)findViewById(R.id.userimage);
        TextView toolbarusername=(TextView)findViewById(R.id.toolbarusername);

        toolbarusername.setVisibility(View.VISIBLE);
        userimage.setVisibility(View.VISIBLE);

        homelist = (ListView)findViewById(R.id.listhomescreen);

        this.setviewforpermissions();

        HomeAdapter homeadap=new HomeAdapter(this,homecatimg,homecatname);
        buttonback=(ImageView)findViewById(R.id.imagebuttonback);
        buttonback.setVisibility(View.GONE);
        homelist.setAdapter(homeadap);

        homelist.setOnFocusChangeListener(new AdapterView.OnFocusChangeListener() {

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

     //   homelist.getChildAt(1).setVisibility(View.GONE);



    }
});

        homelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                switch (homecatname.get(position)){

                    case StatusConstants.homeadmin:
                        startActivity(new Intent(Home.this, UserManagementHome.class));
                        break;
                    case StatusConstants.homeviewmy:
                        startActivity(new Intent(Home.this, ViewMyDevices.class));
                        break;
                    case StatusConstants.homeviewall:
                        startActivity(new Intent(Home.this, ViewAllDevices.class));
                        break;
                    case StatusConstants.homeaddnew:
                        startActivity(new Intent(Home.this, AddDevice.class));
                        break;

                    default:
                        break;
                }

            }
        });
////

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(Html.fromHtml("<font><i>" + "Home" + "</i></font>"));
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

            homelist.setVisibility(View.GONE);

            txtheaderaccountactivate.setVisibility(View.VISIBLE);

            txtAccountStatus.setVisibility(View.VISIBLE);
            txtAccountStatus.setText(Html.fromHtml("<u>" + "Activate Account" + "</u>"));
            txtheaderaccountactivate.setGravity(Gravity.CENTER_HORIZONTAL);
            txtAccountStatus.setGravity(Gravity.CENTER_HORIZONTAL);

        }
        else
        {
            txtheaderaccountactivate.setVisibility(View.GONE);
            txtAccountStatus.setVisibility(View.GONE);

        }

    }

    private void setviewforpermissions() {

        if ((session.isperm_ManageUsers()) || (session.isperm_AdminRequests())) {
            if (!homecatimg.contains(R.drawable.admin_2)) {
                homecatimg.add(R.drawable.admin_2);
            }
            if (!homecatname.contains(StatusConstants.homeadmin)) {
                homecatname.add(StatusConstants.homeadmin);
            }
        }

        if (!homecatimg.contains(R.drawable.viewmy_1)) {
            homecatimg.add(R.drawable.viewmy_1);
        }
        if (!homecatname.contains(StatusConstants.homeviewmy)) {
            homecatname.add(StatusConstants.homeviewmy);
        }

        if (session.isperm_AddDevice()) {
            if (!homecatimg.contains(R.drawable.addsymbol)) {
                homecatimg.add(R.drawable.addsymbol);
            }
            if (!homecatname.contains(StatusConstants.homeaddnew)) {
                homecatname.add(StatusConstants.homeaddnew);
            }
        }



        if (session.isperm_ViewAll()) {

            if (!homecatimg.contains(R.drawable.viewall)) {
                homecatimg.add(R.drawable.viewall);
            }
            if (!homecatname.contains(StatusConstants.homeviewall)) {
                homecatname.add(StatusConstants.homeviewall);
            }

        }





    }

    public void accountactive(final View view) {





        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(Home.this);
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
                BackgroundTask bgtaskpermissions = new BackgroundTask(Home.this, new AsyncResponse() {
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

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Home.this);
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
    BackgroundTask bgtaskpermissions = new BackgroundTask(Home.this, new AsyncResponse() {
        @Override
        public void processFinish(String output) {
            try {
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
            catch (Exception e) {
                Log.e("log_inventory", e.getMessage());
                Toast.makeText(Home.this,"There was some problem during connection. Please check your network connectivity or contact administrator",Toast.LENGTH_LONG);
                e.printStackTrace();
            }

        }
    });
    bgtaskpermissions.execute(methodbase, loggedinuniqueuserid);

    }

    @Override
    public void refresh() {
        this.refreshpage();

    }



    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem refresh = menu.findItem(R.id.refresh);
        if (session.getUserDetails().get(SessionManager.KEY_ISACTIVE).equals("0"))
            {
                refresh.setVisible(false);
            }
            else
            {
                refresh.setVisible(true);
            }
            return true;

    }

}
