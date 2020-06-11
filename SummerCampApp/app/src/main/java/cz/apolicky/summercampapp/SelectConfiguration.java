package cz.apolicky.summercampapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates an interface to select configuration for summer camp.
 */
public class SelectConfiguration extends AppCompatActivity {

    private TextView currentConfingTextView, currentAuthorTextView;
    private ListView listOfConfigurations;
    private Button getAvailableConfigurationsButton, createConfigurationButton;
    private File rootDir;

    private ArrayList<String> conf_urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_config);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        rootDir = getFilesDir();

        currentConfingTextView = findViewById(R.id.curr_conf_value);
        currentAuthorTextView = findViewById(R.id.conf_author_value);
        listOfConfigurations = findViewById(R.id.config_list_view);
        getAvailableConfigurationsButton = findViewById(R.id.refresh_config_btn);
        createConfigurationButton = findViewById(R.id.create_conf_btn);

        reloadConfig();
        listConfs(getDownloadedConfigurations(), getOnlineConfigurations());

        getAvailableConfigurationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadConfig();
                listConfs(getDownloadedConfigurations(), getOnlineConfigurations());
            }
        });

        createConfigurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createConfig();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * Launches CreateConfiguration activity
     */
    private void createConfig(){
        Intent createConfigIntent = new Intent(getApplicationContext(), CreateConfiguration.class);
        startActivity(createConfigIntent);
    }

    /**
     * Refreshes TextViews with current configuration.
     */
    private void reloadConfig(){
        MainActivity.CM.reloadConfiguration();
        currentAuthorTextView.setText(MainActivity.CM.getValue(getResources().getString(R.string.config_author)));
        currentConfingTextView.setText(MainActivity.CM.getValue(getResources().getString(R.string.config_name)));
    }

    /**
     * Extracts configuration names from web urls
     * @param conf_urls list of configuration urls
     * @return names of configuration urls
     */
    public List<String> parseNames(List<String> conf_urls){
        ArrayList<String> names = new ArrayList<>();
        for(String u : conf_urls){
            String[] parts = u.split("/");
            String[] name_ext = parts[parts.length - 1].split("\\.");
            names.add(name_ext[0]);
        }
        return names;
    }

    /**
     * Lists configurations in 'lostOfConfigurations' listView.
     * @param downloaded Map<String,String> of names and paths to downloaded configurations
     * @param online Map<String,String> of names and urls to online configurations
     */
    private void listConfs(ArrayMap<String,String> downloaded, ArrayMap<String,String> online){
        SelectConfigurationAdapter sca = new SelectConfigurationAdapter(this, getApplicationContext(), getFilesDir(), downloaded, online);
        listOfConfigurations.setAdapter(sca);
    }

    /**
     * Extracts links to configurations from "server" and returns them
     * @param c application context for reaching resources
     * @param htmlsite A source code of server's website
     * @return list of links to online configurations
     */
    private static List<String> parseHREFs(Context c, String htmlsite){
        List<String> confs = new ArrayList<>();

        String[] parts = htmlsite.split(" ");

        String cont = c.getResources().getString(R.string.conf_contains);
        String cont_inner = c.getResources().getString(R.string.conf_contains_inner);
        String git_raw = c.getResources().getString(R.string.git_raw_pref);

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

        return confs;
    }

    /**
     * @return Map<String,Stirng> of names and urls to online configurations
     */
    private ArrayMap<String,String> getOnlineConfigurations(){
        try{
            ArrayMap<String,String> a = new ArrayMap<>();
            List<String> urls = parseHREFs(this, HTMLDownloader.getPage(getResources().getString(R.string.conf_url)));
            List<String> names = parseNames(urls);
            for(int i = 0; i < urls.size(); i++){
                a.put(names.get(i), urls.get(i));
            }
            return a;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return Map<String,String> of names and paths to downloaded configurations
     */
    private ArrayMap<String,String> getDownloadedConfigurations(){
        ArrayMap<String,String> downloadedConfigurationsNamePath = new ArrayMap<>();
        File rootDir = getApplicationContext().getFilesDir();
        for(File f : rootDir.listFiles()){
            if(f.getName().endsWith(".json")){
                String path = f.getAbsolutePath();
                String name = f.getName().split("/")[f.getName().split("/").length - 1].split("\\.")[0];
                downloadedConfigurationsNamePath.put(name,path);
            }
        }
        return downloadedConfigurationsNamePath;
    }

    /**
     * Returns the list of names of configurations that already exist
     * @param c context of application for resources
     * @return list of names of configurations that already exist
     */
    public static List<String> takenConfNames(Context c){
        try{
            List<String> j = new ArrayList<>();
            List<String> l = parseHREFs(c, HTMLDownloader.getPage(c.getResources().getString(R.string.conf_url)));
            for(String s : l){
                String[] parts = s.split("/");
                s = parts[parts.length - 1].split("\\.")[0];
                j.add(s);
            }
            return j;
        }
        catch (IOException e){
            return null;
        }

    }
}
