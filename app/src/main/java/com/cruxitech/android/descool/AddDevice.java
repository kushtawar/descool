package com.cruxitech.android.descool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddDevice extends BaseActivity implements AsyncResponse  {

    Spinner DeviceType,DeviceLocation,DeviceManufacturer,DeviceOwner,DeviceCluster;
            EditText DeviceNo,DeviceModel;
    String devtype,devno,devowner,devlocation,devmanufacturer,devmodel,devcluster;
    Button btnAddDevice,btnViewDevices;
private static boolean failflag=false;

    int selecteduserid=0;
    public static boolean flagitemsupdate=false;
    String selectedusername="";


    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("failflag", String.valueOf(failflag));
        setContentView(R.layout.activity_add_device);
        DeviceType = (Spinner)findViewById(R.id.spinnerDeviceType);
        DeviceNo = (EditText)findViewById(R.id.editTextDeviceNo);
        DeviceOwner = (Spinner)findViewById(R.id.spinnerTextOwner);
        DeviceLocation = (Spinner)findViewById(R.id.spinnerDeviceLocation);
        DeviceManufacturer = (Spinner)findViewById(R.id.spinnerDeviceManufacturer);
        DeviceModel = (EditText)findViewById(R.id.editTextDeviceModel);
        DeviceCluster = (Spinner)findViewById(R.id.spinnerDeviceCluster);
        btnAddDevice = (Button)findViewById(R.id.buttonAdd);



        this.getspinnerlistfromdatabase(getApplicationContext());

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(Html.fromHtml("<font><i>" + "Add Device" + "</i></font>"));
    }

    @Override
    public void onStart(){
        super.onStart();
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
    devowner = DeviceOwner.getSelectedItem().toString();
    devlocation = DeviceLocation.getSelectedItem().toString();
    devmanufacturer = DeviceManufacturer.getSelectedItem().toString();
    devmodel = DeviceModel.getText().toString();
    devcluster = DeviceCluster.getSelectedItem().toString();


    Log.d("cruxapp", "devtype:" + devtype);
    String method = "adddevice";
    BackgroundTask backgroundTask = new BackgroundTask(this, new AsyncResponse() {
        @Override
        public void processFinish(String output) {
            Log.e("adddevice", output);

            if (output.equals(StatusConstants.statusAddDeviceSuccessful)) {

               /* Intent inte = new Intent(AddDevice.this, AddDevice.class);
                inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(inte);*/

                Toast.makeText(AddDevice.this, "Device Added successfully", Toast.LENGTH_LONG).show();

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }else
            {
                Toast.makeText(AddDevice.this, "Device could not be added. "+output, Toast.LENGTH_LONG).show();
            }



        }
    });
    backgroundTask.execute(method, devtype, devlocation, devmanufacturer, devno, devowner, devmodel,devcluster);



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

    public void  checkformvalidation() {

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
        if (validation.isnullandhasText_Spinner(DeviceOwner)) {
            failflag=true;
        }

        if (validation.isNullorhasNotext_EditText(DeviceNo)) {
            failflag=true;
        }


        //Selected Value
       /* if (validation.isSelectedValueProper(DeviceOwner)) {
            failflag=true;
        }*/



    }
    public void getspinnerlistfromdatabase(final Context ctx) {
        String method = "updatelist";
        String fieldtobeupdated = "users";

        //   BackgroundTask.m_list=null;

        BackgroundTask backgroundTask = new BackgroundTask(ctx, new AsyncResponse() {

            @Override
            public void processFinish(String output) {
                Log.e("invenapp:method:", new CommonProcs().getMethodname() + ":output:" + output);
                if (output.equals(StatusConstants.listretrievalSuccessful)) {

                    listItems = BackgroundTask.m_list_users;

                    listItems.add(0,StatusConstants.selectiontext);

                    ArrayAdapter NoCoreAdapter = new ArrayAdapter(ctx,
                            R.layout.spinner_layout, listItems);

                    DeviceOwner.setAdapter(NoCoreAdapter);
                    DeviceOwner.setSelection(listItems.indexOf(StatusConstants.selectiontext));


                    DeviceOwner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                   int arg2, long arg3) {
                            // TODO Auto-generated method stub


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });




                    AddDevice.flagitemsupdate=true;

                    Log.e("invenapp:getlist:users", output);
                }

            }
        });
        backgroundTask.execute(method, fieldtobeupdated);
    }


}

