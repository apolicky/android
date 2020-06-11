package cz.apolicky.summercampapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import cz.apolicky.summercampapp.ui.main.MainPagerAdapter;

import java.util.List;

import cz.apolicky.summercampapp.R;

/**
 * The main activity. Provides tabs specified in config file.
 */
public class MainActivity extends AppCompatActivity {

    static ConfigurationManager CM;

    MainPagerAdapter sectionsPagerAdapter;
    ImageButton syncButton;
    ViewPager viewPager;
    TabLayout tabs;
    Spinner sp;

    private Fragment organizationFragment, contactsFragment, otherFragment, photosFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CM = new ConfigurationManager(getApplicationContext());
        initializeFragments();

        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
        sp = findViewById(R.id.spinner);
        syncButton = findViewById(R.id.syncButton);

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncApplication();
            }
        });

        if(!CM.isConfigLoaded()){
            goToSelectConfiguration();
            CM.reloadConfiguration();
        }
        else{
            refreshSpinner();
        }
    }

    /**
     * Initializes fragments
     */
    private void initializeFragments(){
       organizationFragment = new Organization(CM);
       contactsFragment = new Contacts(CM);
       otherFragment = new Other(CM);
       photosFragment = new OnlinePhotos(CM);
    }

    /**
     * Syncs/restarts application so changes become visible.
     */
    public void syncApplication(){
        Intent i = getIntent();
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(i);
    }

    /**
     * Refreshes view pager. Makes sure that only the tabs config uses are there.
     */
    private void refreshViewPager(){
        sectionsPagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager(), CM);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
    }

    /**
     * Refreshes configuration when new one is selected by spinner.
     */
    private void refreshConfig(){
        CM.reloadConfiguration();
        refreshViewPager();
    }

    /**
     * Starts SelectConfiguration activity.
     */
    private void goToSelectConfiguration(){
        Intent selectConfigIntent = new Intent(getApplicationContext(), SelectConfiguration.class);
        startActivity(selectConfigIntent);
    }

    /**
     * Refreshes the spinner when new config is chosen or downloaded.
     * Sets up the items in spinner, adds the 'change config' item
     */
    private void refreshSpinner(){
        List<String> availableConfs = CM.getAvailableConfigurations();
        availableConfs.add(getResources().getString(R.string.manage_configurations));

        final ArrayAdapter<String> adp1 = new ArrayAdapter<>(this,
                R.layout.spinner_item,availableConfs);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(adp1);

        sp.setSelection(adp1.getPosition(CM.currentConfig()));

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == (adp1.getCount() - 1 )) {
                    goToSelectConfiguration();
                    sp.setSelection(adp1.getPosition(CM.currentConfig()));
                }
                else CM.useConfigurationFile(adp1.getItem(i));

                refreshConfig();
                Toast.makeText(getApplicationContext(),R.string.refresh_config_message,Toast.LENGTH_LONG).show();
           }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /**
     * @return reference to fragment with organization
     */
    public Fragment getOrganizationFragment() {
        return organizationFragment;
    }

    /**
     * @return reference to fragment with contacts
     */
    public Fragment getContactsFragment() {
        return contactsFragment;
    }

    /**
     * @return reference to fragment with other information
     */
    public Fragment getOtherFragment() {
        return otherFragment;
    }

    /**
     * @return reference to fragment with online photos
     */
    public Fragment getPhotosFragment() {
        return photosFragment;
    }

}