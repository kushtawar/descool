package com.cruxitech.android.descool;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kushtawar on 30/07/16.
 */
public class RoleAdapter extends ArrayAdapter<UserBean> {

    private ArrayList<UserBean> items;

    public RoleAdapter(Context context, int textViewResourceId, ArrayList<UserBean> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_permissionslistview, null);
        }


        UserBean o = items.get(position);

        Log.e("ARRAY_Logs", "" + o);



        if (o != null) {


            TextView hiddenusertypeview = (TextView) v.findViewById(R.id.hiddenuserid);

            if (hiddenusertypeview != null) {
                hiddenusertypeview.setText(o.getUniqueuserid());
            }



        }
        else
        {

          //  nodatadisplaytextview.setVisibility(View.VISIBLE);
        }


        return v;
    }
}
