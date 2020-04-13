package org.policky.ghotaapp2019v2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.policky.ghotaapp2019v2.ui.main.SectionsPagerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    File rootDir;
    ConfigManager CM;

    ArrayMap<View,String> view_resource_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CM = new ConfigManager(getApplicationContext());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), CM);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSelectConfig();
            }
        });

        // delete ---
        String path = getFilesDir() + "/" + getResources().getString(R.string.conf_filename_json);
        try(FileOutputStream FOS = new FileOutputStream(new File(path))){
            String json_conf = "{ \"resources\" : { \"app_title\" : \"Tabor 2020\", \"conf_author\" : \"Vašek Pokus\" }}";
            FOS.write(json_conf.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }

        // -------


        rootDir = getFilesDir();

        if(!CM.isConfigLoaded()){
            goToSelectConfig();
            CM.reloadConfig();
        }
    }

    /**
     * moves to activity SelectConfig.
     */
    private void goToSelectConfig(){
        Intent selectConfigIntent = new Intent(getApplicationContext(), SelectConfig.class);
        startActivity(selectConfigIntent);
    }
}