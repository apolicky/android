package org.policky.ninthappgetmysqltable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ShowTableActivity extends AppCompatActivity {

    ListView myListView;
    String[] names, prices;
    InputStream IS;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_table);

        myListView = (ListView) findViewById(R.id.myListView);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        /*BackgroundWorker BW = new BackgroundWorker(getApplicationContext());
        BW.execute("getTable");*/

        getTable();

        RowAdapter rowAdapter = new RowAdapter(getApplicationContext(), names, prices);
        myListView.setAdapter(rowAdapter);
    }

    private void getTable(){


        String dbURLString = "https://policky.org/android/copytable.php";

        try {
            URL dbURL = new URL(dbURLString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) dbURL.openConnection();
            httpURLConnection.setRequestMethod("GET");

            IS = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader BR = new BufferedReader(new InputStreamReader(IS));

            StringBuilder SB = new StringBuilder();
            String line = "";
            while ((line = BR.readLine()) != null){
                SB.append(line).append("\n");
            }

            IS.close();

            httpURLConnection.disconnect();

            result = SB.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(result);
            names = new String[jsonArray.length()];
            prices = new String[jsonArray.length()];


            for(int i = 0; i < jsonArray.length(); i++){
                names[i] = jsonArray.getJSONObject(i).getString("name");
                prices[i] = jsonArray.getJSONObject(i).getString("price");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*private class BackgroundWorker extends AsyncTask<String,Void,Void>{

        Context context;
        String result;
        InputStream IS;

        BackgroundWorker(Context c) { context = c; }


        @Override
        protected Void doInBackground(String... params) {
            String type = params[0];
            String dbURLString = "https://policky.org/android/copytable.php";

            if(type.equals("getTable")){
                try {
                    URL dbURL = new URL(dbURLString);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) dbURL.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    BufferedReader BR = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"iso-8859-1"));

                    StringBuilder SB = new StringBuilder();
                    String line = "";
                    while ((line = BR.readLine()) != null){
                        SB.append(line).append("\n");
                    }

                    BR.close();

                    httpURLConnection.disconnect();

                    result = SB.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    names = new String[jsonArray.length()];
                    prices = new String[jsonArray.length()];


                    for(int i = 0; i < jsonArray.length(); i++){
                        names[i] = jsonArray.getJSONObject(i).getString("name");
                        prices[i] = jsonArray.getJSONObject(i).getString("price");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            String dbURLString = "https://policky.org/android/copytable.php";

            try {
                URL dbURL = new URL(dbURLString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) dbURL.openConnection();
                httpURLConnection.setRequestMethod("GET");

                IS = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader BR = new BufferedReader(new InputStreamReader(IS));

                StringBuilder SB = new StringBuilder();
                String line = "";
                while ((line = BR.readLine()) != null){
                    SB.append(line).append("\n");
                }

                IS.close();

                httpURLConnection.disconnect();

                result = SB.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(result);
                names = new String[jsonArray.length()];
                prices = new String[jsonArray.length()];


                for(int i = 0; i < jsonArray.length(); i++){
                    names[i] = jsonArray.getJSONObject(i).getString("name");
                    prices[i] = jsonArray.getJSONObject(i).getString("price");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}
