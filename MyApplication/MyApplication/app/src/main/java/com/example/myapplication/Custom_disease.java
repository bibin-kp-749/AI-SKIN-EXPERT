package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Custom_disease extends BaseAdapter {
    String[] Disease_id,Disease_Name,Image,Description,Type;

    private Context context;

    public Custom_disease(Context appcontext, String[]Disease_id, String[]Disease_Name, String[]Image, String[]Description, String[]Type)
    {
        this.context=appcontext;
        this.Disease_id=Disease_id;
        this.Disease_Name=Disease_Name;
        this.Image=Image;
        this.Description=Description;
        this.Type=Type;



    }

    @Override
    public int getCount() {
        return Disease_id.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(view==null)
        {
            gridView=new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView=inflator.inflate(R.layout.custom_disease,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView disease_name=(TextView)gridView.findViewById(R.id.textView36);
        TextView disease_description=(TextView)gridView.findViewById(R.id.textView39);
        TextView disease_type=(TextView)gridView.findViewById(R.id.textView40);
        ImageView im=(ImageView) gridView.findViewById(R.id.imageView3);
//
//        disease_name.setTextColor(Color.BLACK);
//        disease_description.setTextColor(Color.BLACK);
//        disease_type.setTextColor(Color.BLACK);


        disease_name.setText(Disease_Name[i]);
        disease_description.setText(Description[i]);
        disease_type.setText(Type[i]);


        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
        String ip=sh.getString("ip","");

        String url="http://" + ip + ":5000"+Image[i];


        Picasso.with(context).load(url). into(im);

        return gridView;
    }
}
