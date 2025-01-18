package com.example.fyf_a;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class QueriesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queries);
    }
    public void getFlatOverallRating(View view) {
        new Thread(new Runnable() {
            EditText flatAddress = (EditText) findViewById(R.id.addressLbl);
            InputStream stream = null;
            String result = null;
            Handler handler = new Handler();

            public void run() {
                if (!flatAddress.getText().toString().isEmpty()) {
                    try {
                        String address = flatAddress.getText().toString();
                        String query = String.format("http://192.168.56.1:9000/Android/getFlatOverallRating?address=%s", URLEncoder.encode(address, "UTF-8"));
                        URL url = new URL(query);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(15000); // milliseconds
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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(QueriesActivity.this);
                                builder.setMessage("The average rating for this flat is: " + result)
                                        .setTitle("Find Your Flat")
                                        .setPositiveButton("OK", null);

                                builder.create().show();
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(QueriesActivity.this);
                            builder.setMessage("Please enter a flat address")
                                    .setTitle("Error")
                                    .setPositiveButton("OK", null);

                            builder.create().show();
                        }
                    });
                }
            }
        }).start();
    }

}
