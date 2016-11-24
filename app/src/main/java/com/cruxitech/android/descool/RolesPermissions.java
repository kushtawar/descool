package com.cruxitech.android.descool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class RolesPermissions extends BaseActivity {

    SessionManager session;
    ListView listview;
    String[] foody;
    Context context;
    ArrayList<String> listItems=new ArrayList<>();
    ArrayList<String> listItemschecked=new ArrayList<>();
    public static ArrayList<String> rolesids= new ArrayList<>();
    int selecteduserid=0;
    public static boolean flagitemsupdate=false;
    String selectedusername="";
    // set adapter for listview
    ArrayAdapter<String> adapter;
    private Spinner spinner_roles;

    HashMap cusers= null;
    HashMap croles= null;

    static int positionofviewown=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roles_permissions);
       // context = this;

        session = new SessionManager(getApplicationContext());
        spinner_roles=(Spinner)findViewById(R.id.spinnerUsers);

        this.getspinnerlistfromdatabase();




        //spinner_roles.setOnItemSelectedListener(this);

        listview = (ListView)findViewById(R.id.listviewroles);
        //finally to be taken from database
        listview.setVisibility(View.VISIBLE);

        this.getcheckedlistitemsfromdatabase();

         cusers= CommonProcs.bundleusers;
         croles= CommonProcs.bundleroles;

        CheckedTextView checkedTextView=(CheckedTextView)findViewById(R.id.checkedlistviewperms);




    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(Html.fromHtml("<font><i>" + "User Roles" + "</i></font>"));
    }

    public void onStart(){
        super.onStart();
        //BackgroundTask bt=new BackgroundTask();
        //bt.execute();
    }

    public void getspinnerlistfromdatabase() {
        String method = "updatelist";
        String fieldtobeupdated = "users";

     //   BackgroundTask.m_list=null;

        BackgroundTask backgroundTask = new BackgroundTask(RolesPermissions.this, new AsyncResponse() {

            @Override
            public void processFinish(String output) {
                Log.e("invenapp:method:", new CommonProcs().getMethodname() + ":output:" + output);
                if (output.equals(StatusConstants.listretrievalSuccessful)) {

                    listItems = BackgroundTask.m_list_users;
                    adapter = new ArrayAdapter<String>(RolesPermissions.this,R.layout.support_simple_spinner_dropdown_item,listItems);
                    spinner_roles.setAdapter(adapter);


                    spinner_roles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                   int arg2, long arg3) {
                            // TODO Auto-generated method stub
                            selectedusername = spinner_roles.getSelectedItem().toString();
                            selecteduserid=Integer.parseInt(CommonProcs.bundleusers.get(selectedusername).toString());


                            Log.e("Selected item : ", selectedusername + ":" + selecteduserid);


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });




                    RolesPermissions.flagitemsupdate=true;

                    Log.e("invenapp:getlist:users", output);
                }

            }
        });
        backgroundTask.execute(method, fieldtobeupdated);
    }

    public void getcheckedlistitemsfromdatabase() {
        String method = "updatelist";
        String fieldtobeupdated= "roles";

        BackgroundTask usertask = new BackgroundTask(RolesPermissions.this, new AsyncResponse() {

            @Override
            public void processFinish(String output) {
                Log.e("invenapp:method:", new CommonProcs().getMethodname() + ":output:" + output);
                if (output.equals(StatusConstants.listretrievalSuccessful)) {

                    listItemschecked = BackgroundTask.m_list_roles;
                    adapter = new ArrayAdapter<String>(RolesPermissions.this, R.layout.list_permissionslistview, listItemschecked);
                    listview.setAdapter(adapter);
                    listview.setItemsCanFocus(false);
                    // we want multiple clicks
                    listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);



                    for (int i=0;i<listview.getCount();i++){
                        if(listview.getItemAtPosition(i).toString().equals(SessionManager.role_viewown)){
                            rolesids.add(String.valueOf(i+1));
                             positionofviewown=i+1;
                            listview.setItemChecked(i,true);
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    CheckedTextView cbview = (CheckedTextView) view;
                                    cbview.setEnabled(false);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }





                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, final View v, final int position, long id) {

                            String itemchecked=((AppCompatCheckedTextView) v).getText().toString();




        switch(itemchecked){

    case "View_Own":
        Toast.makeText(RolesPermissions.this, "Default Role:Cannot uncheck it", Toast.LENGTH_SHORT).show();
       // Log.e("invenapp", "2AppCompatCheckedTextView:" + itemchecked);
        ((CheckedTextView) v).toggle();
        listview.setItemChecked(position ,true);

      boolean isitemchecked=  listview.isItemChecked(position);
        Log.e("invenapp","isitemchecked_2:"+isitemchecked+" "+position);
        break;

    default:
        break;



}



                            if (((CheckedTextView) v).isChecked()) {
                                String key = (String) ((CheckedTextView) v).getText();
                                rolesids.add(CommonProcs.bundleroles.get(key).toString());
                                Log.e("invenapp:", CommonProcs.bundleroles.get(key) + ":" + key);
                            }

                        }
                    });


                    Log.e("invenapp:getlist:roles", output);
                }

            }
        });
        usertask.execute(method,fieldtobeupdated);
    }


    public void setuserpermissions (View view)
    {


        rolesids= new ArrayList<>();

        SparseBooleanArray listar=listview.getCheckedItemPositions();


        for (int i = 0; i < listview.getCount(); i++) {
            if (listar.get(i)) {
                rolesids.add(String.valueOf(i+1));
            }
        }




/*
for(SparseBooleanArray longroleid:listar){
    rolesids.add(String.valueOf(longroleid));
}
*/

        for (String inta:rolesids) {
            Log.e("invenapp:Checkeditems", inta);
        }
        Log.e("invenapp:selecteduserid", ""+selecteduserid);

        String selecteduseridstring= String.valueOf(selecteduserid);

             final String method = "insertonetoarraydevices";
            String table_name= StatusConstants.userroles;

        JSONArray jArr1= new JSONArray();
        for(String data:rolesids)
        {
            Log.e("invenapp:data", "" + data);
            jArr1.put(data);
        }
            BackgroundTask.jsonstringforuserroles=null;
            BackgroundTask backgroundTask = new BackgroundTask(this, new AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    if (output.equals(StatusConstants.insertionuserrolesSuccessful)) {
                        Toast.makeText(RolesPermissions.this, "Selected Permissions provided to the user:"+selectedusername, Toast.LENGTH_SHORT).show();


                        Intent newActivity = new Intent(RolesPermissions.this,UserManagementHome.class);
                        startActivity(newActivity);
                        finish();
                    }
                }
            });
            backgroundTask.execute(method,table_name,selecteduseridstring,jArr1.toString());
        rolesids=new ArrayList<>();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
