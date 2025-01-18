package com.example.fyf_a;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    public void registerRenter(View view) {

        new Thread(new Runnable() {
            EditText Username = (EditText) findViewById(R.id.addressLbl);
            EditText Password = (EditText) findViewById(R.id.passwordLbl);
            EditText Confirm_Password = (EditText) findViewById(R.id.confirmpasswordLbl);
            EditText Mail = (EditText) findViewById(R.id.mailLbl);
            EditText Age = (EditText) findViewById(R.id.ageLbl);
            EditText Nationality = (EditText) findViewById(R.id.nationalityLbl);
            EditText Occupation = (EditText) findViewById(R.id.occupationLbl);
            ImageView Pass_Error = (ImageView) findViewById(R.id.pass_error);
            ImageView Pass_Error2 = (ImageView) findViewById(R.id.pass_error2);
            ImageView Name_Error = (ImageView) findViewById(R.id.name_error);

            InputStream stream = null;
            String result = null;
            Handler handler = new Handler();
            public void run() {
                if(!Username.getText().toString().isEmpty() && !Password.getText().toString().isEmpty() && !Confirm_Password.getText().toString().isEmpty()) {
                    try {
                        String query = String.format("http://192.168.56.1:9000/Android/registerRenter?name=" + Username.getText().toString() + "&mail="+Mail.getText().toString()+"&age="+Age.getText().toString()+"&nationality="+Nationality.getText().toString()+"&occupation="+Occupation.getText().toString()+"&password="+Password.getText().toString()+"&verifyPassword="+Confirm_Password.getText().toString());
                        URL url = new URL(query);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000 );
                        conn.setConnectTimeout(15000 /* milliseconds */);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.connect();
                        if(Password.getText().toString().equals(Confirm_Password.getText().toString()))
                        {
                            // sending parameters to login function
//String name, String mail, int age, String nationality, String occupation, String password, String verifyPassword
                            String params = "name="+Username.getText().toString()+"&mail="+Mail.getText().toString()+"&age="+Age.getText().toString()+"&nationality="+Nationality.getText().toString()+"&occupation="+Occupation.getText().toString()+"&password="+Password.getText().toString()+"&verifyPassword"+Confirm_Password.getText().toString();
                            Log.i("serverTest ", params);

                            OutputStream os = conn.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(os, "UTF-8"));
                            writer.write(params.toString());

                            writer.flush();
                            writer.close();
                            os.close();

                            Log.i("serverTest ", "waiting for response...");

                            //server response
                            stream = conn.getInputStream();
                            BufferedReader reader = null;
                            StringBuilder sb = new StringBuilder();
                            reader = new BufferedReader(new InputStreamReader(stream));
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                            result = sb.toString();
                            conn.disconnect();
                            //Correct code

                            if(result.contains("1"))
                            {
                                Intent intento= new Intent(RegisterActivity.this , com.example.fyf_a.MainActivity.class);
                                startActivity(intento);
                            }
                            else{
                                handler.post(new Runnable() {
                                    public void run() {
                                        if(Name_Error.getVisibility()==View.INVISIBLE){
                                            Name_Error.setVisibility(View.VISIBLE);
                                        }
                                        if(Pass_Error.getVisibility()==View.VISIBLE){
                                            Pass_Error.setVisibility(View.INVISIBLE);
                                        }
                                        if(Pass_Error2.getVisibility()==View.VISIBLE){
                                            Pass_Error2.setVisibility(View.INVISIBLE);
                                        }

                                        RegisterActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                                builder
                                                        .setMessage("The username is not available :(")
                                                        .setTitle("Find Your Flat")
                                                        .setPositiveButton("OK", null);

                                                builder.create().show();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                        else{
                            handler.post(new Runnable() {
                                public void run() {
                                    if(Pass_Error.getVisibility()==View.INVISIBLE){
                                        Pass_Error.setVisibility(View.VISIBLE);
                                    }
                                    if(Pass_Error2.getVisibility()==View.INVISIBLE){
                                        Pass_Error2.setVisibility(View.VISIBLE);
                                    }
                                    if(Name_Error.getVisibility()==View.VISIBLE){
                                        Name_Error.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    if(Username.getText().toString().isEmpty()){
                        if(Name_Error.getVisibility()==View.INVISIBLE) {
                            handler.post(new Runnable() {
                                public void run() {
                                    Name_Error.setVisibility(View.VISIBLE);}});
                        }
                    }
                    else{ handler.post(new Runnable() {
                        public void run() {
                            Name_Error.setVisibility(View.INVISIBLE);
                        }
                    });
                    }
                    if(Password.getText().toString().isEmpty()){
                        if(Pass_Error.getVisibility()==View.INVISIBLE)
                        {
                            handler.post(new Runnable() {
                                public void run() {
                                    Pass_Error.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                    else{ handler.post(new Runnable() {
                        public void run() {
                            Pass_Error.setVisibility(View.INVISIBLE);
                        }
                    });
                    }
                    if(Confirm_Password.getText().toString().isEmpty()){
                        if(Pass_Error2.getVisibility()==View.INVISIBLE)
                        {
                            handler.post(new Runnable() {
                                public void run() {
                                    Pass_Error2.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                    else{ handler.post(new Runnable() {
                        public void run() {
                            Pass_Error2.setVisibility(View.INVISIBLE);
                        }
                    });
                    }

                };
            }

        }).start();
    }


}

