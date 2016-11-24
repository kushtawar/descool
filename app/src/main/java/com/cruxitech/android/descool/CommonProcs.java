package com.cruxitech.android.descool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by kushtawar on 01/08/16.
 */
public class CommonProcs  {

     static HashMap bundleroles = null;
     static HashMap bundleusers = null;


    public String getMethodname() {

        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[1];//coz 0th will be getStackTrace so 1st
        String methodName = e.getMethodName();
        System.out.println(methodName);
        return methodName;

    }

    public Bundle onClickViewDeviceList(Context ctx,DeviceOrderAdapter m_adapter,int i, Intent newActivity) {
        DeviceOrder oi= (DeviceOrder) m_adapter.getItem(i);
        Toast.makeText(ctx, "You Clicked " + oi.getDevuniqueid(), Toast.LENGTH_SHORT).show();




        Bundle b = new Bundle();
        b.putString("key_devuniqueid", oi.getDevuniqueid());

        b.putString("key_Devtype", oi.getDevtype());
        b.putString("key_Devlocation", oi.getDevlocation());
        b.putString("key_Devmanufacturer", oi.getDevmanufacturer());

        b.putString("key_Devno", oi.getDevno());
        b.putString("key_Devowner", oi.getDevowner());
        b.putString("key_Devmodel", oi.getDevicemodel());

        b.putString("key_Devlastupdatedby", oi.getLastupdatedby());
        b.putString("key_Devlastupdatedon", oi.getLastupdatedon());
//b.putString("key_Devlastupdatedon", m_adapter.getItem(i).getLastupdatedon());
        newActivity.putExtras(b); //Put your id to your next Intent

        return b;
    }


    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy

        sdfDate.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        Date now = new Date();

        String strDate = sdfDate.format(now);
        return strDate;
    }


    public void commonprogressdialog(ProgressDialog mProgressDialog) {

        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

    }
    public void dismisscommonprogressdialog(ProgressDialog mProgressDialog) {
        try{
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }}catch(Exception ex){
            Log.e("invenapp:dialog",ex.getStackTrace().toString());
        }

    }



    public void getDeviceOrders(JSONArray jArray){
        try{

            while (BackgroundTask.jsonstring == null) {

            }

            // Log.e("log_inventory", "JSONresponse\n " + jsonstringval);

            jArray = new JSONArray(BackgroundTask.jsonstring);
            BackgroundTask.m_orders = new ArrayList<DeviceOrder>();
            for(int i=0; i<jArray.length();i++) {
                DeviceOrder d1 = new DeviceOrder();


                JSONObject json = jArray.getJSONObject(i);
                //    Log.e("log_inventory", "morders_1\n " + json.getString("devtype")+":"+json.getString("deviceno")+":"+json.getString("deviceowner"));

                d1.setDevuniqueid(json.getString("devuniqueid"));
                d1.setDevtype(json.getString("devtype"));
                d1.setDevlocation(json.getString("devlocation"));
                d1.setDevmanufacturer(json.getString("devmanufacturer"));
                d1.setDevno(json.getString("deviceno"));
                d1.setDevowner(json.getString("deviceowner"));
                d1.setDevicemodel(json.getString("devicemodel"));
                d1.setLastupdatedon(json.getString("lastupdatedon"));
                d1.setLastupdatedby(json.getString("lastupdatedby"));


                BackgroundTask.m_orders.add(d1);

            }

            Log.e("log_inventory", "m_orders\n " + BackgroundTask.m_orders);

         //   Thread.sleep(5000);


        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
        }
        //  runOnUiThread(returnRes);
    }

    public void getlogindetails(JSONArray jArray){
        try{

            while (BackgroundTask.jsonstringforlogin == null) {

            }

            // Log.e("log_inventory", "JSONresponse\n " + jsonstringval);

            jArray = new JSONArray(BackgroundTask.jsonstringforlogin);
            BackgroundTask.login_orders = new ArrayList<LoginBean>();
            for(int i=0; i<jArray.length();i++) {
                LoginBean d1 = new LoginBean();


                JSONObject json = jArray.getJSONObject(i);
                //    Log.e("log_inventory", "morders_1\n " + json.getString("devtype")+":"+json.getString("deviceno")+":"+json.getString("deviceowner"));

                d1.setName(json.getString("name"));
                d1.setEmail(json.getString("user_email"));
                d1.setUsername(json.getString("user_name"));
                d1.setUniqueuserid(json.getString("userid"));
                d1.setActivestatus(json.getString("active"));
                d1.setHashkey(json.getString("hash"));

                BackgroundTask.login_orders.add(d1);

            }

            Log.e("log_inventory", "login_orders\n " + BackgroundTask.login_orders);

            //Thread.sleep(5000);


        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
        }
        //  runOnUiThread(returnRes);
    }



    public void getlistfromdb(JSONArray jArray,String fieldnamefromdb){
        try{




            while (BackgroundTask.jsonstring == null) {

            }

            // Log.e("log_inventory", "JSONresponse\n " + jsonstringval);

            jArray = new JSONArray(BackgroundTask.jsonstring);



           switch (fieldnamefromdb) {
               case "roles":
                   BackgroundTask.m_list_roles =null;
                   BackgroundTask.m_list_roles = new ArrayList<String>();
                   bundleroles = new HashMap();
               for (int i = 0; i < jArray.length(); i++) {
                   JSONObject json = jArray.getJSONObject(i);
                   BackgroundTask.m_list_roles.add(json.getString("rolename"));

                   bundleroles.put(json.getString("rolename"), json.getString("roleid"));

               }
                   Log.e("log_inventory", "m_list_roles\n " + BackgroundTask.m_list_roles);
           break;

               case "users":
                   BackgroundTask.m_list_users =null;
                   BackgroundTask.m_list_users = new ArrayList<String>();
                   bundleusers = new HashMap();

                   for (int i = 0; i < jArray.length(); i++) {


                       JSONObject json = jArray.getJSONObject(i);
                       BackgroundTask.m_list_users.add(json.getString("user_name"));

                       bundleusers.put(json.getString("user_name"), json.getString("userid"));
                      // BackgroundTask.m_list_users.add(json.getString("userid"));
                   }
                   Log.e("log_inventory", "m_list_users\n " + BackgroundTask.m_list_users);

                   break;

           }

            //Thread.sleep(5000);


        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
        }
        //  runOnUiThread(returnRes);
    }






    public String methodExecute(String response,String stringurl,String data) throws Exception{

        URL url = new URL(stringurl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        //httpURLConnection.setDoInput(true);
        OutputStream OS = httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

        bufferedWriter.write(data);
        bufferedWriter.flush();
        bufferedWriter.close();
        OS.close();
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            response += line;
        }
        bufferedReader.close();

        inputStream.close();
        //httpURLConnection.connect();
        httpURLConnection.disconnect();

        Log.e("invenapp:method", "JSONresponse\n " + response);

        BackgroundTask.jsonstring = response;
        Log.e("invenapp:method", "JSONstring\n " + BackgroundTask.jsonstring);
        return response;

    }



    public void getpermissiondetails(JSONArray jArray){
        try{

            while (BackgroundTask.jsonstringforpermissions == null) {

            }

            // Log.e("log_inventory", "JSONresponse\n " + jsonstringval);

            jArray = new JSONArray(BackgroundTask.jsonstringforpermissions);

            BackgroundTask.m_list_permissions=new ArrayList<RoleBean>();

            for(int i=0; i<jArray.length();i++) {
                RoleBean r1 = new RoleBean();
                JSONObject json = jArray.getJSONObject(i);

               // r1.setRoleid(json.getString("roleid"));
                r1.setRolename(json.getString("rolename"));
                r1.setPermissionname(json.getString("permissionname"));

                BackgroundTask.m_list_permissions.add(r1);

            }

            Log.e("invenapp:", "m_list_permissions\n " + BackgroundTask.m_list_permissions);

            //Thread.sleep(5000);


        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
        }
        //  runOnUiThread(returnRes);
    }








    protected boolean sendSMS(Activity act) {
        try {
            String toPhoneNumber = "00491639381196";//toPhoneNumberET.getText().toString();
            String smsMessage = "code is 1234"; //smsMessageET.getText().toString();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(toPhoneNumber, null, smsMessage, null, null);
            return true;

        }catch(Exception e)
        {
            Log.e("invenapp:sms",e.getStackTrace().toString());
            return false;
        }

    }

public void createpopupblank(Context ctx, String setmessage,String settitle){
    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
    builder.setMessage(setmessage);

    builder.setTitle(Html.fromHtml("<font color='#D23927'>" + settitle + "</font>"));
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {

        }
    });
    builder.setCancelable(true);
    builder.create().show();
}



}

