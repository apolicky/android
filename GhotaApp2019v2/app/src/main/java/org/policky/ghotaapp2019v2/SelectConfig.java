package org.policky.ghotaapp2019v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Network;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.ArrayMap;
import android.util.Pair;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Creates an interface to select configuration for summer camp.
 */
public class SelectConfig extends AppCompatActivity {

    private TextView curr_conf, curr_author;
    private ListView config_list_view;
    private Button refresh_available_conf_btn, reload_conf_btn, create_conf_btn;
    private File rootDir;

    private ConfigManager CM;

    private ArrayList<String> conf_urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_config);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        CM = new ConfigManager(getApplicationContext());
        rootDir = getFilesDir();

        curr_conf = (TextView) findViewById(R.id.curr_conf_value);
        curr_author = (TextView) findViewById(R.id.conf_author_value);
        config_list_view = (ListView) findViewById(R.id.config_list_view);
        refresh_available_conf_btn = (Button) findViewById(R.id.refresh_config_btn);
        reload_conf_btn = (Button) findViewById(R.id.reload_config_btn);
        create_conf_btn = (Button) findViewById(R.id.create_conf_btn);

        reloadConfig();

        conf_urls = new ArrayList<>();

        reload_conf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadConfig();
            }
        });

        refresh_available_conf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAvailableConfigs();
            }
        });

        create_conf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createConfig();
            }
        });

    }

    private void createConfig(){
        Intent createConfigIntent = new Intent(getApplicationContext(),CreateConfig.class);
        startActivity(createConfigIntent);
    }

    private void reloadConfig(){
        CM.reloadConfig();
        curr_author.setText(CM.getValue(getResources().getString(R.string.conf_author)));
        curr_conf.setText(CM.getValue(getResources().getString(R.string.conf_name)));
    }

    /**
     * Extracts names from urls, gets rid of the extension.
     * @param conf_urls urls of config files
     * @return parsed names.
     */
    private List<String> parseNames(List<String> conf_urls){
        ArrayList<String> names = new ArrayList<>();
        for(String u : conf_urls){
            String[] parts = u.split("/");
            String[] name_ext = parts[parts.length - 1].split("\\.");
            names.add(name_ext[0]);
        }
        return names;
    }

    /**
     * Inflates config_list_view with conf_names.
     * @param conf_urls urls to configurations.
     * @param conf_names names of configurations.
     */
    private void listConfs(List conf_urls, List conf_names){
        SelectConfigAdapter sca = new SelectConfigAdapter(getApplicationContext(),getFilesDir(),conf_urls,conf_names);
        config_list_view.setAdapter(sca);
    }

    /**
     * In 'confs' inserts parsed urls where configuration files are located.
     * @param confs list of strings where configuration files are located.
     * @param htmlsite html of site that is to be parsed.
     */
    private void parseHREFs(List confs, String htmlsite){

        String[] parts = htmlsite.split(" ");

        String cont = getResources().getString(R.string.conf_contains);
        String cont_inner = getResources().getString(R.string.conf_contains_inner);
        String git_raw = getResources().getString(R.string.git_raw_pref);

        for(String part : parts) {
            if (!part.isEmpty()) {
                if (part.contains(cont)) {
                    String[] p = part.split("\"");
                    for (String p_ : p) {
                        if (p_.contains(cont_inner)) {
                            confs.add(git_raw + p_.replace("/blob/", "/"));
                        }
                    }
                }
            }
        }
    }

    /**
     * Downloads html page from 'url'
     * @param url the url of html page.
     * @return the page in html format.
     * @throws IOException throws when url is malformed.
     */
    private String getHTML(String url) throws IOException {
        return HTMLDownloader.getPage(url);
    }

    private void showAvailableConfigs(){
        try{
            String possible_confs_html = getHTML(getResources().getString(R.string.conf_url));
            parseHREFs(conf_urls, possible_confs_html);
            listConfs(conf_urls,parseNames(conf_urls));
        }
        catch (IOException e){ e.printStackTrace();}
    }
}
