package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class doctorappointment extends AppCompatActivity {

    ListView lv;

    String [] Date,From_time,To_time,Name,Qualification,Place,Appointment_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorappointment);

        lv=(ListView) findViewById(R.id.lv);

        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String hu = sh.getString("ip", "");
        String url = "http://" + hu + ":5000/user_viewappointment";



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

                                JSONArray ja= jsonObj.getJSONArray("data");

                                Appointment_id=new String[ja.length()];
                                Date=new String[ja.length()];
                                Name=new String[ja.length()];
                                From_time=new String[ja.length()];
                                To_time=new String[ja.length()];
                                Qualification=new String[ja.length()];
                                Place=new String[ja.length()];


                                for(int i=0;i<ja.length();i++)
                                {
                                    Appointment_id[i]= ja.getJSONObject(i).getString("Appointment_id");
                                    Date[i]= ja.getJSONObject(i).getString("Date");
                                    Name[i]= ja.getJSONObject(i).getString("Name");
                                    From_time[i]= ja.getJSONObject(i).getString("From_time");
                                    To_time[i]= ja.getJSONObject(i).getString("To_time");
                                    Qualification[i]= ja.getJSONObject(i).getString("Qualification");
                                    Place[i]= ja.getJSONObject(i).getString("Place");






                                }




                                lv.setAdapter(new Custom_appointment(getApplicationContext(), Date,From_time,To_time,Name,Qualification,Place,Appointment_id));


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
}