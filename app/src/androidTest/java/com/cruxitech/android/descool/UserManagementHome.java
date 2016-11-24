package com.cruxitech.android.descool;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class UserManagementHome extends BaseActivity {


    AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management_home);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(Html.fromHtml("<font><i>" + "Admin" + "</i></font>"));
    }
    public void userReg(View view)
    {

        startActivity(new Intent(this, Register.class));
    }

    public void pendingRequests(View view) {

        builder = new AlertDialog.Builder(UserManagementHome.this);
        builder.setMessage("This feature will be enabled in the NEXT version.\nThis feature will enable the ADMIN to have a consolidated view of all such devices that are either currently UNASSIGNED or are assigned to Others");
        builder.setTitle("Feature NOT yet implemented !!");
        builder.setTitle(Html.fromHtml("<font color='#FF7F27'>Feature NOT yet implemented !!</font>"));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(true);
        builder.create().show();
    }


    public void manageuserroles(View view) {

        startActivity(new Intent(UserManagementHome.this, RolesPermissions.class));
       // finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, Home.class));
    }

    public void newhome(View view) {

        startActivity(new Intent(UserManagementHome.this, Home.class));
        // finish();
    }



}
