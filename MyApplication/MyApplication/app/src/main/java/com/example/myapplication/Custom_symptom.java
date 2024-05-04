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

public class Custom_symptom extends BaseAdapter {
    String[] Symptoms;
    private Context context;

    public Custom_symptom(Context appcontext,  String[]Symptoms)
    {
        this.context=appcontext;

        this.Symptoms=Symptoms;


    }

    @Override
    public int getCount() {
        return Symptoms.length;
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
            gridView=inflator.inflate(R.layout.custom_symptom,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView symp=(TextView)gridView.findViewById(R.id.textView34);


//        symp.setTextColor(Color.BLACK);


        symp.setText(Symptoms[i]);


        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
        String ip=sh.getString("ip","");



        return gridView;
    }
}
