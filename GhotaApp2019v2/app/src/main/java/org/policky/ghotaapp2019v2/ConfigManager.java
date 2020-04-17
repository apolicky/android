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
import java.util.Map;

public class ConfigManager {

    private JSONObject jsonResource;
    private int indexOfCurrFile = 0;
    private Context context;
    private boolean configLoaded = false;
    private ArrayMap<String,String> confNamesPaths;

    public ConfigManager(Context c_){
        context = c_;
        confNamesPaths = new ArrayMap<>();
        loadAvailableConfigs();
        loadLastConfig();
        loadConfigDocumentJSON();
    }

    private void loadAvailableConfigs(){
        File rootDir = context.getFilesDir();
        for(File f : rootDir.listFiles()){
            if(f.getName().endsWith(".json")){
                String path_name = f.getAbsolutePath();
                String cong_name = JSONParser.getStringValue("resources", context.getResources().getString(R.string.app_title),path_name);
                confNamesPaths.put(cong_name,path_name);
            }
        }
    }

    private void loadLastConfig(){
        String f = context.getFilesDir() + "/" + context.getResources().getString(R.string.last_used_conf);
        try(InputStream is = new FileInputStream(f)){
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String curr_file = new String(buffer, "UTF-8");
            for(int i = 0; i < confNamesPaths.size(); i++){
                if(confNamesPaths.get((confNamesPaths.keyAt(i))).equals(curr_file)){
                    indexOfCurrFile = i;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void saveLastConfig(){
        String f = context.getFilesDir() + "/" + context.getResources().getString(R.string.last_used_conf);
        try(FileOutputStream FOS = new FileOutputStream(new File(f))){
            FOS.write(confNamesPaths.valueAt(indexOfCurrFile).getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void loadConfigDocumentJSON(){
        try(InputStream is = new FileInputStream(new File(confNamesPaths.valueAt(indexOfCurrFile)))){
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String f = new String(buffer);
            JSONObject reader = new JSONObject(f);
            jsonResource = reader.getJSONObject("resources");
            configLoaded = true;
        }
        catch (IOException | JSONException e){
            e.printStackTrace();
            configLoaded = false;
        }
    }

    public String getValue(String tag){
        if(configLoaded){
            try{
                if(tag.equals(context.getString(R.string.photos_web_directory))){
                    return "37.46.208.51/android";
                }
                String s = jsonResource.getString(tag);
                return s;
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return context.getResources().getString(R.string.unset);
    }

    public List<String> getAvailableConfs(){
        List<String> res = new ArrayList<>();
        for(Map.Entry<String,String> me : confNamesPaths.entrySet()){
            res.add(me.getKey());
        }
        return res;
    }

    public int[] getUsedTabs(){
        if(configLoaded){
            try{
                JSONArray ja = jsonResource.getJSONArray("tabs_used");
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
        if(configLoaded){
            try{
                JSONArray ja = jsonResource.getJSONArray(tag);
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

    public int[] getIntArray(String tag){
        if(configLoaded){
            try{
                JSONArray ja = jsonResource.getJSONArray(tag);
                int[] res = new int[ja.length()];
                for(int i = 0; i < ja.length(); i++){
                    res[i] = ja.getInt(i);
                }
                return res;
            }
            catch (JSONException e){
                return null;
            }
        }
        else {
            return null;
        }
    }

    public boolean isConfigLoaded(){
        return configLoaded;
    }

    public void reloadConfig(){
        loadConfigDocumentJSON();
    }

    /**
     * Saves configuration to 'config_file_path' that is downloaded from 'config_url'
     * @param config_file_path place where config will be saved.
     * @param config_url url of page which the config will be downloaded from.
     */
    public static void saveConfiguration(String config_file_path, String config_url) throws IOException{
        File f = new File(config_file_path);
        if(!f.exists()){
            f.createNewFile();
        }
        try(FileOutputStream FOS = new FileOutputStream(f)){
            String new_conf = HTMLDownloader.getPage(config_url);
            FOS.write(new_conf.getBytes());
        }
    }

    public void setConfig(String conf_name){
        indexOfCurrFile = confNamesPaths.indexOfKey(conf_name);
        saveLastConfig();
        loadConfigDocumentJSON();
        // would be awesome to refresh the whole app from here.
    }

    public String currentConfig(){
        return confNamesPaths.keyAt(indexOfCurrFile);
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