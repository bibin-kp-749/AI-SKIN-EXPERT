package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ipsettings extends AppCompatActivity implements View.OnClickListener {

    EditText edip;
    Button btsavesettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipsettings);

        edip=(EditText) findViewById(R.id.editTextTextPersonName7);
        btsavesettings=(Button) findViewById(R.id.button5);

        btsavesettings.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String ip =edip.getText().toString();

        if (ip.length()==0)
        {
            edip.setError("Missing");
        }
        else
        {
            SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor ed=sh.edit();
            ed.putString("ip",ip);
            ed.commit();


                Intent ins=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(ins);



        }

    }
}