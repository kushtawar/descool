package com.cruxitech.android.descool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ViewDevice extends BaseActivity {

static boolean makeditable=true;
    SessionManager session;
    Spinner DeviceType,DeviceLocation,DeviceManufacturer,DeviceOwner,DeviceCluster;
    ImageView imgdevtype;
    TextView lastupdatedbyon,Devlastupdatedbyandon;
    EditText DeviceNo,DeviceModel,Devlastupdatedby;
    String devtype,devno,devowner,devlocation,devmanufacturer,devmodel,devcluster,lastupdatedby,lastupdatedon,callingclass;
    Button btnEditDevice,btnDeleteDevice,btnSaveDevice;
private  String devuniqueid,devicetype,devicelocation,devicemanufacturer,deviceno,deviceowner,devicemodel;
    private static boolean failflag=false;
    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());


        setContentView(com.cruxitech.android.descool.R.layout.activity_view_device);

        if (session.isUserLoggedIn()) {

            DeviceType = (Spinner) findViewById(com.cruxitech.android.descool.R.id.spinnerDeviceType);
            DeviceLocation = (Spinner) findViewById(com.cruxitech.android.descool.R.id.spinnerDeviceLocation);
            DeviceManufacturer = (Spinner) findViewById(com.cruxitech.android.descool.R.id.spinnerDeviceManufacturer);
            DeviceNo = (EditText) findViewById(com.cruxitech.android.descool.R.id.editTextDeviceNo);
            DeviceOwner = (Spinner) findViewById(R.id.spinnerTextOwner);
            DeviceModel = (EditText) findViewById(com.cruxitech.android.descool.R.id.editTextDeviceModel);
            DeviceCluster = (Spinner)findViewById(R.id.spinnerDeviceCluster);
            Devlastupdatedbyandon = (TextView) findViewById(R.id.lastupdatedbyandon);
            imgdevtype=(ImageView)findViewById(R.id.icondevtype);

            btnEditDevice = (Button) findViewById(R.id.buttonEdit);
            btnSaveDevice = (Button) findViewById(R.id.buttonSave);

            btnEditDevice.setTextColor(Color.parseColor("#FF9E9B9B"));
            btnEditDevice.setEnabled(false);

            this.getspinnerlistfromdatabase(getApplicationContext());


            String[] devicetypeArray = getResources().getStringArray(R.array.devicetype_arrays);
            String[] devicelocationArray = getResources().getStringArray(R.array.devicelocation_arrays);
            String[] devicemanufacturerArray = getResources().getStringArray(R.array.devicetype_arrays);

            Bundle bundle = getIntent().getExtras();
            devuniqueid = bundle.getString("key_devuniqueid");
            devicetype = bundle.getString("key_Devtype");
            devicelocation = bundle.getString("key_Devlocation");
            deviceno = bundle.getString("key_Devno");
            deviceowner = bundle.getString("key_Devowner");
            devicemodel = bundle.getString("key_Devmodel");
            devcluster=bundle.getString("key_cluster");
            devicemanufacturer = bundle.getString("key_Devmanufacturer");
            lastupdatedby = bundle.getString("key_Devlastupdatedby");
            lastupdatedon = bundle.getString("key_Devlastupdatedon");
            callingclass=bundle.getString("callingclass");
            Devlastupdatedbyandon.setText("Lastupdated by: " + lastupdatedby + " on: " + lastupdatedon + "IST");

            this.setdefaultvalues();


        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Session Expired: Moving to Login Page" , Toast.LENGTH_LONG).show();
            session.checkLogin();
        }



    }

    @Override
    public void onStart(){
        super.onStart();


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        this.setviewforpermissions();

    }
    @Override
    public void gotopreviousscreen (View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        switch(callingclass.toLowerCase()) {

            case "viewmydevices":

            startActivity(new Intent(ViewDevice.this, ViewMyDevices.class));
                finish();
                break;
            case "viewalldevices":

                startActivity(new Intent(ViewDevice.this, ViewAllDevices.class));
                finish();
                break;
            default:
                startActivity(new Intent(ViewDevice.this, Home.class));
                finish();
                break;

        }
        finish();

    }

    public void methodMakeFieldsEditable(View V)
    {
        this.makefieldseditable(true);
        while((TextView) DeviceOwner.getSelectedView()==null){

        }
        ((TextView) DeviceOwner.getSelectedView()).setTextColor(Color.parseColor("#000000"));
        btnEditDevice.setVisibility(View.GONE);
        btnSaveDevice.setVisibility(View.VISIBLE);
    }




    public void methodSaveDevice(View view)
    {
        this.checkformvalidation();

        if(!failflag) {


            AlertDialog.Builder builder = new AlertDialog.Builder(ViewDevice.this);

            builder.setTitle("Confirm");
            builder.setMessage("Are you sure you want to save the changes ?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();


                    devtype = DeviceType.getSelectedItem().toString();
                    devlocation = DeviceLocation.getSelectedItem().toString();
                    devmanufacturer = DeviceManufacturer.getSelectedItem().toString();
                    devowner = DeviceOwner.getSelectedItem().toString();
                    devcluster=DeviceCluster.getSelectedItem().toString();
                    devno = DeviceNo.getText().toString();
                    devmodel = DeviceModel.getText().toString();



                    Log.d("cruxapp", "devtype:" + devtype);
                    String method = "editdevice";
                    BackgroundTask backgroundTask = new BackgroundTask(ViewDevice.this, new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            Log.e("editdevice", output);
                            if (output.equals(StatusConstants.statusEditviewDeviceSuccessful)) {
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ViewDevice.this);
                                dlgAlert.setMessage("Device Detail has been updated successfully");
                                dlgAlert.setTitle("Edit Device");
                                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        ((TextView) DeviceOwner.getSelectedView()).setTextColor(Color.parseColor("#FF9E9B9B"));
                                        DeviceNo.setEnabled(false);
                                        DeviceType.setEnabled(false);
                                        DeviceLocation.setEnabled(false);
                                        DeviceManufacturer.setEnabled(false);
                                        DeviceCluster.setEnabled(false);
                                        DeviceOwner.setEnabled(false);
                                        DeviceModel.setEnabled(false);
                                        btnSaveDevice.setVisibility(View.GONE);
                                        btnEditDevice.setVisibility(View.VISIBLE);
                                    }
                                });


                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();

                                Devlastupdatedbyandon.setText("Lastupdated by: " + session.getUserDetails().get(SessionManager.KEY_USERNAME) + " on: " + new CommonProcs().getCurrentTimeStamp());


                            } else {
                                Toast.makeText(ViewDevice.this, "Device could not be updated. " + output, Toast.LENGTH_LONG).show();
                            }


                        }
                    });
                    backgroundTask.execute(method, devtype, devlocation, devmanufacturer, devno, devowner, devmodel, devuniqueid, devcluster);
                }

            });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // setdefaultvalues();
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                }








    }




    public void methodDeleteDevice(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewDevice.this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete this record ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String method = "deletedevice";
                BackgroundTask backgroundTask = new BackgroundTask(ViewDevice.this, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        Log.e("invenapp:method:",new CommonProcs().getMethodname()+output);
                        if (output.equals(StatusConstants.statusDeleteOneDeviceSuccessful)) {

                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ViewDevice.this);
                            dlgAlert.setMessage("Device has been deleted successfully");
                            dlgAlert.setTitle("Delete Device");
                            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    onBackPressed();
                                }
                            });


                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();
                        }


                    }
                });
                backgroundTask.execute(method, devuniqueid);

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                setdefaultvalues();
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();




        // startActivity(new Intent(this, ViewAllDevices.class));
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



    }


    private void setdefaultvalues() {
//
        this.makefieldseditable(true);

        DeviceType.setSelection(((ArrayAdapter) DeviceType.getAdapter()).getPosition(devicetype));
        DeviceLocation.setSelection(((ArrayAdapter) DeviceLocation.getAdapter()).getPosition(devicelocation));
        DeviceManufacturer.setSelection(((ArrayAdapter) DeviceManufacturer.getAdapter()).getPosition(devicemanufacturer));
        DeviceNo.setText(deviceno);
        DeviceCluster.setSelection(((ArrayAdapter) DeviceCluster.getAdapter()).getPosition(devcluster));

        //   DeviceOwner.setSelection(listItems.indexOf(deviceowner));
        DeviceModel.setText(devicemodel);

this.makefieldseditable(false);

}


    public void makefieldseditable (boolean settoEnable) {

        DeviceNo.setEnabled(settoEnable);
        DeviceType.setEnabled(settoEnable);
        DeviceLocation.setEnabled(settoEnable);
        DeviceManufacturer.setEnabled(settoEnable);
        DeviceCluster.setEnabled(settoEnable);
        DeviceOwner.setEnabled(settoEnable);
        DeviceModel.setEnabled(settoEnable);

        }



    private void setviewforpermissions() {
        int totalvisible=4;
//ADMIN Button
        if ((session.isperm_AdminRequests()) || (session.isperm_EditAll()) || (session.isperm_EditOwn())) {

            btnEditDevice.setVisibility(View.VISIBLE);


        } else
        {
            btnEditDevice.setVisibility(View.GONE);

        }


        if ((session.isperm_DeleteAll()) || (session.isperm_DeleteOwn())) {

            buttondelete.setVisibility(View.VISIBLE);


        } else
        {
            buttondelete.setVisibility(View.GONE);

        }

    }


    public void getspinnerlistfromdatabase(final Context ctx) {
        String method = "updatelist";
        String fieldtobeupdated = "users";

        //   BackgroundTask.m_list=null;

        BackgroundTask backgroundTask = new BackgroundTask(ViewDevice.this, new AsyncResponse() {

            @Override
            public void processFinish(String output) {
                Log.e("invenapp:method:", new CommonProcs().getMethodname() + ":output:" + output);
                if (output.equals(StatusConstants.listretrievalSuccessful)) {

                    btnEditDevice.setEnabled(true);
                    btnEditDevice.setTextColor(Color.parseColor("#FFFFFF"));

                    listItems = BackgroundTask.m_list_users;

                    listItems.add(0, StatusConstants.selectiontext);

                    ArrayAdapter NoCoreAdapter = new ArrayAdapter(ctx,
                            R.layout.spinner_layout, listItems);

                    DeviceOwner.setAdapter(NoCoreAdapter);
                    DeviceOwner.setEnabled(true);

                    if(listItems.indexOf(deviceowner)<0){
                        DeviceOwner.setSelection(listItems.indexOf("others"));
                    }else
                    {
                        DeviceOwner.setSelection(listItems.indexOf(deviceowner));
                    }



                    DeviceOwner.setEnabled(false);

                    DeviceOwner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                        @Override
                        public void onItemSelected(AdapterView<?> parent, View arg1,
                                                   int arg2, long arg3) {


                            if (DeviceOwner.isEnabled())
                            {
                                ((TextView) arg1).setTextColor(Color.parseColor("#000000"));
                        }else
                            {
                                ((TextView) arg1).setTextColor(Color.parseColor("#FF9E9B9B"));
                            }

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