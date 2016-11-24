package com.cruxitech.android.invenapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

/**

 /**
 * Created by Chandan on 7/14/2016.
 */
    public class BackgroundTask extends AsyncTask<String,Void,String> {
    public boolean loginstatus = false;
    String method=null;
    private ProgressDialog mProgressDialog;

    public JSONArray jArray = null;
    JSONArray jsonar = new JSONArray();

    public AsyncResponse delegate = null;//Call back interface
    public static String jsonstring = null;
    public static String jsonstringforlogin=null;
    public static String jsonstringforuserroles=null;
    public static String jsonstringforpermissions=null;

    WeakReference<Activity> mWeakActivity;
    public static ArrayList<DeviceOrder> m_orders = null;
    public static ArrayList<LoginBean> login_orders = null;

    public static ArrayList<String> m_list_users = null;
    public static ArrayList<String> m_list_roles = null;


    public static ArrayList<RoleBean> m_list_permissions = null;

    public String response = "";
    public String loggedin_username="";
    public String loggedin_user_email="";
    public static String JSON;

    private ProgressDialog pDialog;
    SessionManager session;

    Context ctx;
    String table_name = null;

    BackgroundTask(Context ctx,AsyncResponse delegate) {
try {
    this.delegate = delegate;

    this.ctx = ctx.getApplicationContext();
    mProgressDialog = new ProgressDialog(ctx);

    session = new SessionManager(ctx.getApplicationContext());

    HashMap<String, String> user = session.getUserDetails();

    // name
    loggedin_username = user.get(SessionManager.KEY_USERNAME);

    // email
    loggedin_user_email = user.get(SessionManager.KEY_EMAIL);

    switch (ctx.getClass().getSimpleName()) {


        case "Landingpage":
            switch (BaseActivity.methodbase) {

                case "activateaccount":
                    mProgressDialog.setMessage("Authorizing!!");
                    new CommonProcs().commonprogressdialog(mProgressDialog);
                    break;
                case "getpermissions":
                    mProgressDialog.setMessage("Checking the assigned roles!!");
                    new CommonProcs().commonprogressdialog(mProgressDialog);
                    break;
            }
break;

        case "MainActivity":

            switch (MainActivity.methodmain) {
                case "login":
                    mProgressDialog.setMessage("Authenticating..Please wait!!");
                    new CommonProcs().commonprogressdialog(mProgressDialog);
                    break;

                case "getpermissions":
                    mProgressDialog.setMessage("Checking the assigned roles!!");
                    new CommonProcs().commonprogressdialog(mProgressDialog);
                    break;



                default:
                    break;

            }
            break;
        case "Register":
            mProgressDialog.setMessage("Please wait while we are validating the data..!!");

            break;
        case "AddDevice":
            mProgressDialog.setMessage("Please wait data is being added..!!");

            break;

        case "ViewAllDevices":
            mProgressDialog.setMessage("Give us few seconds while we find all available devices ..!!");

            break;

        case "ViewMyDevices":
            mProgressDialog.setMessage("Please wait data is being loaded ..!!");

            break;

        case "ViewDevice":


            mProgressDialog.setMessage("The record is being synchronized with the common database. Please do not press Back button..");


            break;

        //case StatusConstants.insertionuserrolesSuccessful:


        default:
            mProgressDialog.setMessage("Please do not press Back button..");

            break;

    }
    new CommonProcs().commonprogressdialog(mProgressDialog);

}catch(Exception ex){

    Log.e("invenapp:showdialog",ex.getStackTrace().toString());

}

    }


    @Override
    protected void onPreExecute() {

        try {
            mProgressDialog.show();
        }catch (Exception ex){
            Log.e("invenapp:preexecute",ex.getStackTrace().toString());
        }

    }

    @Override
    protected String doInBackground(String... params) {
        String methodtoexecute=null;
        method = params[0];

        switch (method) {

            case "activateaccount":
                String passeduserid = params[1];
                String hashkey=params[2];
                String table_name=StatusConstants.userroles;

                try {

                    String data =
                                   URLEncoder.encode("selecteduserid", "UTF-8") + "=" + URLEncoder.encode(passeduserid, "UTF-8")+ "&" +
                                           URLEncoder.encode("hashkey", "UTF-8") + "=" + URLEncoder.encode(hashkey, "UTF-8")+"&" +
                            URLEncoder.encode("methodtoexecute", "UTF-8") + "=" + URLEncoder.encode(method, "UTF-8")+"&" +
                                           URLEncoder.encode("table_name", "UTF-8") + "=" + URLEncoder.encode(table_name, "UTF-8");


                    response= new CommonProcs().methodExecute(response,StatusConstants.commonwrite_url, data);


                    if (response.toLowerCase().contains(":unsuccessful"))
                    {
                        return StatusConstants.statusactiveUnsuccessful;
                    }
                    return StatusConstants.statusactiveSuccessful;


                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (UnknownHostException e) {
                    Log.e("log_inventory", "Unable to connect to the host database");
                    response = "Unknown host";
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;



            case "registration":
                String name = params[1];
                String user_name = params[2];
                String user_pass = params[3];
                String user_email = params[4];
                table_name = method;
                try {

                    String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                            URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&" +
                            URLEncoder.encode("user_pass", "UTF-8") + "=" + URLEncoder.encode(user_pass, "UTF-8") + "&" +
                            URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user_email, "UTF-8") + "&" +
                            URLEncoder.encode("table_name", "UTF-8") + "=" + URLEncoder.encode(table_name, "UTF-8") ;


                   response= new CommonProcs().methodExecute(response,StatusConstants.reg_url, data);


                    if (response.toLowerCase().contains("duplicate")) {
                        return StatusConstants.statusRegistrationErrorDuplicate;

                    }
                    else if (response.toLowerCase().contains(":unsuccessful") || response.toLowerCase().contains("error"))
                    {
                        return StatusConstants.statusRegistrationUnsuccessful;
                    }
                    else
                    {
                        return StatusConstants.statusRegistrationSuccessful;
                    }


                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (UnknownHostException e) {
                    Log.e("log_inventory", "Unable to connect to the host database");
                    response = "Unknown host";
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case "login":
                String login_name = params[1];
                String login_pass = params[2];
                table_name = method;
                try {

                    String data = URLEncoder.encode("login_name", "UTF-8") + "=" + URLEncoder.encode(login_name, "UTF-8") + "&" +
                            URLEncoder.encode("login_pass", "UTF-8") + "=" + URLEncoder.encode(login_pass, "UTF-8") + "&" +
                            URLEncoder.encode("table_name", "UTF-8") + "=" + URLEncoder.encode(table_name, "UTF-8");
                    URL url = new URL(StatusConstants.login_url);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    response = response.trim();
                    jsonar = new JSONArray(response);
                    jsonstringforlogin = response;
                    Log.e("invenapp", "login:JSONresponse\n " + jsonstringforlogin);


                   new CommonProcs().getlogindetails(jArray);


                    if (response.toLowerCase().contains(":unsuccessful")){
                        return StatusConstants.statusLoginUnsuccessful;

                    }
                    else {
                        return StatusConstants.statusLoginSuccessful;
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    Log.e("invenapp", "Unable to connect to the host database");
                    response = "Unknown host";
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "viewalldevices":

                table_name = method;
                methodtoexecute="viewalldevices";
                try {
                    String data =
                            URLEncoder.encode("table_name", "UTF-8") + "=" + URLEncoder.encode(table_name, "UTF-8")+ "&" +
                                    URLEncoder.encode("methodtoexecute", "UTF-8") + "=" + URLEncoder.encode(methodtoexecute, "UTF-8");
                    response=new CommonProcs().methodExecute(response,StatusConstants.getdevices_url, data);

                new CommonProcs().getDeviceOrders(jArray);

                    // response="viewdevices";
                    if (response.toLowerCase().contains(":unsuccessful")){
                        return StatusConstants.statusViewAllDevicesUnsuccessful;

                    }
                    else {
                        return StatusConstants.statusViewAllDevicesSuccessful;
                    }



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    Log.e("log_inventory", "Unable to connect to the host database");
                    response = "Unknown host";
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {

                    e.printStackTrace();
                }

                break;

            case "viewmydevices":

                table_name = method;
                methodtoexecute="viewmydevices";
                try {
                    String data =
                            URLEncoder.encode("table_name", "UTF-8") + "=" + URLEncoder.encode(table_name, "UTF-8")+ "&" +
                                    URLEncoder.encode("methodtoexecute", "UTF-8") + "=" + URLEncoder.encode(methodtoexecute, "UTF-8")+ "&" +
                                    URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(loggedin_username, "UTF-8");

                    response=new CommonProcs().methodExecute(response,StatusConstants.getdevices_url,data);


                    new CommonProcs().getDeviceOrders(jArray);

                    // response="viewdevices";

                    if (response.toLowerCase().contains(":unsuccessful")){
                        return StatusConstants.statusViewMyDeviceUnsuccessful;

                    }
                    else {
                        return StatusConstants.statusViewMyDeviceSuccessful;
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    Log.e("log_inventory", "Unable to connect to the host database");
                    response = "Unknown host";
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {

                    e.printStackTrace();
                }

                break;

            case "adddevice":


                String devtype = params[1];
                String dev_location = params[2];
                String dev_manufacturer = params[3];
                String dev_no = params[4];
                String dev_owner = params[5];
                String dev_model = params[6];
                String dev_lastupdatedby = loggedin_username;
                table_name = method;

                methodtoexecute="insertdevice";

                try {

                    String data = URLEncoder.encode("device_type", "UTF-8") + "=" + URLEncoder.encode(devtype, "UTF-8") + "&" +
                            URLEncoder.encode("device_location", "UTF-8") + "=" + URLEncoder.encode(dev_location, "UTF-8") + "&" +
                            URLEncoder.encode("device_manufacturer", "UTF-8") + "=" + URLEncoder.encode(dev_manufacturer, "UTF-8") + "&" +
                            URLEncoder.encode("device_no", "UTF-8") + "=" + URLEncoder.encode(dev_no, "UTF-8") + "&" +
                            URLEncoder.encode("device_owner", "UTF-8") + "=" + URLEncoder.encode(dev_owner, "UTF-8") + "&" +
                            URLEncoder.encode("device_model", "UTF-8") + "=" + URLEncoder.encode(dev_model, "UTF-8") + "&" +
                            URLEncoder.encode("device_lastupdatedby", "UTF-8") + "=" + URLEncoder.encode(dev_lastupdatedby, "UTF-8") + "&" +
                            URLEncoder.encode("table_name", "UTF-8") + "=" + URLEncoder.encode(table_name, "UTF-8")+ "&" +
                            URLEncoder.encode("methodtoexecute", "UTF-8") + "=" + URLEncoder.encode(methodtoexecute, "UTF-8");

                   response= new CommonProcs().methodExecute(response,StatusConstants.commonwrite_url, data);

                    if (response.toLowerCase().contains(":unsuccessful")){
                        return StatusConstants.statusAddDeviceUnsuccessful;

                    }
                    else {
                        return StatusConstants.statusAddDeviceSuccessful;
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    Log.e("log_inventory", "Unable to connect to the host database");
                    response = "Unknown host";
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "editdevice":
//from ViewDevice.java

                String edit_devtype = params[1];
                String edit_dev_location = params[2];
                String edit_dev_manufacturer = params[3];
                String edit_dev_no = params[4];
                String edit_dev_owner = params[5];
                String edit_dev_model = params[6];
                String edit_devuniqueid = params[7];
                String edit_dev_lastupdatedby = loggedin_username;
                table_name = method;
                 methodtoexecute="updatedevice";

                try {
                    String data = URLEncoder.encode("device_type", "UTF-8") + "=" + URLEncoder.encode(edit_devtype, "UTF-8") + "&" +
                            URLEncoder.encode("device_location", "UTF-8") + "=" + URLEncoder.encode(edit_dev_location, "UTF-8") + "&" +
                            URLEncoder.encode("device_manufacturer", "UTF-8") + "=" + URLEncoder.encode(edit_dev_manufacturer, "UTF-8") + "&" +
                            URLEncoder.encode("device_no", "UTF-8") + "=" + URLEncoder.encode(edit_dev_no, "UTF-8") + "&" +
                            URLEncoder.encode("device_owner", "UTF-8") + "=" + URLEncoder.encode(edit_dev_owner, "UTF-8") + "&" +
                            URLEncoder.encode("device_model", "UTF-8") + "=" + URLEncoder.encode(edit_dev_model, "UTF-8") + "&" +
                            URLEncoder.encode("device_lastupdatedby", "UTF-8") + "=" + URLEncoder.encode(edit_dev_lastupdatedby, "UTF-8") + "&" +
                            URLEncoder.encode("table_name", "UTF-8") + "=" + URLEncoder.encode(table_name, "UTF-8") + "&" +
                            URLEncoder.encode("record_devuniqueid", "UTF-8") + "=" + URLEncoder.encode(edit_devuniqueid, "UTF-8")+ "&" +
                            URLEncoder.encode("methodtoexecute", "UTF-8") + "=" + URLEncoder.encode(methodtoexecute, "UTF-8");;

                    response=new CommonProcs().methodExecute(response,StatusConstants.commonwrite_url, data);

                    if (response.toLowerCase().contains(":unsuccessful")){
                        return StatusConstants.statusEditviewDeviceUnsuccessful;

                    }
                    else {
                        return StatusConstants.statusEditviewDeviceSuccessful;
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    Log.e("log_inventory", "Unable to connect to the host database");
                    response = "Unknown host";
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "deletedevice":

             edit_devuniqueid = params[1];

                table_name = "deletedevice";
                methodtoexecute=method;

                try {
                    String data =
                            URLEncoder.encode("table_name", "UTF-8") + "=" + URLEncoder.encode(table_name, "UTF-8") + "&" +
                                    URLEncoder.encode("record_devuniqueid", "UTF-8") + "=" + URLEncoder.encode(edit_devuniqueid, "UTF-8")+ "&" +
                                    URLEncoder.encode("methodtoexecute", "UTF-8") + "=" + URLEncoder.encode(methodtoexecute, "UTF-8");;

                    response=new CommonProcs().methodExecute(response,StatusConstants.commonwrite_url,data);

                    if (response.toLowerCase().contains(":unsuccessful")){
                        return StatusConstants.statusDeleteOneDeviceUnsuccessful;

                    }
                    else {
                        return StatusConstants.statusDeleteOneDeviceSuccessful;
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    Log.e("log_inventory", "Unable to connect to the host database");
                    response = "Unknown host";
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                break;


            case "updatelist":

                String fieldtobeupdated=params[1];
                methodtoexecute = params[0];



                try {
                    String data =
                            URLEncoder.encode("fieldtobeupdated", "UTF-8") + "=" + URLEncoder.encode(fieldtobeupdated, "UTF-8") + "&" +
                                    URLEncoder.encode("methodtoexecute", "UTF-8") + "=" + URLEncoder.encode(methodtoexecute, "UTF-8");;

                    jsonstring=null;

                    response=new CommonProcs().methodExecute(response,StatusConstants.getlist_url,data);
                    new CommonProcs().getlistfromdb(jArray,fieldtobeupdated);



                    if ((response.toLowerCase().contains(":unsuccessful")) || (response.toLowerCase().contains("error"))){
                        return StatusConstants.listretrievalUnsuccessful;

                    }
                    else {
                        return StatusConstants.listretrievalSuccessful;
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    Log.e("log_inventory", "Unable to connect to the host database");
                    response = "Unknown host";
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                break;


            case "insertonetoarraydevices":


                methodtoexecute = params[0];
                table_name=params[1];
                String selecteduserid=params[2];
                String arrayrolesid=params[3];
                Log.e("invenapp:arrayrolesid",arrayrolesid);
                try {
                    String data =
                            URLEncoder.encode("table_name", "UTF-8") + "=" + URLEncoder.encode(table_name, "UTF-8") + "&" +
                                    URLEncoder.encode("methodtoexecute", "UTF-8") + "=" + URLEncoder.encode(methodtoexecute, "UTF-8")+ "&" +
                                    URLEncoder.encode("selecteduserid", "UTF-8") + "=" + URLEncoder.encode(selecteduserid, "UTF-8")+ "&" +
                                    URLEncoder.encode("arrayids", "UTF-8") + "=" + URLEncoder.encode(arrayrolesid, "UTF-8");

                    jsonstringforuserroles=null;

                    response=new CommonProcs().methodExecute(response,StatusConstants.commonwrite_url,data);
                   // new CommonProcs().getlistfromdb(jArray,fieldtobeupdated);



                    if (response.toLowerCase().contains(":unsuccessful")){
                        return StatusConstants.insertionuserrolesUnSuccessful;

                    }
                    else {
                        return StatusConstants.insertionuserrolesSuccessful;
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    Log.e("log_inventory", "Unable to connect to the host database");
                    response = "Unknown host";
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case "getpermissions":

                String userid=params[1];
                try {
                String data =
                        URLEncoder.encode("methodtoexecute", "UTF-8") + "=" + URLEncoder.encode(method, "UTF-8")+ "&" +
                                URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");

                    jsonstringforpermissions=null;

                    response=new CommonProcs().methodExecute(response,StatusConstants.getlist_url,data);


                    jsonstringforpermissions=response;

                    new CommonProcs().getpermissiondetails(jArray);
                    if (response.toLowerCase().contains(":unsuccessful")){
                        return StatusConstants.statusgetpermissionsUnsuccessful;

                    }
                    else {
                        return StatusConstants.statusgetpermissionsSuccessful;
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    Log.e("log_inventory", "Unable to connect to the host database");
                    response = "Unknown host";
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            break;

            default:
                response = null;
                break;


        }

        return response;


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {

        switch (result) {
            case StatusConstants.statusRegistrationSuccessful:
            case StatusConstants.statusLoginSuccessful:
            case StatusConstants.statusEditviewDeviceSuccessful:
            case StatusConstants.statusDeleteOneDeviceSuccessful:
            case StatusConstants.statusViewAllDevicesSuccessful:
            case StatusConstants.statusViewMyDeviceSuccessful:
            case StatusConstants.statusAddDeviceSuccessful:
            case StatusConstants.insertionuserrolesSuccessful:
            case StatusConstants.statusgetpermissionsSuccessful:



               // Toast.makeText(ctx, "Refreshing..", Toast.LENGTH_SHORT).show();
                new CommonProcs().dismisscommonprogressdialog(mProgressDialog);
                delegate.processFinish(result);
                break;

            case StatusConstants.statusRegistrationErrorDuplicate:


                Toast.makeText(ctx, StatusConstants.statusRegistrationErrorDuplicate, Toast.LENGTH_LONG).show();
                new CommonProcs().dismisscommonprogressdialog(mProgressDialog);
                delegate.processFinish(result);
                break;


            case StatusConstants.statusactiveSuccessful:


                Toast.makeText(ctx, "Account Activated", Toast.LENGTH_SHORT).show();
                new CommonProcs().dismisscommonprogressdialog(mProgressDialog);
                delegate.processFinish(result);
                break;

            case StatusConstants.listretrievalSuccessful:
                new CommonProcs().dismisscommonprogressdialog(mProgressDialog);
                delegate.processFinish(result);

                break;


            case "Unknown host":
                Toast.makeText(ctx.getApplicationContext(), "The database could not be connected:" + result, Toast.LENGTH_LONG).show();

                new CommonProcs().dismisscommonprogressdialog(mProgressDialog);
                delegate.processFinish(result);
                //   CommonProcs.showalertdialogError(ctx,"Database Host could not be connected");


                break;
            case StatusConstants.statusViewAllDevicesUnsuccessful:
            case StatusConstants.statusViewMyDeviceUnsuccessful:
                Toast.makeText(ctx, "No data found !!" , Toast.LENGTH_LONG).show();

                new CommonProcs().dismisscommonprogressdialog(mProgressDialog);
                delegate.processFinish(result);
                break;
            default:
                Toast.makeText(ctx, "Something went wrong.Please contact administrator " + result, Toast.LENGTH_LONG).show();

                new CommonProcs().dismisscommonprogressdialog(mProgressDialog);
                delegate.processFinish(result);

                break;


        }
    }



}