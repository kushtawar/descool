package com.cruxitech.android.descool;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewAllDevices extends BaseActivity implements AsyncResponse {

    public static ProgressDialog m_ProgressDialog = null;
    private ArrayList<DeviceOrder> m_orders = null;
    private static DeviceOrderAdapter m_adapter;
    TextView txtviewEmptymydevices=null;
    public static String jsonstringval=null;
    Toolbar mToolbar;
    ListView lv=null;
    EditText inputSearch;
    SearchView search=null;
    TextView mTitle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_devices);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(Html.fromHtml("<font><i>" + "All Devices" + "</i></font>"));
        jsonstringval=null;
         lv = (ListView)findViewById(R.id.list1);
        txtviewEmptymydevices=(TextView)findViewById(R.id.txtviewEmpty);

        this.getdatafromdatabase();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                      @Override
                                      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                          Intent newActivity = new Intent(ViewAllDevices.this, ViewDevice.class);

                                          Bundle b = new CommonProcs().onClickViewDeviceList(getApplicationContext(), m_adapter, i, newActivity);
                                          b.putString("callingclass", "ViewAllDevices");
                                          newActivity.putExtras(b); //Put your id to your next Intent

                                          startActivity(newActivity);
                                          finish();


                                      }


                                  }


        );



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(ViewAllDevices.this, Home.class));


    }



    private void getdatafromdatabase()
    {
        String method = "viewalldevices";
        BackgroundTask.jsonstring=null;
        BackgroundTask backgroundTask = new BackgroundTask(this, new AsyncResponse() {

            @Override
            public void processFinish(String output) {
                if (output.equals(StatusConstants.statusViewAllDevicesSuccessful)) {

                m_orders = BackgroundTask.m_orders;
                m_adapter = new DeviceOrderAdapter(ViewAllDevices.this, R.layout.list_alldevices, m_orders);
                lv.setAdapter(m_adapter);
                    lv.setTextFilterEnabled(true);
//m_orders=BackgroundTask.m_orders;
                Log.e("viewalldeviceslog",output);
                }else
                {
                    txtviewEmptymydevices.setText("No devices found ");
                }
            }
        });
        backgroundTask.execute(method);


    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        View empty = findViewById(R.id.txtviewEmpty);
        ListView list = (ListView) findViewById(R.id.list1);
        list.setEmptyView(empty);
    }

    public void processFinish(String output){
        Log.e("viewalldeviceslog2", output);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu


         final MenuItem menuItem = menu.findItem(R.id.menu_search);
        final SearchView search = (SearchView) MenuItemCompat.getActionView(menuItem);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        EditText searchEditText = (EditText) search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.parseColor("#FFFFFF"));
        // searchEditText.setHintTextColor(getResources().getColor(R.color.white));



       // search.setMaxWidth(200);
        search.setVisibility(View.VISIBLE);

        search.setQueryHint("Search");
        //*** setOnQueryTextFocusChangeListener ***
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub


                Log.d("invenapp", "onFocusChange()" + hasFocus);
                if ((hasFocus) || (((SearchView) v).getQuery().toString().trim().length()>0) ){
                    mTitle.setVisibility(View.GONE);
                }
                /*else
                {
                    mTitle.setVisibility(View.VISIBLE);
                }*/


            }
        });






        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                ViewAllDevices.this.m_adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub

                if (newText.length() < 1) {
                    ViewAllDevices.this.m_adapter.getFilter().filter(null);
                }

                return false;
            }
        });

search.setOnCloseListener(new SearchView.OnCloseListener() {
    @Override
    public boolean onClose() {
        ViewAllDevices.this.m_adapter.getFilter().filter(null);
        mTitle.setVisibility(View.VISIBLE);
       // mTitle.setText(Html.fromHtml("<font><i>" + "All Devices" + "</i></font>"));
        return false;
    }


});






        return true;
    }


    @Override
    public void refresh() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);

    }


}
