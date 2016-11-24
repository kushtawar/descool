package com.cruxitech.android.descool;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by kushtawar on 30/07/16.
 */
public class HomeAdapter extends ArrayAdapter<String> {

    ArrayList<Integer> homecatimg=null;
    ArrayList<String> homecatname=null;

    Context ctx;
    LayoutInflater inflater;

    public HomeAdapter(Context context, ArrayList<Integer> homecatimg,ArrayList<String> homecatname) {
        super(context, R.layout.list_homepage, homecatname);

        this.ctx=context;
        this.homecatimg=homecatimg;
        this.homecatname=homecatname;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_homepage, null);
        }

        ImageView homecategoryimg=(ImageView)convertView.findViewById(R.id.homecategoryimage);
        TextView homecategoryname = (TextView) convertView.findViewById(R.id.homecategoryname);
      //  Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.image);


        /*if(homecategoryimg != null) {
            ((BitmapDrawable)homecategoryimg.getDrawable()).getBitmap().recycle();
        }
        homecategoryimg = (ImageView) convertView.findViewById(R.id.homecategoryimage);*/



        Log.e("invenapp", "position:" + position);


     //   File imgFile = new  File(""+homecatimg.get(position));
     //   Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
     //   homecategoryimg.setImageBitmap(myBitmap);

        homecategoryimg.setImageBitmap(null);
        homecategoryimg.setImageResource(homecatimg.get(position));

        homecategoryname.setText(homecatname.get(position));


        return convertView;
    }




    public static Drawable getAssetImage(Context context, String filename) throws IOException {
        AssetManager assets = context.getResources().getAssets();
        InputStream buffer = new BufferedInputStream((assets.open("drawable/" + filename + ".png")));
        Bitmap bitmap = BitmapFactory.decodeStream(buffer);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

}
