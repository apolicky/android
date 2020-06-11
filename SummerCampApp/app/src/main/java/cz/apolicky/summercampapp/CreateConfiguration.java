package cz.apolicky.summercampapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.apolicky.summercampapp.ui.main.ConfPagerAdapter;

/**
 * An activity that creates a new configuration which can then be downloaded from the server.
 */
public class CreateConfiguration extends AppCompatActivity {

    private CreateOrganizationFragment COrF;
    private CreateOtherFragment COtF;
    private CreateContactsFragment CCF;
    private CreatePhotosFragment CPF;
    private CreateDeveloperFragment CDF;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_conf);

        initializeFragments();

        ConfPagerAdapter confPagerAdapter = new ConfPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(confPagerAdapter);
    }

    /**
     * Initializes fragments used when creating new configuration
     */
    private void initializeFragments(){
        COrF = new CreateOrganizationFragment(this);
        CCF = new CreateContactsFragment(this);
        COtF = new CreateOtherFragment(this);
        CPF = new CreatePhotosFragment(this);
        CDF = new CreateDeveloperFragment(this);
    }

    /**
     * Sets active tab to i-th one.
     * @param i an index of newly used tab.
     */
    public void setTab(int i){
        viewPager.setCurrentItem(i);
    }

    /**
     * @return a reference to fragment creating organization stuff
     */
    public Fragment getCreateOrganizationFragment() {
        return COrF;
    }

    /**
     * @return a reference to fragment creating contacts
     */
    public Fragment getCreateContactsFragment() {
        return CCF;
    }

    /**
     * @return a reference to fragment creating other information
     */
    public Fragment getCreateOthersFragment() {
        return COtF;
    }

    /**
     * @return a reference to fragment creating online photos
     */
    public Fragment getCreatePhotosFragment() {
        return CPF;
    }

    /**
     * @return a reference to fragment creating developer informations
     */
    public Fragment getCreateDeveloperFragment() {
        return CDF;
    }

    /**
     * Sends the information from create*Fragments to the server.
     */
    public void sendConfiguration(){
        try{
            JSONObject newConfiguration = new JSONObject();
            JSONObject resources = new JSONObject();
            JSONArray tabs_used = new JSONArray();
            String subject = CDF.getConfigurationName();
            resources.put(getString(R.string.app_title), COrF.getAppTitle());
            // --- developer ---------
            resources.put(getString(R.string.config_author), CDF.getAuthorName());
            resources.put(getString(R.string.config_name), CDF.getConfigurationName());
            resources.put(getString(R.string.author_mail), CDF.getAuthorEmail());
            // --- organization ---------
            resources.put(getString(R.string.organization_departure_place), COrF.getDeparturePlace());
            resources.put(getString(R.string.organization_departure_date), COrF.getDepartureDate());
            resources.put(getString(R.string.organization_departure_time), COrF.getDepartureTime());
            if(COrF.getDepartureCoords() != null){
                resources.put(getString(R.string.organization_departure_geo_coords), COrF.getDepartureCoords());
            }
            resources.put(getString(R.string.organization_arrival_place), COrF.getArrivalPlace());
            resources.put(getString(R.string.organization_arrival_date), COrF.getArrivalDate());
            resources.put(getString(R.string.organization_arrival_time), COrF.getArrivalTime());
            if(COrF.getArrivalCoords() != null){
                resources.put(getString(R.string.organization_arrival_geo_coords), COrF.getArrivalCoords());
            }
            resources.put(getString(R.string.organization_camp_address_value), COrF.getCampAddress());
            tabs_used.put(0);
            // --- contacts ---------
            if(!CCF.noContacts()){
                resources.put(getString(R.string.contacts),CCF.getContacts());
                tabs_used.put(1);
            }
            // --- otherinfo ---------
            if(!COtF.noOtherInfo()){
                resources.put(getString(R.string.other_info_text), COtF.getOtherInfo());
                tabs_used.put(2);
            }
            // --- online photos ---------
            if(!CPF.noPhotos()){
                resources.put(getString(R.string.photos_web_directory), CPF.getPhotoAddress());
                tabs_used.put(3);
            }

            newConfiguration.put("resources",resources);
            System.out.println(newConfiguration.toString());

            sendConfigurationByMail(subject, newConfiguration.toString());
        }
        catch(JSONException je){
            je.printStackTrace();
        }


        exit();
    }

    /**
     * Sends an email to the server
     * @param subject the name of configuration is used as a subject of email
     * @param message the json format string with configuration
     */
    private void sendConfigurationByMail(String subject, String message){

        String[] TO = {getString(R.string.email_target)};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(),
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void exit(){
        this.finish();
    }
}