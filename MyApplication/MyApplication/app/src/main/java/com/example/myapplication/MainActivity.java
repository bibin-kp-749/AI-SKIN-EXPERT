package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edname,edpassword;
    Button btlogin;

    TextView tvsignup;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edname=(EditText) findViewById(R.id.editTextTextPersonName3);
        edpassword=(EditText) findViewById(R.id.editTextTextPassword5);
        btlogin=(Button) findViewById(R.id.button4);
        tvsignup=(TextView) findViewById(R.id.textView6);

        tvsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ins= new Intent(getApplicationContext(),Signup.class);
                startActivity(ins);
            }
        });

        btlogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String name=edname.getText().toString();
        String password=edpassword.getText().toString();

        if(name.length()==0)
        {
            edname.setError("Missing");
        }
        else if(password.length()==0)
        {
            edpassword.setError("Missing");
        }
        else
        {
            SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            String hu = sh.getString("ip", "");
            String url = "http://" + hu + ":5000/user_login";



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

                                    String lid= jsonObj.getString("lid");


                                    SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor ed=sh.edit();
                                    ed.putString("lid",lid);
                                    ed.commit();


                                    String type= jsonObj.getString("type");

                                    if(type.equalsIgnoreCase("user"))
                                    {
                                        Intent ins= new Intent(getApplicationContext(),Home.class);
                                        startActivity(ins);
                                    }
                                    else if(type.equalsIgnoreCase("doctor"))
                                    {
                                        Intent ins=new Intent(getApplicationContext(),Doctorhome.class);
                                        startActivity(ins);
                                    }

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


                    params.put("Username",name);
                    params.put("Passwword",password);

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



    }

        }

