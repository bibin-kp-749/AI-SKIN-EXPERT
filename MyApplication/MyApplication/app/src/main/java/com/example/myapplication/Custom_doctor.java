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

public class Custom_doctor extends BaseAdapter {
    String[] Doctor_id,Login_id,Name,Email,Phone_No,Qualification,Gender,Experiance,Place,Photo;
//    ImageView Photo;

    private Context context;

    public Custom_doctor(Context appcontext,String[]Doctor_id,String[]Login_id,String[]Name,String[]Email,String[]Phone_No,String[]Qualification,String[]Gender,String[]Experiance,String[]Place,String[] Photo)
    {
        this.context=appcontext;
        this.Doctor_id=Doctor_id;
        this.Login_id=Login_id;
        this.Name=Name;
        this.Email=Email;
        this.Phone_No=Phone_No;
        this.Qualification=Qualification;
        this.Gender=Gender;
        this.Experiance=Experiance;
        this.Place=Place;
        this.Photo=Photo;


    }

    @Override
    public int getCount() {
        return Doctor_id.length;
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
            gridView=inflator.inflate(R.layout.custom_doctor,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView doctor_name=(TextView)gridView.findViewById(R.id.textView11);
        TextView d_gender=(TextView)gridView.findViewById(R.id.textView22);
        TextView d_email=(TextView)gridView.findViewById(R.id.textView16);
        TextView d_phone=(TextView)gridView.findViewById(R.id.textView18);
        TextView d_qualify=(TextView)gridView.findViewById(R.id.textView20);
        TextView d_experience=(TextView)gridView.findViewById(R.id.textView24);
        TextView d_place=(TextView)gridView.findViewById(R.id.textView26);
        ImageView d_photo=(ImageView)gridView.findViewById(R.id.imageView5);
        Button btschedule=(Button) gridView.findViewById(R.id.button7);

        btschedule.setTag(Login_id[i]);

        btschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("dlid", view.getTag().toString());
                ed.commit();

                Intent ins= new Intent(context,viewdoctorschedule.class);
                ins.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ins);


            }
        });


//        doctor_name.setTextColor(Color.BLACK);
//        d_gender.setTextColor(Color.BLACK);
//        d_email.setTextColor(Color.BLACK);
//        d_phone.setTextColor(Color.BLACK);
//        d_qualify.setTextColor(Color.BLACK);
//        d_experience.setTextColor(Color.BLACK);
//        d_place.setTextColor(Color.BLACK);
//          d_photo.setTextColor(Color.Black);


        doctor_name.setText(Name[i]);
        d_gender.setText(Gender[i]);
        d_email.setText(Email[i]);
        d_phone.setText(Phone_No[i]);
        d_qualify.setText(Qualification[i]);
        d_experience.setText(Experiance[i]);
        d_place.setText(Place[i]);
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);

        String hu = sh.getString("ip", "");
        String url1 = "http://" + hu + ":5000"+Photo[i];



        Picasso.with(context).load(url1).into(d_photo);




        return gridView;
    }
}
