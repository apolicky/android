package cz.apolicky.summercampapp;

import android.content.Context;
import android.util.ArrayMap;
import android.widget.Toast;

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

/**
 * A class that provides access to fields with information saved in a json file.
 * Returns values as String, Int, IntArray, ...
 */
public class ConfigurationManager {

    /**
     * A file where information is saved.
     */
    private JSONObject jsonResource;

    /**
     * A index of current resource file in list returned by 'getFilesDir().listFiles()'
     */
    private int indexOfCurrFile = 0;

    /**
     * Applications context
     */
    private Context context;

    /**
     * Indication whether resource file was loaded successfully or not.
     */
    private boolean configLoaded = false;

    /**
     * A map with names of available resource files and path to them
     */
    private ArrayMap<String,String> confNamesPaths;

    /**
     * A constructor. Loads available configurations, loads the last used configuration.
     * @param c_ context of application
     */
    public ConfigurationManager(Context c_){
        context = c_;
        confNamesPaths = new ArrayMap<>();
        getDownloadedConfigurations();
        getIndexOfLastUsedConfiguration();
        reloadConfiguration();
    }

    /**
     * Looks in to application storage and lists downloaded configurations
     */
    private void getDownloadedConfigurations(){
        File rootDir = context.getFilesDir();
        for(File f : rootDir.listFiles()){
            if(f.getName().endsWith(".json")){
                String path_name = f.getAbsolutePath();
                String cong_name = JSONParser.getStringValue("resources", context.getResources().getString(R.string.app_title), path_name);
                confNamesPaths.put(cong_name,path_name);
            }
        }
    }

    /**
     * Finds the index of last time used configuration.
     */
    private void getIndexOfLastUsedConfiguration(){
        String f = context.getFilesDir() + "/" + context.getString(R.string.last_used_conf);
        try(InputStream is = new FileInputStream(f)){
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String curr_file = new String(buffer, "UTF-8");

            indexOfCurrFile =  confNamesPaths.containsKey(curr_file) ? confNamesPaths.indexOfKey(curr_file) : -1;
        }
        catch (IOException e){
            Toast.makeText(context, context.getString(R.string.config_could_not_be_loaded_cz), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Loads configuration from current file or the first available file, if last_used can not be found.
     */
    public void reloadConfiguration(){
        if(confNamesPaths.size() > 0) {
            if(indexOfCurrFile == -1) indexOfCurrFile = 0;

            try (InputStream is = new FileInputStream(new File(confNamesPaths.valueAt(indexOfCurrFile)))) {
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String f = new String(buffer);
                JSONObject reader = new JSONObject(f);

                jsonResource = reader.getJSONObject("resources");
                configLoaded = true;

                saveCurrentConfiguration();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                configLoaded = false;
            }
        }
        else {
            configLoaded = false;
        }
    }

    /**
     * Saves the name of the current file into a file in storage. It is then used to load the right
     * configuration when app is started again
     */
    private void saveCurrentConfiguration(){
        String f = context.getFilesDir() + "/" + context.getResources().getString(R.string.last_used_conf);
        try(FileOutputStream FOS = new FileOutputStream(new File(f))){
            FOS.write(confNamesPaths.valueAt(indexOfCurrFile).getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns available configurations
     * @return a list of configuration names
     */
    public List<String> getAvailableConfigurations(){
        List<String> res = new ArrayList<>();
        for(Map.Entry<String,String> me : confNamesPaths.entrySet()){
            res.add(me.getKey());
        }
        return res;
    }

    // --- GETTING VALUES FROM JSON RESOURCE FILE ---------

    /**
     * Returns the string value of field with tag 'tag'
     * @param tag a tag of field to be read
     * @return a value of 'tag' field
     */
    public String getValue(String tag){
        if(configLoaded){
            try{
                String s = jsonResource.getString(tag);
                return s;
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return context.getResources().getString(R.string.unset);
    }

    /**
     * Returns the geographic coordinates as a string
     * @param tag a specifier of departure or arrival
     * @return a string with coordinated delimited by a comma
     */
    public String getGeo(String tag){
        if(configLoaded){
            try{
                JSONArray ja = jsonResource.getJSONArray(tag);
                String s = ja.get(0) + "," + ja.get(1);
                return s;
            }
            catch (JSONException je){

            }
        }
        return "0,0";
    }

    /**
     * Returns a array of integers for specified tag.
     * Returns null when an array can not be found.
     * @param tag a specifier of an array
     * @return an integer array for tag 'tag'
     */
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
            catch (JSONException e){}
        }
        return null;
    }

    /**
     * Returns a Map<String,String> for field with tag 'tag'.
     * Empty map is returned when an exception occurs.
     * @param tag a specifier of a field in resource file
     * @return a Map<String,String> for field with tag 'tag'
     */
    public ArrayMap<String,String> getValueMap(String tag){
        ArrayMap<String,String> values = new ArrayMap<>();
        if(configLoaded){
            try{
                JSONArray ja = jsonResource.getJSONArray(tag);
                for(int i = 0; i < ja.length(); i++){
                    values.put(ja.getJSONObject(i).getString("name"),ja.getJSONObject(i).getString("value"));
                }
            }
            catch (JSONException e){
                values.clear();
            }
        }
        return values;
    }

    /**
     * Informs whether configuration was loaded or not.
     * @return true if configuration was loaded successfully
     */
    public boolean isConfigLoaded(){
        return configLoaded;
    }

    /**
     * Saves a configuration from web url 'config_url' to file 'config_file_path'
     * @param config_file_path a path where new file should be saved
     * @param config_url a url from where new file can be downloaded
     * @throws IOException if file can not be saved
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

    /**
     * Starts to use 'conf_name' file as a new resource file.
     * @param conf_name a name of new resource file
     */
    public void useConfigurationFile(String conf_name){
        indexOfCurrFile = confNamesPaths.indexOfKey(conf_name);
        saveCurrentConfiguration();
        reloadConfiguration();
    }

    /**
     * @return a name of file currently used as a resource file.
     */
    public String currentConfig(){
        return confNamesPaths.keyAt(indexOfCurrFile);
    }

}