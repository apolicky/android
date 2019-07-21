package org.policky.taborovyzpevnik;

import android.content.Context;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class DownloadDeleteYearAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    List<String> years;
    Context context;
    ButtonType textOfButton;
    File rootDirectory;


    DownloadDeleteYearAdapter(Context c, List<String> y, ButtonType t, File rD){
        context = c;
        years = y;
        textOfButton = t;
        rootDirectory = rD;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    @Override
    public int getCount() {
        return years.size();
    }

    @Override
    public Object getItem(int i) {
        return years.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = (View) mInflater.inflate(R.layout.stahnout_layout, null);

        final TextView yearTextView = (TextView) v.findViewById(R.id.zpevnikYearTextView);
        yearTextView.setText(years.get(i));

        Button downDelButton = (Button) v.findViewById(R.id.downloadDeleteButton);

        switch (textOfButton){
            case DELETE:
                downDelButton.setText("Smazat");

                downDelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteYear(yearTextView.getText().toString());
                    }
                });
                break;

            case DOWNLOAD:
                downDelButton.setText("Stáhnout");

                downDelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        downloadYear(yearTextView.getText().toString());
                    }
                });
                break;
        }

        return v;
    }

    public void deleteYear(String yearString){
        for(File f : rootDirectory.listFiles()){
            if( f.getName().endsWith(yearString) /*f.getName().equals(yearString)*/){
                deleteRecursive(f);
            }
        }
    }

    public void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    public void downloadYear(String yearString){
        for (File f : rootDirectory.listFiles()){
            if(f.getName().equals(yearString)){
                f.delete();
            }
        }
        File yearDir = new File(context.getFilesDir().toString(), yearString);
        yearDir.mkdirs();

        getTable("http://policky.org/android/tabor/askquery.php", yearString, yearDir);

    }

    public void getTable(String dbURLString, String yearString, File directory) {
        String result = "";
        int[] ids;
        String[] auhtors, songNames, texts;



        try {
            URL dbURL = new URL(dbURLString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) dbURL.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(),"UTF-8"));
            String postData = URLEncoder.encode("qry","UTF-8") + "=" + URLEncoder.encode("SELECT * FROM `" + yearString + "`","UTF-8");

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

        } catch (
                MalformedURLException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(result);
            auhtors = new String[jsonArray.length()];
            songNames = new String[jsonArray.length()];
            ids = new int[jsonArray.length()];
            texts = new String[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++) {
                ids[i] = jsonArray.getJSONObject(i).getInt("id");
                auhtors[i] = jsonArray.getJSONObject(i).getString("author");
                songNames[i] = jsonArray.getJSONObject(i).getString("song");
                texts[i] = jsonArray.getJSONObject(i).getString("text");
            }


            for(int i = 0; i < ids.length; i++){
                String song = directory.toString() + "/" + ids[i] + "-" + auhtors[i] + "-" + songNames[i];
                try{
                    FileOutputStream FOS = new FileOutputStream(new File(song));
                    //FOS.write((auhtors[i]+"\n").getBytes());
                    //FOS.write((songNames[i] + "\n").getBytes());
                    FOS.write(texts[i].getBytes());
                    FOS.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }


    }

}
