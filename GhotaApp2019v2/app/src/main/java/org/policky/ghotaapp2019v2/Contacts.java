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

//    private Button callFirst, callSecond;
    private ConfigManager CM;

    private ListView contactListView;

    public Contacts(ConfigManager CM_){
        CM = CM_;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_contacts_layout, container, false);

        contactListView = (ListView) view.findViewById(R.id.contacts_list_view);
        refreshContactListView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshContactListView();
    }

    private void refreshContactListView(){
        ContactsAdapter ca = new ContactsAdapter(getContext(),
                CM.getValueMap("contacts"));
        contactListView.setAdapter(ca);
    }

    //
//    public void callNumber(Uri phoneNumber){
//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        intent.setData(phoneNumber);
//
//        Log.d("call number:", phoneNumber.toString());
//
//        startActivity(intent);
//    }
}
