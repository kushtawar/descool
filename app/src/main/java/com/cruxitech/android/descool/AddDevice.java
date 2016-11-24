package com.cruxitech.android.descool;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddDevice extends BaseActivity implements AsyncResponse  {

    Spinner DeviceType,DeviceLocation,DeviceManufacturer;
            EditText DeviceNo,DeviceOwner,DeviceModel;
    String devtype,devno,devowner,devlocation,devmanufacturer,devmodel;
    Button btnAddDevice,btnViewDevices;
private static boolean failflag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("failflag", String.valueOf(failflag));
        setContentView(com.cruxitech.android.descool.R.layout.activity_add_device);
        DeviceType = (Spinner)findViewById(com.cruxitech.android.descool.R.id.spinnerDeviceType);
        DeviceNo = (EditText)findViewById(com.cruxitech.android.descool.R.id.editTextDeviceNo);
        DeviceOwner = (EditText)findViewById(com.cruxitech.android.descool.R.id.editTextOwner);
        DeviceLocation = (Spinner)findViewById(com.cruxitech.android.descool.R.id.spinnerDeviceLocation);
        DeviceManufacturer = (Spinner)findViewById(com.cruxitech.android.descool.R.id.spinnerDeviceManufacturer);
        DeviceModel = (EditText)findViewById(com.cruxitech.android.descool.R.id.editTextDeviceModel);

        btnAddDevice = (Button)findViewById(com.cruxitech.android.descool.R.id.buttonAdd);





        DeviceModel.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnAddDevice.performClick();
                    return true;
                }
                return false;
            }
        });




    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(AddDevice.this, MainActivity.class));
        finish();

    }

    public void methodaddDevice(View view) {


this.checkformvalidation();




if(!failflag) {

    devtype = DeviceType.getSelectedItem().toString();
    devno = DeviceNo.getText().toString();
    devowner = DeviceOwner.getText().toString();
    devlocation = DeviceLocation.getSelectedItem().toString();
    devmanufacturer = DeviceManufacturer.getSelectedItem().toString();
    devmodel = DeviceModel.getText().toString();


    Log.d("cruxapp", "devtype:" + devtype);
    String method = "adddevice";
    BackgroundTask backgroundTask = new BackgroundTask(this, new AsyncResponse() {
        @Override
        public void processFinish(String output) {
            Log.e("adddevice", output);

            if (output.equals(StatusConstants.statusAddDeviceSuccessful)) {
                Intent inte = new Intent(AddDevice.this, AddDevice.class);
                inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(inte);
            }



        }
    });
    backgroundTask.execute(method, devtype, devlocation, devmanufacturer, devno, devowner, devmodel);



}
    }

    

    public void methodViewAllDevices(View view)
    {

        try {
            startActivity(new Intent(this, ViewAllDevices.class));
        }
        catch(Exception ex)
        {
            Log.d("cruxapp", "Into ViewAllDevices"+ex.getMessage());
        }
    }


    public void processFinish(String output){
        Log.e("addevice2",output);
    }

    private void  checkformvalidation() {

        Validationrules validation=new Validationrules();

        failflag=false;
        if (validation.isnullandhasText_Spinner(DeviceType)) {
            failflag=true;
        }

        if (validation.isnullandhasText_Spinner(DeviceLocation)) {
            failflag=true;
        }
        if (validation.isnullandhasText_Spinner(DeviceManufacturer)) {
            failflag=true;
        }

        if (validation.isNullorhasNotext_EditText(DeviceModel)) {
            // Toast.makeText(AddDevice.this, "Form contains error", Toast.LENGTH_LONG).show();
            failflag=true;
        }
        if (validation.isNullorhasNotext_EditText(DeviceOwner)) {
            failflag=true;
        }

        if (validation.isNullorhasNotext_EditText(DeviceNo)) {
            failflag=true;
        }


    }



}


/*
Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        */
/**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * *//*

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);*/
