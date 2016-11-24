package com.cruxitech.android.descool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;



public class ViewMyDevices extends BaseActivity implements AsyncResponse {

    public static ProgressDialog m_ProgressDialog = null;
    private ArrayList<DeviceOrder> m_orders = null;
    private static DeviceOrderAdapter m_adapter;
SearchView search=null;

    public static String jsonstringval=null;

    ListView lv=null;

    TextView txtviewEmptymydevices=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonstringval=null;
        setContentView(R.layout.activity_view_my_devices);
        txtviewEmptymydevices=(TextView)findViewById(R.id.txtviewEmptymydevices);
        lv = (ListView)findViewById(R.id.listmydevices);


        this.getdatafromdatabase();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                      @Override
                                      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                          //Toast.makeText(getApplicationContext(), "You Clicked " + m_adapter.getItem(i).getDevuniqueid(), Toast.LENGTH_SHORT).show();
                                          Intent newActivity = new Intent(ViewMyDevices.this, ViewDevice.class);
                                          Bundle b = new CommonProcs().onClickViewDeviceList(getApplicationContext(), m_adapter, i, newActivity);
                                          b.putString("callingclass", "ViewMyDevices");
                                          newActivity.putExtras(b); //Put your id to your next Intent
                                          startActivity(newActivity);
                                          finish();


                                      }


                                  }

        );
        search=(SearchView) findViewById(R.id.searchfield);
        search.setVisibility(View.VISIBLE);
        search.setQueryHint("Search");
        //*** setOnQueryTextFocusChangeListener ***
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub


            }
        });

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                ViewMyDevices.this.m_adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub


                return false;
            }
        });



    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        startActivity(new Intent(ViewMyDevices.this, Landingpage.class));


    }



    private void getdatafromdatabase()
    {
        String method = "viewmydevices";
        BackgroundTask backgroundTask = new BackgroundTask(ViewMyDevices.this, new AsyncResponse() {

            @Override
            public void processFinish(String output) {
                Log.e("invenapp:method:", new CommonProcs().getMethodname() + ":output:" + output);
                if (output.equals(StatusConstants.statusViewMyDeviceSuccessful)) {

                    m_orders = BackgroundTask.m_orders;
                    m_adapter = new DeviceOrderAdapter(ViewMyDevices.this, R.layout.list_mydevice, m_orders);
                    lv.setAdapter(m_adapter);

                    Log.e("invenapp:viewmydevices", output);
            }else
                {
                    txtviewEmptymydevices.setText("No devices found that are tagged on your name.");
                }


     }
        });
        backgroundTask.execute(method);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        View empty = findViewById(R.id.txtviewEmptymydevices);
        ListView list = (ListView) findViewById(R.id.listmydevices);
        list.setEmptyView(empty);
    }

    public void processFinish(String output){
        Log.e("viewmydeviceslog2", output);
    }


}
