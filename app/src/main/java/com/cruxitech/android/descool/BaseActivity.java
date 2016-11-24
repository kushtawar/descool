package com.cruxitech.android.descool;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public  class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar;
    SessionManager session;
    AlertDialog.Builder builder = null;
public static String methodbase;
    String loggedinusername,loggedinuniqueuserid,loggedinname,loggedinuseremail=null;
     TextView toolbarusername=null;
  protected ImageView buttondelete,buttonback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());
        loggedinuniqueuserid=session.getUserDetails().get(SessionManager.KEY_UNIQUEUSERID);
        loggedinusername=session.getUserDetails().get(SessionManager.KEY_USERNAME);
        loggedinname=session.getUserDetails().get(SessionManager.KEY_NAME);
        loggedinuseremail=session.getUserDetails().get(SessionManager.KEY_EMAIL);



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
         mToolbar = (Toolbar) findViewById(R.id.toolbar);
        buttonback=(ImageView)findViewById(R.id.imagebuttonback);
        buttondelete=(ImageView)findViewById(R.id.deleteiconbase);
        toolbarusername=(TextView)findViewById(R.id.toolbarusername);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");
        mToolbar.showOverflowMenu();
        setSupportActionBar(mToolbar);
        toolbarusername.setText(loggedinusername);
      //  buttondelete.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu

        MenuItem menusearch = menu.findItem(R.id.menu_search);
        menusearch.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {



        case R.id.menu_search:

        this.callsearchfunction();
          return true;


            case R.id.refresh:

                this.refresh();
                return true;
            case R.id.logout:

               logout();

                return true;

            case R.id.usersettings:

               changePassword();

                return true;

            case R.id.about:
                showAbout();
                return true;
            /*case R.id.help:
                Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void callsearchfunction() {


    }

    public void changePassword() {

        Intent passwordchangeactivity = new Intent(BaseActivity.this, PasswordChange.class);
        startActivity(passwordchangeactivity);

    }

    protected void logout() {
        session.logoutUser();
        Home.homepage.finish();

        finish();
    }

    public void refresh() {
        Toast.makeText(getApplicationContext(), "Refreshing..", Toast.LENGTH_LONG).show();

    }


    public void searchitems(){

    }


    private void showAbout()  {


            String versionName = "";
            PackageInfo packageInfo=null;

                try {
                    packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                versionName = "version:" + packageInfo.versionName;






        builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setMessage("Developed as part of TCS Telecom EU Initiative");

        builder.setTitle(Html.fromHtml("<font color='#FF7F27'>"+versionName+"</font>"));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(true);
        builder.create().show();

    }
    public void gotopreviousscreen (View view) {
        super.onBackPressed();
        finish();
    }
    public void showuserdetails(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        String user="\n\nUser:"+loggedinusername+"\n"+"\n"+"Assigned roles:"+'\n'+'\n';

        alertDialogBuilder.setMessage(SessionManager.rolelist + '\n' + '\n');

        alertDialogBuilder.setTitle(loggedinusername);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }


}
