package org.policky.ghotaapp2019v2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;


public class Organization extends Fragment {

    TextView departurePlaceTextView, arrivalPlaceTextView, departureTimeTextView, arrivalTimeTextView, campAddressTextView;
    Switch notificationSwitch;
    private ConfigManager CM;

    public Organization(ConfigManager CM_){
        CM = CM_;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_organization_layout, container, false);

        departurePlaceTextView = (TextView) view.findViewById(R.id.departurePlaceValueTextView);
        departureTimeTextView = (TextView) view.findViewById(R.id.departureTimeValueTextView);
        arrivalPlaceTextView = (TextView) view.findViewById(R.id.arrivalPlaceValueTextView);
        arrivalTimeTextView = (TextView) view.findViewById(R.id.arrivalTimeValueTextView);

        campAddressTextView = (TextView) view.findViewById(R.id.campAdressValueTextView);

        notificationSwitch = (Switch) view.findViewById(R.id.setNotificationSwitch);

        initializeTexts();

        departurePlaceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(getLocationUri(true));
            }
        });

        arrivalPlaceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(getLocationUri(false));
            }
        });

        initializePositionOfSwitch(notificationSwitch);

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setPositionOfSwitch(notificationSwitch, b);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadConfig();
    }

    private void reloadConfig(){
        CM.reloadConfig();
        initializeTexts();
    }

    private void navigate(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    // pass true if departure, if arrival pass false.
    private Uri getLocationUri(boolean departure){
        String coords;
        String text = "";
        if(departure){
            coords = CM.getValue(getResources().getString(R.string.departure_geo_location));
            text = CM.getValue(getResources().getString(R.string.departure_place));
        }
        else{
            coords = CM.getValue(getResources().getString(R.string.arrival_geo_location));
            text = CM.getValue(getResources().getString(R.string.arrival_place));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("geo:");
        sb.append(coords);
        sb.append("?q=");
        sb.append(coords);
        sb.append("(");
        sb.append(text);
        sb.append(")");

        return Uri.parse(sb.toString());
    }

    /**
     * Initializes texts of TextViews from current config.
     */
    private void initializeTexts() {
        departurePlaceTextView.setText(CM.getValue(getResources().getString(R.string.departure_place)));
        departureTimeTextView.setText(CM.getValue(getResources().getString(R.string.departure_time)));
        arrivalPlaceTextView.setText(CM.getValue(getResources().getString(R.string.arrival_place)));
        arrivalTimeTextView.setText(CM.getValue(getResources().getString(R.string.arrival_time)));
        campAddressTextView.setText(CM.getValue(getResources().getString(R.string.camp_address_value)));
    }

    private void initializePositionOfSwitch(Switch sw){
        File rootDir = getContext().getFilesDir();
        boolean isSetNotif = false;
        for(File f : rootDir.listFiles()){
            //Log.d("initPosition:", f.getName() + "---" + rootDir.toString() + "/" + getString(R.string.notification_indicator_file));
            if(f.getName().equals(getString(R.string.notification_indicator_file))){
                isSetNotif = true;
                break;
            }
        }

        sw.setChecked(isSetNotif);

    }

    private void setPositionOfSwitch(Switch sw, boolean nowChecked){
        File rootDir = getContext().getFilesDir();
        if(nowChecked){
            File switchIndicator = new File(rootDir.toString() + "/" + getString(R.string.notification_indicator_file));
            if(!switchIndicator.exists()){
                try{
                    switchIndicator.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            for(File f : rootDir.listFiles()){
                if(f.getName().equals(getString(R.string.notification_indicator_file))){
                    f.delete();
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.change_config_menu,menu);
        MenuItem item = menu.add ("Clear Array");
        item.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick (MenuItem item){
//                clearArray();
                return true;
            }
        });
    }


}
