package org.policky.ghotaapp2019v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class SelectConfigAdapter extends BaseAdapter {

    List<String> configs_urls, config_names;

    int file_used;

    Context context;
    File rootDir;
    LayoutInflater mInflater;

    SelectConfigAdapter(Context cont, File rootD, List<String> conf_urls, List<String> conf_names){
        context = cont;
        configs_urls = conf_urls;
        config_names = conf_names;
        rootDir = rootD;
        mInflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return configs_urls.size();
    }

    @Override
    public Object getItem(int i) {
        return configs_urls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = (View) mInflater.inflate(R.layout.select_config_l, null);

        file_used = i;

        final TextView conf_name_text_view = (TextView) v.findViewById(R.id.sel_conf_available_conf_text_view);
        conf_name_text_view.setText(config_names.get(i));

        Button set_conf_btn = (Button) v.findViewById(R.id.sel_conf_set_config_btn);

        set_conf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setConfiguration();
            }
        });

        return  v;
    }


    /**
     * Tries to download configuration file saved saved on web page 'config_urls.get(file_used)'
     * Saves it as configuration file that will be used from now on.
     */
    private void setConfiguration(){
        String file_name = rootDir + "/" + context.getResources().getString(R.string.conf_filename_json);
        ConfigManager.saveConfiguration(file_name, configs_urls.get(file_used));
    }
}
