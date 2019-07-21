package org.policky.taborovyzpevnik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class AvailibleYears extends AppCompatActivity {

    List<String> downloadedZpevnikYears;
    List<String> years;
    ListView availibleListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dostupne_zpevniky);

        availibleListView = (ListView) findViewById(R.id.availibleListView);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        downloadedZpevnikYears = new ArrayList<String>();
        getDownloadedZpevniks();
        getOnlineZpevniks();
        getPossibleZpevniks();

        DownloadDeleteYearAdapter downloadDeleteYearAdapter = new DownloadDeleteYearAdapter(
                getApplicationContext(),
                years,
                ButtonType.DOWNLOAD,
                getFilesDir());
        availibleListView.setAdapter(downloadDeleteYearAdapter);

    }

    private void getPossibleZpevniks(){
        for(String downlYear : downloadedZpevnikYears){
            if(years.contains(downlYear)){
                years.remove(downlYear);
            }
        }
    }

    private void getDownloadedZpevniks(){
        File rootDirectory = getFilesDir();

        for(File f : rootDirectory.listFiles()){
            if(f.isDirectory()){
                downloadedZpevnikYears.add(f.getName());
            }
        }

    }

    private void getOnlineZpevniks(){
        String result = null;
        String dbURLString = "http://policky.org/android/tabor/askquery.php";
        try {
            URL dbURL = new URL(dbURLString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) dbURL.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(),"UTF-8"));
            String postData = URLEncoder.encode("qry","UTF-8") + "=" + URLEncoder.encode("SELECT rok FROM years","UTF-8");

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

            result = SB.toString();

            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(result);
            years = new ArrayList<>();

            for(int i = 0; i < jsonArray.length(); i++){
                years.add(jsonArray.getJSONObject(i).getString("rok"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
