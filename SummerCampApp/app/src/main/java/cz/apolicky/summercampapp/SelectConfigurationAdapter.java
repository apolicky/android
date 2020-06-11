package cz.apolicky.summercampapp;

import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * An adapter that lists downloaded and online configurations.
 * If a configuration is online and downloaded, lists it only in downloaded
 */
public class SelectConfigurationAdapter extends BaseAdapter {

    ArrayMap<String,String> downloaded, online;
    private SelectConfiguration SC;
    Context context;
    File rootDir;
    LayoutInflater mInflater;

    SelectConfigurationAdapter(SelectConfiguration sc, Context cont, File rootD, ArrayMap<String,String> downloaded, ArrayMap<String,String> online){
        SC = sc;
        context = cont;
        this.online = online;
        this.downloaded = downloaded;
        rootDir = rootD;
        mInflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        removeDuplicates();
    }

    @Override
    public int getCount() {
        return downloaded.size() + online.size();
    }

    @Override
    public Object getItem(int i) {
        if(i >= downloaded.size()){
            return online.keyAt(i - downloaded.size());
        }
        else{
            return downloaded.keyAt(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = (View) mInflater.inflate(R.layout.select_config_adapter,null);

        final TextView conf_name_text_view = (TextView) v.findViewById(R.id.sel_conf_available_conf_text_view);

        if(i >= downloaded.size()){
            conf_name_text_view.setText(online.keyAt(i - downloaded.size()));
            Button downloadConfigurationButton = (Button) v.findViewById(R.id.sel_conf_set_config_btn);

            downloadConfigurationButton.setText(context.getString(R.string.config_download_btn_text));
            downloadConfigurationButton.setOnClickListener(new ArgumentedOnClickListener(i - downloaded.size()){
                @Override
                public void onClick(View v) {
                    setConfiguration(argument);
                }
            });

        }
        else{
            conf_name_text_view.setText(downloaded.keyAt(i));
            Button deleteConfigurationButton = (Button) v.findViewById(R.id.sel_conf_set_config_btn);

            deleteConfigurationButton.setText(context.getString(R.string.config_delete_btn_text));
            deleteConfigurationButton.setOnClickListener(new ArgumentedOnClickListener(i){
                @Override
                public void onClick(View v) {
                    deleteConfiguration(argument);
                }
            });

        }

        return  v;
    }

    /**
     * Deletes i-th configuration
     * @param i index of configuration to be deleted
     */
    private void deleteConfiguration(int i){
        File f = new File(downloaded.valueAt(i));
        if(f.delete()){
            Toast.makeText(context, "File deleted.", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(context, "File NOT deleted.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Saves i-th configuration
     * @param i index of configuration to be downloaded
     */
    private void setConfiguration(int i){
        String file_name = rootDir + "/" + online.keyAt(i) + ".json";
        try{
            ConfigurationManager.saveConfiguration(file_name, online.valueAt(i));
            Toast.makeText(context, "Downloaded.", Toast.LENGTH_LONG).show();
        }
        catch (IOException e){
            Toast.makeText(context,"File "+ file_name + "could not be created.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * From online deletes links to configurations that are downloaded
     */
    private void removeDuplicates(){
        for(Map.Entry<String,String> d : downloaded.entrySet()){
            if(online.containsKey(d.getKey())){
                online.remove(d.getKey());
            }
        }
    }
}
