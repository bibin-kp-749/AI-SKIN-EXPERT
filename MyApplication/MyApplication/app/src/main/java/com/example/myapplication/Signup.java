package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    EditText edname, edemail, edphone, edage, edplace, edpassword, edconfirmpassword;
    RadioButton rdmale, rdfemale, rdothers;
    Button btsignup;
    ImageView im;
    String path, atype, fname, attach="none", attatch1;
    byte[] byteArray = null;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edname = (EditText) findViewById(R.id.editTextTextPersonName);
        edemail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        edphone = (EditText) findViewById(R.id.editTextPhone);
        rdmale = (RadioButton) findViewById(R.id.radioButton4);
        rdfemale = (RadioButton) findViewById(R.id.radioButton);
        rdothers = (RadioButton) findViewById(R.id.radioButton2);
        edage = (EditText) findViewById(R.id.editTextNumber2);
        edplace = (EditText) findViewById(R.id.editTextTextPersonName5);
        edpassword = (EditText) findViewById(R.id.editTextTextPassword);
        edconfirmpassword = (EditText) findViewById(R.id.editTextTextPassword2);
        btsignup = (Button) findViewById(R.id.button);

        btsignup.setOnClickListener(this);
        im=findViewById(R.id.imageView6);
        im.setOnClickListener(this);

    }

    String gender="";

    @Override
    public void onClick(View view) {

        if(view==im){
            showfilechooser(1);
        }

        else {


            if (rdmale.isChecked()) {
                gender = "Male";
            } else if (rdfemale.isChecked()) {
                gender = "Female";
            } else {
                gender = "Other";
            }

            String name = edname.getText().toString();
            String email = edemail.getText().toString();
            String phone = edphone.getText().toString();
            String age = edage.getText().toString();
            String place = edplace.getText().toString();
            String password = edpassword.getText().toString();
            String confirmpassword = edconfirmpassword.getText().toString();

            if (name.length() == 0) {
                edname.setError("Missing");
            } else if (email.length() == 0) {
                edemail.setError("Missing");
            } else if (phone.length() == 0) {
                edphone.setError("Missing");
            } else if (age.length() == 0) {
                edage.setError("Missing");
            } else if (place.length() == 0) {
                edplace.setError("Missing");
            } else if (password.length() == 0) {
                edpassword.setError("Missing");
            } else if (confirmpassword.length() == 0) {
                edconfirmpassword.setError("Missing");
            }

            else if (!password.equals(confirmpassword)){

                edconfirmpassword.setError("Password Missmatch");

            }
            else if(attach=="none"){

                Toast.makeText(this, "Upload a Photo", Toast.LENGTH_SHORT).show();

            }


            else {


                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                String hu = sh.getString("ip", "");
                String url = "http://" + hu + ":5000/usersignup";


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


                                        Toast.makeText(Signup.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception e) {
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


                        params.put("Name", name);
                        params.put("Email", email);
                        params.put("Phone_No", phone);
                        params.put("Age", age);
                        params.put("Gender", gender);
                        params.put("Place", place);
                        params.put("Photo", attach);
                        params.put("password", password);


                        return params;
                    }
                };

                int MY_SOCKET_TIMEOUT_MS = 100000;

                postRequest.setRetryPolicy(new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(postRequest);

            }
        }

    }
    void showfilechooser(int string) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //getting all types of files

        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), string);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                ////
                Uri uri = data.getData();

                try {
                    path = FileUtils.getPath(this, uri);

                    File fil = new File(path);
                    float fln = (float) (fil.length() / 1024);
                    atype = path.substring(path.lastIndexOf(".") + 1);


                    fname = path.substring(path.lastIndexOf("/") + 1);
//                    ed15.setText(fname);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                try {

                    File imgFile = new File(path);

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        im.setImageBitmap(myBitmap);

                    }


                    File file = new File(path);
                    byte[] b = new byte[8192];
                    Log.d("bytes read", "bytes read");

                    InputStream inputStream = new FileInputStream(file);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    int bytesRead = 0;

                    while ((bytesRead = inputStream.read(b)) != -1) {
                        bos.write(b, 0, bytesRead);
                    }
                    byteArray = bos.toByteArray();

                    String str = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    attach = str;


                } catch (Exception e) {
                    Toast.makeText(this, "String :" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }

                ///

            }
        }

    }
}