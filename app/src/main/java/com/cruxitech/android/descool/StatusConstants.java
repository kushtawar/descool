package com.cruxitech.android.descool;

/**
 * Created by kushtawar on 02/08/16.
 */
public class StatusConstants {


    public static final String statusRegistrationSuccessful="registration:successful";
    public static final String statusRegistrationUnsuccessful="registration:unsuccessful";
    public static final String statusRegistrationErrorDuplicate="This username/email already exists. Please choose another one.";


    public static final String statusLoginSuccessful="login:successful";
    public static final String statusLoginUnsuccessful="login:unsuccessful";

    public static final String statusAddDeviceSuccessful="adddevice:successful";
    public static final String statusAddDeviceUnsuccessful="adddevice:unsuccessful";
    public static final String statusDeviceErrorDuplicate="Duplicate Device Number-Device not updated";

    public static final String statusViewAllDevicesSuccessful="viewalldevices:successful";
    public static final String statusViewAllDevicesUnsuccessful="viewalldevices:unsuccessful";

    public static final String statusViewMyDeviceSuccessful="viewmydevice:successful";
    public static final String statusViewMyDeviceUnsuccessful="viewmydevice:unsuccessful";

    public static final String statusEditviewDeviceSuccessful="editview:successful";
    public static final String statusEditviewDeviceUnsuccessful="editview:unsuccessful";

    public static final String statusDeleteOneDeviceSuccessful="deleteonedevice:successful";
    public static final String statusDeleteOneDeviceUnsuccessful="deleteonedevice:unsuccessful";

    public static final String listretrievalSuccessful="list retrieval:successful";
    public static final String listretrievalUnsuccessful="list retrieval:unsuccessful";

    public static final String insertionuserrolesSuccessful="insertionuserroles:successful";
    public static final String insertionuserrolesUnSuccessful="insertionuserroles:unsuccessful";

    public static final String insertionrolepermissionsSuccessful="insertionrolepermissions:successful";
    public static final String insertionrolepermissionsUnuccessful="insertionrolepermissions:unsuccessful";

    public static final String statusgetpermissionsSuccessful="getpermissions:successful";
    public static final String statusgetpermissionsUnsuccessful="getpermissions:unsuccessful";

    public static final String statusactiveSuccessful="makeactive:successful";
    public static final String statusactiveUnsuccessful="makeactive:unsuccessful";

    public static final String statusforgotpasswordSuccessful="password reset:successful";
    public static final String statusforgotpasswordUnsuccessful="password reset:unsuccessful";

    public static final String statuschangepasswordSuccessful="password change:successful";
    public static final String statuschangepasswordUnsuccessful="password change:unsuccessful";

    //

    public static final String selectiontext="    ";
    //


    public static final String serverurl = "http://www.cruxitech.com/DeviceManagement";
    // String serverurl="http://192.168.0.14/androidmysql/";
    // String serverurl = "http://192.168.43.157/androidmysql/";
    public static final String reg_url = serverurl + "/register.php";
    public static final String login_url = serverurl + "/login.php";
    public static final String commonwrite_url = serverurl + "/commonwrite.php";
    public static final String getdevices_url = serverurl + "/getdevices.php";
    public static final String getlist_url = serverurl + "/getlist.php";



    public static final String userroles="userroles";
    public static final String rolepermissions="rolepermissions";


    public static final String homeadmin="Admin";
    public static final String homeviewmy="My Devices";
    public static final String homeviewall="View All Devices";
    public static final String homeaddnew="Add New Device";






}
