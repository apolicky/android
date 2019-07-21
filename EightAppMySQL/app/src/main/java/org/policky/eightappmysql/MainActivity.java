package org.policky.eightappmysql;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText fruitNameET, fruitPriceET;
    AlertDialog alertDialog;
    Button searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fruitNameET = (EditText) findViewById(R.id.fruitNameEditText);
        fruitPriceET = (EditText) findViewById(R.id.priceEditText);
        searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch(view);
            }
        });
    }

    public void onSearch(View view){
        String fruitName = fruitNameET.getText().toString();
        String fruitPrice = fruitPriceET.getText().toString();

        String type = "search";

        BackgroundWorker BW = new BackgroundWorker(this);
        BW.execute(type,fruitName,fruitPrice);

    }

    private class BackgroundWorker extends AsyncTask<String,Void,String>{

        Context context;

        BackgroundWorker(Context c){
            context = c;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String dbURLString = "https://policky.org/getrows.php";

            if(type.equals("search")){
                try {
                    URL dbURL = new URL(dbURLString);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) dbURL.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(),"UTF-8"));
                    String postData = URLEncoder.encode("fruit_name","UTF-8") + "=" + URLEncoder.encode(params[1],"UTF-8") +
                            "&" + URLEncoder.encode("fruit_price","UTF-8") + "=" + URLEncoder.encode(params[2],"UTF-8");

                    BW.write(postData);
                    BW.flush();
                    BW.close();

                    BufferedReader BR = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"iso-8859-1"));

                    StringBuilder SB = new StringBuilder();
                    String line = "";
                    while ((line = BR.readLine()) != null){
                        SB.append(line).append("\n");
                    }

                    BR.close();

                    httpURLConnection.disconnect();

                    return SB.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("SEARCH DIALOG");


        }

        @Override
        protected void onPostExecute(String result) {
            alertDialog.setMessage(result);
            alertDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
