package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityHomeBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {


    ImageView img;
    TextView tvname,tvgender,tvage,tvplace,tvemail,tvphone,tvedit;
    Button btedp;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
//        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setItemIconTintList(null);


        img=(ImageView) findViewById(R.id.imageView);
        tvname=(TextView) findViewById(R.id.textView7);
        tvgender=(TextView) findViewById(R.id.textView9);
        tvage=(TextView) findViewById(R.id.textView10);
        tvplace=(TextView) findViewById(R.id.textView13);
        tvemail=(TextView) findViewById(R.id.textView14);
        tvphone=(TextView) findViewById(R.id.textView15);
        tvedit=(TextView) findViewById(R.id.textView41);

        tvedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in=new Intent(getApplicationContext(),editprofile.class);
                startActivity(in);



            }
        });


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
                                tvgender.setText("Am "+jo.getString("Gender"));
                                tvage.setText("Age:"+jo.getString("Age"));
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


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.nav_home)
                {
                    Intent ins=new Intent(getApplicationContext(),Home.class);
                    startActivity(ins);
                }

                else if(item.getItemId()==R.id.nav_viewdoctor)
                {
                    Intent ins=new Intent(getApplicationContext(),viewdoctor.class);
                    startActivity(ins);
                }
                else if(item.getItemId()==R.id.nav_doctorappointment)
                {
                    Intent ins=new Intent(getApplicationContext(),doctorappointment.class);
                    startActivity(ins);
                }
                else if(item.getItemId()==R.id.nav_viewdiseases)
                {
                    Intent ins=new Intent(getApplicationContext(),viewdisease.class);
                    startActivity(ins);
                }

                else if(item.getItemId()==R.id.nav_predictimage)
                {
                    Intent ins=new Intent(getApplicationContext(),predictdiseasebyimage.class);
                    startActivity(ins);
                }
                else if(item.getItemId()==R.id.nav_predictsymptom)
                {
                    Intent ins=new Intent(getApplicationContext(),predictdiseaseusingsymptom.class);
                    startActivity(ins);
                }
                else if(item.getItemId()==R.id.nav_changepassword)
                {
                    Intent ins=new Intent(getApplicationContext(),changepassword.class);
                    startActivity(ins);
                }
                else if(item.getItemId()==R.id.nav_logout)
                {
                    Intent ins=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(ins);
                }  else if(item.getItemId()==R.id.nav_feedback)
                {
                    Intent ins=new Intent(getApplicationContext(),sendfeedback.class);
                    startActivity(ins);
                }






                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }




}