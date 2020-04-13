package org.policky.ghotaapp2019v2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Contacts extends Fragment {

    private Button callFirst, callSecond;
    private ConfigManager CM;

    private ListView contactListView;
    private ArrayList<String> contact_names, contact_numbers;

    public Contacts(ConfigManager CM_){
        CM = CM_;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_contacts_layout, container, false);

        contactListView = (ListView) view.findViewById(R.id.contacts_list_view);
        contactListView.setAdapter(new ContactsAdapter(
                getContext(),
                CM.getValues(getResources().getString(R.string.contact_names)),
                CM.getValues(getResources().getString(R.string.contact_numbers))));

        return view;
    }

    public void callNumber(Uri phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(phoneNumber);

        Log.d("call number:", phoneNumber.toString());

        startActivity(intent);
    }
}
