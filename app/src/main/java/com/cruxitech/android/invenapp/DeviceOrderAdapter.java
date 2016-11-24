package com.cruxitech.android.invenapp;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kushtawar on 30/07/16.
 */
public class DeviceOrderAdapter extends BaseAdapter implements Filterable {

    private ArrayList<DeviceOrder> items;
    private ArrayList<DeviceOrder> filterList;
    private LayoutInflater mInflater;
    private Context context;
    CustomFilter filter;
    //private ItemFilter mFilter = new ItemFilter();

    public DeviceOrderAdapter(Context context,int textViewResourceId,  ArrayList<DeviceOrder> items) {
       // super(context, textViewResourceId, items);
        this.filterList=items;
        this.context=context;
        this.items = items;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position){
        return items.get(position);
    }

    @Override
    public long getItemId(int position){
        return items.indexOf(getItem(position));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(com.cruxitech.android.invenapp.R.layout.list_alldevices, null);
        }


        DeviceOrder o = items.get(position);

        Log.e("ARRAY_Logs", "" + o);



        if (o != null) {
           // ListView lv = (ListView)v.findViewById(R.id.list1);
       //     TextView nodatadisplaytextview = (TextView) v.findViewById(R.id.txtviewEmpty);
     //       lv.setEmptyView(nodatadisplaytextview);

            TextView devtypeview = (TextView) convertView.findViewById(com.cruxitech.android.invenapp.R.id.devtype);
            TextView devnoview = (TextView) convertView.findViewById(com.cruxitech.android.invenapp.R.id.devno);
            TextView devownerview = (TextView) convertView.findViewById(com.cruxitech.android.invenapp.R.id.devowner);
            TextView devlocation = (TextView) convertView.findViewById(R.id.devlocation);
            TextView devmanufacturer = (TextView) convertView.findViewById(R.id.devmanufacturer);
            TextView devlastupdatedby = (TextView) convertView.findViewById(R.id.lastupdatedby);
            TextView devlastupdatedon = (TextView) convertView.findViewById(R.id.lastupdatedon);
            ImageView imgdevtype=(ImageView)convertView.findViewById(R.id.icondevtype);





            if (devtypeview != null) {
                devtypeview.setText(o.getDevtype());

                switch(o.getDevtype().toString().trim().toLowerCase()){

                    case "laptop":
                        imgdevtype.setImageResource(R.drawable.laptop_1);
                        break;
                    case "mobile":
                        imgdevtype.setImageResource(R.drawable.mobile);
                        break;
                    case "server":
                        imgdevtype.setImageResource(R.drawable.server);
                        break;
                    case "tablet":
                        imgdevtype.setImageResource(R.drawable.tablet);
                        break;
                    case "others":
                    default:
                        imgdevtype.setImageResource(R.drawable.others);
                        break;



                }

            }

            if (devnoview != null) {
                devnoview.setText(o.getDevno());
            }
            if (devownerview != null) {
                devownerview.setText(o.getDevowner());
            }
////

            if (devlocation != null) {
                devlocation.setText(o.getDevlocation());
            }
            if (devmanufacturer != null) {
                devmanufacturer.setText(o.getDevmanufacturer());
            }

            if (devlastupdatedby != null) {
                devlastupdatedby.setText(o.getLastupdatedby());
            }

            if (devlastupdatedon != null) {
                devlastupdatedon.setText(o.getLastupdatedon());
            }

        }
        else
        {

          //  nodatadisplaytextview.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new CustomFilter();
        }

        return filter;
    }

    //INNER CLASS

    class CustomFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results=new FilterResults();

            if(constraint != null && constraint.length()>0){
                constraint=constraint.toString().toUpperCase();
                ArrayList<DeviceOrder> filters= new ArrayList<DeviceOrder>();

                for (int i=0;i<filterList.size();i++){

                    if((filterList.get(i).getDevtype().toUpperCase().contains(constraint)) ||
                    (filterList.get(i).getDevlocation().toUpperCase().contains(constraint))||
                            (filterList.get(i).getDevmanufacturer().toUpperCase().contains(constraint))||
                            (filterList.get(i).getDevno().toUpperCase().contains(constraint))||
                            (filterList.get(i).getDevowner().toUpperCase().contains(constraint))||
                            (filterList.get(i).getDevicemodel().toUpperCase().contains(constraint))||
                            (filterList.get(i).getLastupdatedby().toUpperCase().contains(constraint))||
                            (filterList.get(i).getLastupdatedon().toUpperCase().contains(constraint)))

                            {
                        filters.add(filterList.get(i));
                    }
                }

                if(filters.size()>0){

                results.count=filters.size();
                results.values=filters;
                }else
                {
                   /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                        alertDialogBuilder.setMessage("No match found");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();*/
                    Toast toast=Toast.makeText(context, "No match found", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }


            }else
            {
                results.count=filterList.size();
                results.values=filterList;
            }

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.count == 0) {
//                filterList.add()
//                notifyDataSetInvalidated();
            } else {
                items = (ArrayList<DeviceOrder>) results.values;
                notifyDataSetChanged();
            }



        }
    }



}
