package org.policky.ghotaapp2019v2;

import android.content.Context;
import android.util.ArrayMap;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ConfigManager {

    private ArrayMap<View,String> view_res_map;
    private NodeList node_list;
    private JSONObject json_res;
    private Context c;
    private boolean config_loaded = false;

    public ConfigManager(Context c_){
        c = c_;
        view_res_map = new ArrayMap<>();
        loadConfigDocumentXML();
        loadConfigDocumentJSON();
    }

    public void addViews(ArrayMap<View,String> view_res){
        for (Map.Entry<View,String> p: view_res.entrySet()) {
            view_res_map.put(p.getKey(),p.getValue());
        }
    }

    private void loadConfigDocumentXML(){
        String file_name = c.getResources().getString(R.string.conf_filename);

        try (InputStream is = new FileInputStream(new File(c.getFilesDir() + "/" + file_name))) {

            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            node_list = doc.getElementsByTagName("string");

            config_loaded = true;

        } catch (Exception e) {
            e.printStackTrace();
            config_loaded = false;
        }
    }

    private void loadConfigDocumentJSON(){
        String file_name = c.getFilesDir() + "/" + c.getResources().getString(R.string.conf_filename_json);

        try(InputStream is = new FileInputStream(file_name)){
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            new String(buffer, "UTF-8");
            JSONObject reader = new JSONObject(new String(buffer, "UTF-8"));
            json_res = reader.getJSONObject("resources");
            config_loaded = true;
        }
        catch (IOException | JSONException e){
            e.printStackTrace();
            config_loaded = false;
        }
    }

    /**
     * Saves configuration to 'config_file_path' that is downloaded from 'config_url'
     * @param config_file_path place where config will be saved.
     * @param config_url url of page which the config will be downloaded from.
     */
    public static void saveConfiguration(String config_file_path, String config_url){
        try(FileOutputStream FOS = new FileOutputStream(new File(config_file_path))){
            String xml_conf = HTMLDownloader.getPage(config_url);
            FOS.write(xml_conf.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void loadConfiguration(Context c, Map<View,String> views) {

    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public String getNewTitle(){
        return "New Title";
    }

    public String getValue(String tag){
        try{
            String s = json_res.getString(tag);
            return s;
        }
        catch (JSONException e){
            return c.getResources().getString(R.string.unset);
        }
    }

    public List<String> getValues(String tag){
        ArrayList<String> values = new ArrayList<>();
        try{
            JSONArray ja = json_res.getJSONArray(tag);
            for(int i = 0; i < ja.length(); i++){
                values.add(ja.getString(i));
            }
            return values;
        }
        catch (JSONException e){
            values.clear();
            return values;
        }
    }

    public ArrayMap<String,String> getValuesMap(String tag){
        ArrayMap<String,String> values = new ArrayMap<>();
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

    public boolean isConfigLoaded(){
        return config_loaded;
    }

    public void reloadConfig(){
        loadConfigDocumentXML();
        loadConfigDocumentJSON();
    }

}