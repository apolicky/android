package cz.apolicky.summercampapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A class providing access to information in json files.
 */
public class JSONParser {

    /**
     * Returns value of field 'name' in object 'from' in file 'path_of_file'
     * @param from Outer field where field 'name' is located
     * @param name name of field whose value we want
     * @param path_of_file Path to JSON file
     * @return a value of field 'name' in object 'from' in file 'path_to_file'
     */
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
