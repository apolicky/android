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
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A class for creating contacts.
 */
public class CreateContactsFragment extends Fragment {

    private boolean noContacts = false;
    private ArrayList<TextInputLayout> names = new ArrayList<>();
    private ArrayList<TextInputLayout> values = new ArrayList<>();

    private CreateConfiguration CC;
    public CreateContactsFragment(CreateConfiguration cc){
        CC = cc;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_create_contacts, container, false);

        initializeFields(view);

        final Button skipButton = view.findViewById(R.id.contactsPassButton);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noContacts = true;
                CC.setTab(2);
            }
        });

        final Button checkButton = view.findViewById(R.id.checkContactsButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields()){
                    CC.setTab(2);
                }
            }
        });

        final Button backButton = view.findViewById(R.id.contactsBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CC.setTab(0);
            }
        });

        return view;
    }

    /**
     * initializes fields
     * @param v the view where field are located
     */
    private void initializeFields(View v){
        names.add((TextInputLayout)v.findViewById(R.id.contactName1));
        values.add((TextInputLayout) v.findViewById(R.id.contact1));
        names.add((TextInputLayout)v.findViewById(R.id.contactName2));
        values.add((TextInputLayout) v.findViewById(R.id.contact2));
        names.add((TextInputLayout)v.findViewById(R.id.contactName3));
        values.add((TextInputLayout) v.findViewById(R.id.contact3));
        names.add((TextInputLayout)v.findViewById(R.id.contactName4));
        values.add((TextInputLayout) v.findViewById(R.id.contact4));
        names.add((TextInputLayout)v.findViewById(R.id.contactName5));
        values.add((TextInputLayout) v.findViewById(R.id.contact5));
    }

    /**
     * Checks if all fields are of correct format.
     * @return true if everything is correct.
     */
    private boolean checkFields() {
        int indicator = 0;
        for(int i = 0; i < names.size(); i++){
            indicator += nameNumberOK(i);
        }
        return indicator == names.size();
    }

    /**
     * Checks if the input of i-th contact is correct
     * @param i the index of contact
     * @return 1 if format is correct, 0 otherwise
     */
    private int nameNumberOK(int i){

        TextInputLayout name = names.get(i);
        TextInputLayout number = values.get(i);

        String nr = number.getEditText().getText().toString();
        String nme = name.getEditText().getText().toString();

        if(nr.length() == 0){
            if(nme.length() == 0){
                number.setError(null);
                name.setError(null);
                return 1;
            }
            else{
                name.setError("Delete the name or fill in the number.");
                number.setError("Fill in the number for " + nme + " or delete the name.");
                return 0;
            }
        }
        else if(nr.length() == 9){
            try{
                Integer.parseInt(nr);
            }
            catch (NumberFormatException e){
                number.setError("Wrong number format.");
                return 0;
            }
            if(nme.length() > 0){
                number.setError(null);
                name.setError(null);
                return 1;
            }
            else{
                name.setError("Fill in the name or delete the number.");
                number.setError("Delete the number for " + nme + " or fill in the name.");
                return 0;
            }
        }
        else{
            number.setError("Wrong number format.");
            return 0;
        }
    }

    /**
     * @return a JSONArray with contacts
     */
    public JSONArray getContacts(){
        if(noContacts){
            return new JSONArray();
        }
        else{
            JSONArray ja = new JSONArray();
            for(int i = 0; i < names.size(); i++) {
                String nme = names.get(i).getEditText().getText().toString();
                if(!nme.isEmpty()){
                    String nr = values.get(i).getEditText().getText().toString();
                    try {
                        JSONObject jo = new JSONObject();

                        jo.put("name", nme);
                        jo.put("value", nr);
                        ja.put(jo);
                    }
                    catch (JSONException je){

                    }
                }

            }
            return ja;
        }
    }

    /**
     * @return true if the new configuration does not use contacts tab
     */
    public boolean noContacts(){
        return noContacts;
    }
}
