package org.policky.ghotaapp2019v2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JSONParser {

    public static String getStringValue(String from, String name, String path_of_file){
        try(InputStream is = new FileInputStream(path_of_file)){
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            new String(buffer, "UTF-8");
            JSONObject reader = new JSONObject(new String(buffer, "UTF-8"));
            String value = reader.getJSONObject(from).getString(name);
            return value;
        }
        catch (IOException | JSONException e){
            e.printStackTrace();
            return "Unknown";
        }
    }

}
