package cz.apolicky.summercampapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A class for organization information.
 */
public class CreateOrganizationFragment extends Fragment {

    private TextInputLayout campName,date1,time1,place1,geo1,date2,time2,place2,geo2,campAddress;

    private CreateConfiguration CC;
    public CreateOrganizationFragment(CreateConfiguration cc){
        CC = cc;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_create_organization, container, false);

        initializeFields(view);

        final Button checkButton = view.findViewById(R.id.createOrganizationCheckButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields()){
                    CC.setTab(1);
                }
            }
        });

        final Button exit = view.findViewById(R.id.createOganizationExitButton);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CC.exit();
            }
        });

        return view;
    }

    /**
     * Initializes fields wrt view 'v'
     * @param v
     */
    private void initializeFields(View v){
        campName = v.findViewById(R.id.textInputLayout3);
        date1 = v.findViewById(R.id.textInputLayout4);
        time1 = v.findViewById(R.id.textInputLayout5);
        place1 = v.findViewById(R.id.departurePlaceTextLayout);
        geo1 = v.findViewById(R.id.departureGeoTextLayout);
        date2 = v.findViewById(R.id.textInputLayout6);
        time2 = v.findViewById(R.id.textInputLayout7);
        place2 = v.findViewById(R.id.arrivalPlaceTextLayout);
        geo2 = v.findViewById(R.id.arrivalGeoTextLayout);
        campAddress = v.findViewById(R.id.textInputLayout8);
    }

    /**
     * Checks fields
     * @return true if all of them are OK.
     */
    private boolean checkFields(){
        int indicator = 0;
        indicator += nameOK(campName);
        indicator += dateFormatOK(date1);
        indicator += dateFormatOK(date2);
        indicator += geoOK(geo1);
        indicator += nameOK(place1);
        indicator += timeFormatOK(time1);
        indicator += timeFormatOK(time2);
        indicator += nameOK(place2);
        indicator += geoOK(geo2);
        indicator += nameOK(campAddress);
        return indicator == 10;
    }

    /**
     * Checks if name is OK.
     * @param name the input field from the name is read.
     * @return 1 if OK, 0 if not
     */
    private int nameOK(TextInputLayout name){
        if(!name.getEditText().getText().toString().isEmpty()){
            name.setError(null);
            return 1;
        }
        name.setError(getString(R.string.create_organization_fill_in_name_cz));
        return 0;
    }

    /**
     * Checks if date is OK.
     * @param dte the input field from the date is read.
     * @return 1 if OK, 0 if not
     */
    private int dateFormatOK(TextInputLayout dte){
        String[] dateParts = dte.getEditText().getText().toString().trim().split("\\.");
        try {
            if (dateParts.length == 3 && dateParts[0].length() == 2 && dateParts[1].length() == 2 && dateParts[2].length() == 4) {
                if (Integer.parseInt(dateParts[0]) > 0 && Integer.parseInt(dateParts[1]) > 0 && Integer.parseInt(dateParts[2]) > 0 &&
                        Integer.parseInt(dateParts[0]) <= 31 && Integer.parseInt(dateParts[1]) <= 12 && Integer.parseInt(dateParts[2]) < 3000) {
                    dte.setError(null);
                    return 1;
                }
            }
            dte.setError(getString(R.string.create_organization_wrong_date_fmt_cz));
            return 0;
        }
        catch (NumberFormatException e){
            dte.setError(getString(R.string.create_organization_wrong_date_fmt_cz));
            return 0;
        }
    }

    /**
     * Checks if time is OK.
     * @param tme the input field from the time is read.
     * @return 1 if OK, 0 if not
     */
    private int timeFormatOK(TextInputLayout tme){
        String[] timeParts = tme.getEditText().getText().toString().trim().split(":");
        try{
            if(timeParts.length == 2 && timeParts[0].length() == 2 && timeParts[1].length() == 2) {
                if(Integer.parseInt(timeParts[0]) > 0 && Integer.parseInt(timeParts[1]) > 0 && Integer.parseInt(timeParts[0]) < 24 && Integer.parseInt(timeParts[1]) < 60) {
                    tme.setError(null);
                    return 1;
                }
            }
            tme.setError(getString(R.string.create_organization_wrong_time_fmt_cz));
            return 0;
        }
        catch (NumberFormatException e){
            tme.setError(getString(R.string.create_organization_wrong_time_fmt_cz));
            return 0;
        }
    }

    /**
     * Checks if geographic coordinates are OK.
     * @param geo the input field from the coords are read.
     * @return 1 if OK, 0 if not
     */
    private int geoOK(TextInputLayout geo){
        if(geo.getEditText().getText().toString().isEmpty()){
            return 1;
        }
        else {
            String[] geoParts = geo.getEditText().getText().toString().trim().split(",");
            try {
                if (geoParts.length == 2) {
                    Double.parseDouble(geoParts[0]);
                    Double.parseDouble(geoParts[1]);
                    geo.setError(null);
                    return 1;
                }
                geo.setError(getString(R.string.create_organization_wrong_geo_fmt_cz));
                return 0;
            } catch (NumberFormatException e) {
                geo.setError(getString(R.string.create_organization_wrong_geo_fmt_cz));
                return 0;
            }
        }
    }

    /**
     * @return application title/ the name of camp
     */
    public String getAppTitle(){
        return campName.getEditText().getText().toString();
    }

    /**
     * @return the place of departure
     */
    public String getDeparturePlace(){
        return place1.getEditText().getText().toString();
    }

    /**
     * @return the date of departure
     */
    public JSONArray getDepartureDate(){
        JSONArray ja = new JSONArray();
        String[] dte = date1.getEditText().getText().toString().split("\\.");
        ja.put(Integer.parseInt(dte[0]));
        ja.put(Integer.parseInt(dte[1]));
        ja.put(Integer.parseInt(dte[2]));
        return ja;
    }

    /**
     * @return the time of departure
     */
    public JSONArray getDepartureTime(){
        JSONArray ja = new JSONArray();
        String[] tme = time1.getEditText().getText().toString().split(":");
        ja.put(Integer.parseInt(tme[0]));
        ja.put(Integer.parseInt(tme[1]));
        return ja;
    }

    /**
     * @return the geographic coordinates of departure
     */
    public JSONArray getDepartureCoords(){
        try{
            JSONArray ja = new JSONArray();
            String[] geo = geo1.getEditText().getText().toString().split(",");
            ja.put(Double.parseDouble(geo[0]));
            ja.put(Double.parseDouble(geo[1]));
            return ja;
        }
        catch (NumberFormatException | JSONException je){
            return null;
        }
    }

    /**
     * @return the place of arrival
     */
    public String getArrivalPlace(){
        return place2.getEditText().getText().toString();
    }

    /**
     * @return the date of arrival
     */
    public JSONArray getArrivalDate(){
        JSONArray ja = new JSONArray();
        String[] dte = date2.getEditText().getText().toString().split("\\.");
        ja.put(Integer.parseInt(dte[0]));
        ja.put(Integer.parseInt(dte[1]));
        ja.put(Integer.parseInt(dte[2]));
        return ja;
    }

    /**
     * @return the time of arrival
     */
    public JSONArray getArrivalTime(){
        JSONArray ja = new JSONArray();
        String[] tme = time2.getEditText().getText().toString().split(":");
        ja.put(Integer.parseInt(tme[0]));
        ja.put(Integer.parseInt(tme[1]));
        return ja;
    }

    /**
     * @return the geographic coordinates of arrival
     */
    public JSONArray getArrivalCoords(){
        try{
            JSONArray ja = new JSONArray();
            String[] geo = geo2.getEditText().getText().toString().split(",");
            ja.put(Double.parseDouble(geo[0]));
            ja.put(Double.parseDouble(geo[1]));
            return ja;
        }
        catch (NumberFormatException | JSONException je){
            return null;
        }
    }

    /**
     * @return the camp address
     */
    public String getCampAddress(){
        return campAddress.getEditText().getText().toString();
    }
}
