package org.policky.ghotaapp2019v2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.policky.ghotaapp2019v2.ui.main.SectionsPagerAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

        final Toolbar toolbar = findViewById(R.id.toolbar);
//        setActionBar(toolbar);
//        getActionBar().setTitle(CM.getValue(getResources().getString(R.string.app_title)));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshApplication();
            }
        });

        setCampConfigSpinner();

        rootDir = getFilesDir();

        if(!CM.isConfigLoaded()){
            goToSelectConfig();
            CM.reloadConfig();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadConfig();
    }

    private void reloadConfig(){
        CM.reloadConfig();
    }

    /**
     * moves to activity SelectConfig.
     */
    private void goToSelectConfig(){
        Intent selectConfigIntent = new Intent(getApplicationContext(), SelectConfig.class);
        startActivity(selectConfigIntent);
    }

    private void refreshApplication(){
        Intent i = getIntent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(i);
    }

    private void setCampConfigSpinner(){
        Spinner sp = (Spinner) findViewById(R.id.spinner);
        String[] arr = CM.getAvailableConfs();

        final ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,arr);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp1);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CM.setConfig(adp1.getItem(i));
                refreshApplication();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}