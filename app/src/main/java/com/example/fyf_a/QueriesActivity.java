package com.example.fyf_a;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class QueriesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queries);

        String username = getIntent().getStringExtra("Username");

        TextView welcomeTextView = findViewById(R.id.textView2);
        String welcomeMessage = "Welcome " + username + ", please select any query";
        welcomeTextView.setText(welcomeMessage);
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
    public void getRatersList(View view) {
        new Thread(new Runnable() {
            EditText flatAddress = (EditText) findViewById(R.id.addressLbl);
            ListView ratersListView = (ListView) findViewById(R.id.ratersList);
            TextView ratersHeader = (TextView) findViewById(R.id.ratersHeader);
            InputStream stream = null;
            String result = null;
            Handler handler = new Handler();

            public void run() {
                if (!flatAddress.getText().toString().isEmpty()) {
                    try {
                        String address = flatAddress.getText().toString();
                        String query = String.format("http://192.168.56.1:9000/Android/getRatersList?address=%s", URLEncoder.encode(address, "UTF-8"));
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
                                if (result.equals("Flat not found") || result.equals("No ratings found for this flat")) {
                                    ratersHeader.setVisibility(View.GONE); // Oculta el encabezado
                                    Toast.makeText(QueriesActivity.this, result, Toast.LENGTH_LONG).show();
                                } else {
                                    try {
                                        JSONArray jsonArray = new JSONArray(result);
                                        List<String> raters = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            raters.add(jsonObject.getString("name"));
                                        }

                                        // Mostrar el encabezado y la lista
                                        ratersHeader.setVisibility(View.VISIBLE); // Muestra el encabezado
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(QueriesActivity.this, android.R.layout.simple_list_item_1, raters);
                                        ratersListView.setAdapter(adapter);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        ratersHeader.setVisibility(View.GONE);
                                        Toast.makeText(QueriesActivity.this, "Error parsing data", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ratersHeader.setVisibility(View.GONE);
                            Toast.makeText(QueriesActivity.this, "Please enter a flat address", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }


}
