package org.policky.ghotaapp2019v2;

import android.content.Context;
import android.util.ArrayMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConfigManager {

    private JSONObject json_res;
    private String curr_file;
    private Context c;
    private boolean config_loaded = false;
    private ArrayMap<String,String> conf_names_paths;

    public ConfigManager(Context c_){
        c = c_;
        conf_names_paths = new ArrayMap<>();
        loadAvailableConfigs();
        loadLastConfig();
        loadConfigDocumentJSON();
    }

    private void loadAvailableConfigs(){
        File rootDir = c.getFilesDir();
        for(File f : rootDir.listFiles()){
            if(f.getName().endsWith(".json")){
                String path_name = f.getAbsolutePath();
                String cong_name = JSONParser.getStringValue("resources",c.getResources().getString(R.string.app_title),path_name);
                conf_names_paths.put(cong_name,path_name);
            }
        }
    }

    private void loadLastConfig(){
        String f = c.getFilesDir() + "/" + c.getResources().getString(R.string.last_used_conf);
        try(InputStream is = new FileInputStream(f)){
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            curr_file = new String(buffer, "UTF-8");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void saveLastConfig(){
        try(FileOutputStream FOS = new FileOutputStream(new File(curr_file))){
            FOS.write(curr_file.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void loadConfigDocumentJSON(){
        try(InputStream is = new FileInputStream(new File(curr_file))){
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String f = new String(buffer);
            JSONObject reader = new JSONObject(f);
            json_res = reader.getJSONObject("resources");
            config_loaded = true;
        }
        catch (IOException | JSONException e){
            e.printStackTrace();
            config_loaded = false;
        }
    }

    public String getValue(String tag){
        if(config_loaded){
            try{
                String s = json_res.getString(tag);
                return s;
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return c.getResources().getString(R.string.unset);
    }

    public String[] getAvailableConfs(){
        String[] res = new String[conf_names_paths.size()];
        for(int i = 0; i < conf_names_paths.size(); i++){
            res[i] = conf_names_paths.keyAt(i);
        }
        return res;
    }

    public int[] getUsedTabs(){
        if(config_loaded){
            try{
                JSONArray ja = json_res.getJSONArray("tabs_used");
                int[] tabs_used = new int[ja.length()];
                for(int i = 0; i < ja.length(); i++){
                    tabs_used[i] = ja.getInt(i);
                }
                return tabs_used;
            }
            catch (JSONException e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public ArrayMap<String,String> getValueMap(String tag){
        ArrayMap<String,String> values = new ArrayMap<>();
        if(config_loaded){
            try{
                JSONArray ja = json_res.getJSONArray(tag);
                for(int i = 0; i < ja.length(); i++){
                    values.put(ja.getJSONObject(i).getString("name"),ja.getJSONObject(i).getString("value"));
                }
                return values;
            }
            catch (JSONException e){
                values.clear();
                return values;
            }
        }
        return values;
    }

    public boolean isConfigLoaded(){
        return config_loaded;
    }

    public void reloadConfig(){
        loadConfigDocumentJSON();
    }

    /**
     * Saves configuration to 'config_file_path' that is downloaded from 'config_url'
     * @param config_file_path place where config will be saved.
     * @param config_url url of page which the config will be downloaded from.
     */
    public static void saveConfiguration(String config_file_path, String config_url){
        try(FileOutputStream FOS = new FileOutputStream(new File(config_file_path))){
            String new_conf = HTMLDownloader.getPage(config_url);
            FOS.write(new_conf.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setConfig(String conf_name){
        curr_file = conf_names_paths.get(conf_name);
        saveLastConfig();
        loadConfigDocumentJSON();
        // would be awesome to refresh the whole app from here.
    }


//    --- commented out -------


//    public List<String> getValues(String tag){
//        ArrayList<String> values = new ArrayList<>();
//        try{
//            JSONArray ja = json_res.getJSONArray(tag);
//            for(int i = 0; i < ja.length(); i++){
//                values.add(ja.getString(i));
//            }
//            return values;
//        }
//        catch (JSONException e){
//            values.clear();
//            return values;
//        }
//    }
}