package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Custom_appointment extends BaseAdapter {
    String[] Date,From_time,To_time,Name,Qualification,Place,Appointment_id;
    private Context context;

    public Custom_appointment(Context appcontext,String[]Date,String[]From_time,String[]To_time,String[]Name,String[]Qualification,String[]Place,String[]Appointment_id)
    {
        this.context=appcontext;
        this.Appointment_id=Appointment_id;
        this.Date=Date;
        this.From_time=From_time;
        this.To_time=To_time;
        this.Name=Name;
        this.Qualification=Qualification;
        this.Place=Place;




    }

    @Override
    public int getCount() {
        return Appointment_id.length;
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
            gridView=inflator.inflate(R.layout.custom_appointment,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView a_appointid=(TextView)gridView.findViewById(R.id.textView35);
        TextView a_date=(TextView)gridView.findViewById(R.id.textView38);
        TextView a_from=(TextView)gridView.findViewById(R.id.textView43);
        TextView a_to=(TextView)gridView.findViewById(R.id.textView45);
        TextView a_name=(TextView) gridView.findViewById(R.id.textView47);
        TextView a_qualify=(TextView)gridView.findViewById(R.id.textView49);
        TextView a_place=(TextView)gridView.findViewById(R.id.textView51);


//        a_appointid.setTextColor(Color.BLACK);
//        a_date.setTextColor(Color.BLACK);
//        a_from.setTextColor(Color.BLACK);
//        a_to.setTextColor(Color.BLACK);
//        a_name.setTextColor(Color.BLACK);
//        a_qualify.setTextColor(Color.BLACK);
//        a_place.setTextColor(Color.BLACK);


        a_appointid.setText(Appointment_id[i]);
        a_date.setText(Date[i]);
        a_from.setText(From_time[i]);
        a_to.setText(To_time[i]);
        a_name.setText(Name[i]);
        a_qualify.setText(Qualification[i]);
        a_place.setText(Place[i]);


        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
        String ip=sh.getString("ip","");

//        String url="http://" + ip + ":5000/static/game/"+Photo[i]+".jpg";
//
//
//        Picasso.with(context).load(url). into(imag);

        return gridView;
    }
}
