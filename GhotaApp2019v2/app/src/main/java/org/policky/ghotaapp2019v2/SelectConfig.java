package org.policky.ghotaapp2019v2;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Network;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

public class SelectConfig extends AppCompatActivity {

    TextView curr_conf_text;
    ListView config_list_view;
    Button set_config_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_config);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        curr_conf_text = (TextView) findViewById(R.id.curr_conf_text);
//        curr_conf_text.setText("ahoj");

        config_list_view = (ListView) findViewById(R.id.config_list_view);
        final ArrayList<String> arrayList = new ArrayList<>();

        set_config_btn = (Button) findViewById(R.id.set_config_btn);
        set_config_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String possible_confs_html = getPossibleConfigs(arrayList);
                    parseHREFs(possible_confs_html);
                }
                catch (IOException e){ e.printStackTrace();}
            }
        });

    }

    private void parseHREFs(String htmlsite){
//        System.out.println(htmlsite);

        String[] parts = htmlsite.split("\n");
        ArrayList<String> new_parts = new ArrayList<>();

        for(String part : parts){
            if(!part.isEmpty()){
                if(part.contains("href=\"/apolicky/java")) {
                    new_parts.add(part);
                }
            }
        }

        for(String part : new_parts){
            System.out.println(part);
        }



        System.out.println(htmlsite);
    }

    String githubRawPref = "https://raw.githubusercontent.com/";

    private String getPossibleConfigs(ArrayList<String> confs) throws IOException{
        String result = null;
//        String dbURLString = "http://policky.org/android/tabor/askquery.php";

        URL url = new URL("https://github.com/apolicky/ghota_web_19/tree/master/kids");
        try(BufferedReader BR = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = BR.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            result = sb.toString();
//            curr_conf_text.setText(result);
        }

        return result;
//        parseHREFs(result);






//        try {
//            URL dbURL = new URL(dbURLString);
//            HttpURLConnection httpURLConnection = (HttpURLConnection) dbURL.openConnection();
//            httpURLConnection.setRequestMethod("POST");
//            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setDoInput(true);
//
//            BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(),"UTF-8"));
//            String postData = URLEncoder.encode("qry","UTF-8") + "=" + URLEncoder.encode("SELECT rok FROM years","UTF-8");
//
//            BW.write(postData);
//            BW.flush();
//            BW.close();
//
//            BufferedReader BR = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"iso-8859-1"));
//
//            StringBuilder SB = new StringBuilder();
//            String line = "";
//            while ((line = BR.readLine()) != null){
//                SB.append(line).append("\n");
//            }
//
//            BR.close();
//
//            result = SB.toString();
//
//            httpURLConnection.disconnect();
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            JSONArray jsonArray = new JSONArray(result);
//            confs = new ArrayList<>();
//
//            for(int i = 0; i < jsonArray.length(); i++){
//                confs.add(jsonArray.getJSONObject(i).getString("rok"));
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//

    }
}
