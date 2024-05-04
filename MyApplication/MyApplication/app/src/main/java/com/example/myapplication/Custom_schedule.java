package com.example.myapplication;

import android.content.Context;
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

public class Custom_schedule extends BaseAdapter {
    String[] Schedule_id,Date,From_time,To_time ;
    private Context context;

    public Custom_schedule(Context appcontext,String[]Schedule_id,String[]Date,String[]From_time,String[]To_time)
    {
        this.context=appcontext;
        this.Schedule_id=Schedule_id;
        this.Date=Date;
        this.From_time=From_time;
        this.To_time=To_time;



    }

    @Override
    public int getCount() {
        return Schedule_id.length;
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
            gridView=inflator.inflate(R.layout.custom_schedule,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView s_date=(TextView)gridView.findViewById(R.id.textView28);
        TextView s_from=(TextView)gridView.findViewById(R.id.textView30);
        TextView s_to=(TextView)gridView.findViewById(R.id.textView32);
        Button btsched=(Button)gridView.findViewById(R.id.button6);

        btsched.setTag(Schedule_id[i]);
        btsched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);

                String hu = sh.getString("ip", "");
                String url = "http://" + hu + ":5000/user_addappointment";



                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                // response
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                          Toast.makeText(context, "Appointment done", Toast.LENGTH_LONG).show();

                                    }


                                    // }
                                    else {
                                        Toast.makeText(context, "Failed to add appointment", Toast.LENGTH_LONG).show();
                                    }

                                }    catch (Exception e) {
                                    Toast.makeText(context, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(context, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("User_id",sh.getString("lid",""));
                        params.put("Schedule_id",view.getTag().toString());

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
        });

//        s_date.setTextColor(Color.BLACK);
//        s_from.setTextColor(Color.BLACK);
//        s_to.setTextColor(Color.BLACK);


        s_date.setText(Date[i]);
        s_from.setText(From_time[i]);
        s_to.setText(To_time[i]);





        return gridView;
    }
}
