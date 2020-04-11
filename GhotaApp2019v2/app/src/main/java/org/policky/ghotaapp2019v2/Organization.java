package org.policky.ghotaapp2019v2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

    TextView departureTV, arrivalTV;
    Switch notificationSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_organization_layout, container, false);

        departureTV = (TextView) view.findViewById(R.id.departurePlaceValueTextView);
        arrivalTV = (TextView) view.findViewById(R.id.arrivalPlaceValueTextView);
        notificationSwitch = (Switch) view.findViewById(R.id.setNotificationSwitch);

        departureTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(getLocationUri());
            }
        });

        arrivalTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(getLocationUri());
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

    public void navigate(Uri geoLocation) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        System.out.println(geoLocation + " : " + getString(R.string.arrival_geo_location));
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public Uri getLocationUri(){
        return Uri.parse("geo:" + getString(R.string.arrival_geo_location) + "?q=" + getString(R.string.arrival_geo_location) + "(" + arrivalTV.getText().toString() +")");
    }

    public void initializePositionOfSwitch(Switch sw){
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

    public void setPositionOfSwitch(Switch sw, boolean nowChecked){
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
}
