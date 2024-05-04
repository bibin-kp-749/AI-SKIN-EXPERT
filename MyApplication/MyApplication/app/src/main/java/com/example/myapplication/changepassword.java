package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class changepassword extends AppCompatActivity implements View.OnClickListener {


    EditText edoldpassword,ednewpassword,edconfirmpassword  ;

    Button btchangepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        edoldpassword=(EditText) findViewById(R.id.editTextTextPersonName4);
        ednewpassword=(EditText) findViewById(R.id.editTextTextPersonName6);
        edconfirmpassword=(EditText) findViewById(R.id.editTextTextPersonName8);
        btchangepassword=(Button) findViewById(R.id.button3);

        btchangepassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String oldpassword=edoldpassword.getText().toString();
        String newpassword=ednewpassword.getText().toString();
        String confirmpassword=edconfirmpassword.getText().toString();

        if(oldpassword.length()==0)
        {
            edoldpassword.setError("Missing");
        }
        else if(newpassword.length()==0)
        {
            ednewpassword.setError("Missing");
        }
        else if(confirmpassword.length()==0)
        {
            edconfirmpassword.setError("Missing");
        }
        else
        {

            SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            String hu = sh.getString("ip", "");
            String url = "http://" + hu + ":5000/user_changepassword";



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

                                    Toast.makeText(getApplicationContext(),"Password changed successfully",Toast.LENGTH_SHORT).show();

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
                    params.put("newpassword",newpassword);

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