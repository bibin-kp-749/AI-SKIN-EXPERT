package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class symptombasedresult extends AppCompatActivity {

    TextView dname,ddescript,dtype;
    ImageView dimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptombasedresult);

        dname=(TextView) findViewById(R.id.textView52);
        ddescript=(TextView) findViewById(R.id.textView53);
        dtype=(TextView) findViewById(R.id.textView54);
        dimg=(ImageView) findViewById(R.id.imageView2);
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        dname.setText("Disease name:   "+sh.getString("name",""));
        ddescript.setText("Description:     "+sh.getString("des",""));
        dtype.setText("Type:    "+sh.getString("type",""));




        String hu = sh.getString("ip", "");
        String url = "http://" + hu + ":5000";

        String img=url+sh.getString("image","");

        Picasso.with(getApplicationContext()).load(img).into(dimg);





    }



    }

