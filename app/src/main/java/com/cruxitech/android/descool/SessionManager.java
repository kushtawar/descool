package com.cruxitech.android.descool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SessionManager {

    // Shared Preferences reference
    SharedPreferences pref;



    // Editor reference for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "invenapppreferences";

    // All Shared Preferences Keys
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)

    public static final String KEY_UNIQUEUSERID = "uniqueuserid";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ISACTIVE = "inactive";
    public static final String KEY_HASHKEY = "0000";

    public static ArrayList<String> currentrolelistarray=null;

    public static final String role_viewown="View_Own";
    public static final String role_viewall="View_All_Devices";
    public static final String role_adddevice="Add_Device";
    public static final String role_deviceadmin="Device_Admin";
    public static final String role_useradmin="User_Admin";
    public static final String role_superuser="Super_User";

    public static String rolelist= "";




    public static final String permission_viewown="View_Own";
    public static final String permission_editown="Edit_Own";
    public static final String permission_deleteown="Delete_Own";
    public static final String permission_viewall="View_All";
    public static final String permission_editall="Edit_All";
    public static final String permission_deleteall="Delete_All";
    public static final String permission_adddevice="Add_Device";
    public static final String permission_manageusers="Manage_Users";
    public static final String permission_adminrequests="Admin_Requests";


    public static  boolean flag_permission_viewown=true;
    public static  boolean flag_permission_editown=false;
    public static  boolean flag_permission_deleteown=false;
    public static  boolean flag_permission_viewall=false;
    public static  boolean flag_permission_editall=false;
    public static  boolean flag_permission_deleteall=false;
    public static  boolean flag_permission_adddevice=false;
    public static  boolean flag_permission_manageusers=false;





    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String uniqueuserid,String name,String username,  String email,String isactive,String hashkey){

        editor.clear();

        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);


        // Storing name in pref
        editor.putString(KEY_UNIQUEUSERID, uniqueuserid);

        // Storing name in pref
        editor.putString(KEY_USERNAME, username);


        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        editor.putString(KEY_ISACTIVE, isactive);

        editor.putString(KEY_HASHKEY, hashkey);

        flag_permission_viewown=true;
        // commit changes

    }


    public void createUserRolesPermissions(ArrayList<RoleBean> arrayrolespermissions){

        rolelist="";

        for (RoleBean permstring:arrayrolespermissions)
        {
            switch(permstring.getPermissionname()) {

        case permission_viewown:
            editor.putBoolean(permission_viewown, true);
            break;

    case permission_viewall:
        editor.putBoolean(permission_viewall, true);
        break;

        case permission_editown:
            editor.putBoolean(permission_editown, true);
        break;

    case permission_deleteown:
        editor.putBoolean(permission_deleteown, true);
        break;

    case permission_editall:
        editor.putBoolean(permission_editall, true);
        break;
    case permission_deleteall:
        editor.putBoolean(permission_deleteall, true);
        break;
    case permission_adddevice:
        editor.putBoolean(permission_adddevice, true);
        break;
    case permission_manageusers:
        editor.putBoolean(permission_manageusers, true);
        break;

    case permission_adminrequests:
        editor.putBoolean(permission_adminrequests, true);
        break;

    default:
        editor.putBoolean(permission_viewown, true);

}

        }

        Set<String> uniquerolelist=new HashSet<>();
        for (RoleBean rolestring:arrayrolespermissions) {

            uniquerolelist.add(rolestring.getRolename());
        }

        for (String rolestring:uniquerolelist) {

            setroles(rolestring);

             rolelist= rolelist+"\n" +rolestring;

        }


Log.e("invenapp","rolelist"+rolelist);


        // commit changes
        editor.commit();
    }
public void commiteditor()
    {
        editor.commit();
    }

    private void setroles(String rolename) {

        editor.putBoolean(rolename, true);
    }


    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_UNIQUEUSERID, pref.getString(KEY_UNIQUEUSERID, null));


        // user name
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));


        //  name of the user
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_ISACTIVE, pref.getString(KEY_ISACTIVE, null));
        user.put(KEY_HASHKEY, pref.getString(KEY_HASHKEY, null));


        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, MainActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public boolean isperm_ViewOwn(){
        return pref.getBoolean(permission_viewown, false);
    }
    public boolean isperm_ViewAll(){
        return pref.getBoolean(permission_viewall, false);
    }
    public boolean isperm_EditOwn(){
        return pref.getBoolean(permission_editown, false);
    }
    public boolean isperm_DeleteOwn(){
        return pref.getBoolean(permission_deleteown, false);
    }

    public boolean isperm_EditAll(){
        return pref.getBoolean(permission_editall, false);
    }

    public boolean isperm_DeleteAll(){
        return pref.getBoolean(permission_deleteall, false);
    }
    public boolean isperm_AddDevice(){
        return pref.getBoolean(permission_adddevice, false);
    }
    public boolean isperm_ManageUsers(){
        return pref.getBoolean(permission_manageusers, false);
    }
    public boolean isperm_AdminRequests(){return pref.getBoolean(permission_adminrequests, false);
    }

    /*public boolean isrole_ViewOwn(){
        return pref.getBoolean(role_viewown, false);
    }
    public boolean isrole_ViewAll(){
        return pref.getBoolean(role_viewall, false);
    }
    public boolean isrole_AddDevice(){
        return pref.getBoolean(role_viewown, false);
    }
    public boolean isrole_DeviceAdmin(){
        return pref.getBoolean(role_viewown, false);
    }
    public boolean isrole_UserAdmin(){
        return pref.getBoolean(role_viewown, false);
    }
    public boolean isrole_SuperUser(){
        return pref.getBoolean(role_viewown, false);
    }*/

    public void cleareditor(){
        editor.clear();
    }

}