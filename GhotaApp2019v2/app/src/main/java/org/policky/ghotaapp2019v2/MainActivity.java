package org.policky.ghotaapp2019v2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.policky.ghotaapp2019v2.ui.main.SectionsPagerAdapter;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static ConfigManager CM;

    SectionsPagerAdapter sectionsPagerAdapter;
    ImageButton syncButton;
    ViewPager viewPager;
    TabLayout tabs;
    Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CM = new ConfigManager(getApplicationContext());

        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
        sp = (Spinner) findViewById(R.id.spinner);
        syncButton = (ImageButton) findViewById(R.id.syncButton);

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncApplication();
            }
        });

        refreshSpinner();

        if(!CM.isConfigLoaded()){
            goToSelectConfig();
            CM.reloadConfig();
        }
    }

    private void syncApplication(){
        Intent i = getIntent();
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(i);
    }

    private void refreshViewPager(){
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), CM);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
    }

    private void refreshConfig(){
        CM.reloadConfig();
        refreshViewPager();
    }

    /**
     * moves to activity SelectConfig.
     */
    private void goToSelectConfig(){
        Intent selectConfigIntent = new Intent(getApplicationContext(), SelectConfig.class);
        startActivity(selectConfigIntent);
    }

    private void refreshSpinner(){
        List<String> availableConfs = CM.getAvailableConfs();
        availableConfs.add(getResources().getString(R.string.manage_configurations));

        final ArrayAdapter<String> adp1 = new ArrayAdapter<>(this,
                R.layout.spinner_item,availableConfs);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(adp1);

        sp.setSelection(adp1.getPosition(CM.currentConfig()));

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == (adp1.getCount() - 1)) goToSelectConfig();
                else CM.setConfig(adp1.getItem(i));

                refreshConfig();
                Toast.makeText(getApplicationContext(),R.string.refresh_config_message,Toast.LENGTH_LONG).show();
           }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}