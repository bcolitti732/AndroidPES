package com.example.fyf_a;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("testProjecte", "Starting app");
    }

    public void goToRegisterActivity(View vista) {
        Intent intento = new Intent(this, RegisterActivity.class);
        startActivity(intento);
    }

    public void Login(View view) {
        new Thread(new Runnable() {
            EditText Username = (EditText) findViewById(R.id.addressLbl);
            EditText Password = (EditText) findViewById(R.id.passwordLbl);
            InputStream stream = null;
            String result = null;
            Handler handler = new Handler();

            public void run() {
                if (!Username.getText().toString().isEmpty() && !Password.getText().toString().isEmpty()) {
                    try {
                        String query = String.format("http://192.168.56.1:9000/Android/logInRenter?name=" + Username.getText().toString() + "&password=" + Password.getText().toString());
                        URL url = new URL(query);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(15000);
                        conn.setRequestMethod("GET");
                        conn.setDoInput(true);
                        conn.connect();
                        stream = conn.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                        result = sb.toString();
                        conn.disconnect();

                        Log.i("testProjecte", "result value is" + result);
                        if (result.equals("1")) {
                                //Para mandar parametros de una activity a otra
                                Intent intento= new Intent( MainActivity.this , QueriesActivity.class);
                                intento.putExtra("Username",Username.getText().toString());
                                startActivity(intento);
                        } else {
                            handler.post(new Runnable() {
                                public void run() {
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setMessage("Username or password not found")
                                                    .setTitle("Find Your Flat")
                                                    .setPositiveButton("OK", null);
                                            builder.create().show();
                                        }
                                    });
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Please fill in all fields")
                                    .setTitle("FurHub")
                                    .setPositiveButton("OK", null);
                            builder.create().show();
                        }
                    });
                }
            }
        }).start();
    }
}