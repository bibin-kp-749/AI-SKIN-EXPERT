package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class viewprofile extends AppCompatActivity implements View.OnClickListener {

   ImageView img;
   TextView tvname,tvgender,tvage,tvplace,tvemail,tvphone;
   Button btedp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);

        img=(ImageView) findViewById(R.id.imageView);
        tvname=(TextView) findViewById(R.id.textView7);
        tvgender=(TextView) findViewById(R.id.textView9);
        tvage=(TextView) findViewById(R.id.textView10);
        tvplace=(TextView) findViewById(R.id.textView13);
        tvemail=(TextView) findViewById(R.id.textView14);
        tvphone=(TextView) findViewById(R.id.textView15);
        btedp=(Button) findViewById(R.id.button11);

        btedp.setOnClickListener(this);


        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String hu = sh.getString("ip", "");
        String url = "http://" + hu + ":5000/user_viewprofile";



        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        // response
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                               JSONObject jo =jsonObj.getJSONObject("data");
                               tvname.setText(jo.getString("Name"));
                               tvgender.setText(jo.getString("Gender"));
                               tvage.setText(jo.getString("Age"));
                               tvplace.setText(jo.getString("Place"));
                               tvemail.setText(jo.getString("Email"));
                               tvphone.setText(jo.getString("Phone_No"));
                               String pic= jo.getString("Photo");
                                String url1 = "http://" + hu + ":5000"+pic;



                                Picasso.with(getApplicationContext()).load(url1).into(img);
                            }


                            // }
                            else {
                                Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                            }

                        }    catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();

params.put("lid",sh.getString("lid",""));


                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS=100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);









    }

    @Override
    public void onClick(View view) {

        Intent ins=new Intent(getApplicationContext(),editprofile.class);
        startActivity(ins);



    }
}