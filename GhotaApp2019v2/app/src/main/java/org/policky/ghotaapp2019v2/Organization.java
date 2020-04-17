package org.policky.ghotaapp2019v2;

import android.content.Intent;
import android.icu.util.Calendar;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;


public class Organization extends Fragment {

    TextView departurePlaceTextView, arrivalPlaceTextView, departureTimeTextView, arrivalTimeTextView, campAddressTextView, setNotificationTextView;
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
        setNotificationTextView = (TextView) view.findViewById(R.id.setNotificationTextView);


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

        setNotificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCalendarEvent();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
            coords = CM.getValue(getResources().getString(R.string.organization_departure_geo_coords));
            text = CM.getValue(getResources().getString(R.string.organization_departure_place));
        }
        else{
            coords = CM.getValue(getResources().getString(R.string.organization_arrival_geo_coords));
            text = CM.getValue(getResources().getString(R.string.organization_arrival_place));
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
        departurePlaceTextView.setText(CM.getValue(getResources().getString(R.string.organization_departure_place)));
        arrivalPlaceTextView.setText(CM.getValue(getResources().getString(R.string.organization_arrival_place)));
        campAddressTextView.setText(CM.getValue(getResources().getString(R.string.organization_camp_address_value)));

        int[] dDate = CM.getIntArray(getResources().getString(R.string.organization_departure_date));
        int[] dTime = CM.getIntArray(getResources().getString(R.string.organization_departure_time));
        int[] aDate = CM.getIntArray(getResources().getString(R.string.organization_arrival_date));
        int[] aTime = CM.getIntArray(getResources().getString(R.string.organization_arrival_time));

        try {
            departureTimeTextView.setText(datify(dDate, dTime));
            arrivalTimeTextView.setText(datify(aDate, aTime));
        } catch (IOException e){}
    }

    private void createCalendarEvent(){
        int[] depDate,depTime,arrDate,arrTime;
        depDate = CM.getIntArray(getResources().getString(R.string.organization_departure_date));
        depTime = CM.getIntArray(getResources().getString(R.string.organization_departure_time));
        arrDate = CM.getIntArray(getResources().getString(R.string.organization_arrival_date));
        arrTime = CM.getIntArray(getResources().getString(R.string.organization_arrival_time));

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(depDate[2], depDate[1], depDate[0], depTime[0], depTime[1]);

        Calendar endTime = Calendar.getInstance();
        endTime.set(arrDate[2], arrDate[1], arrDate[0], arrTime[0], arrTime[1]);

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");

        intent.putExtra(CalendarContract.Events.TITLE, "Nazev akce");
        intent.putExtra(CalendarContract.Events.ALL_DAY, false);// periodicity
        intent.putExtra(CalendarContract.Events.DESCRIPTION,"popisek akce");
        intent.putExtra(CalendarContract.Reminders.MINUTES,50);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Misto Akce");

        startActivity(intent);
    }

    private String datify(int[] date, int[] time) throws IOException{
        if(date!=null && time!=null) {
            if (date.length == 3 && time.length == 2) {
                return date[0] + "." + date[1] + "." + date[2] + " " + time[0] + ":" + time[1];
            }
        }
        throw new IOException("Wrong datetime format");
    }

}
