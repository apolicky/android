package org.policky.ghotaapp2019v2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Contacts extends Fragment {

    private Button callFirst, callSecond;
    private ConfigManager CM;

    public Contacts(ConfigManager CM_){
        CM = CM_;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_contacts_layout, container, false);

        callFirst = (Button) view.findViewById(R.id.callFirstButton);
        callSecond = (Button) view.findViewById(R.id.callSecondButton);
        final String firstNumber, secondNumber;
        firstNumber = CM.getValue("first_phone_number");
        secondNumber = CM.getValue("second_phone_number");

        callFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNumber(Uri.parse("tel:" + firstNumber));
            }
        });

        callSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNumber(Uri.parse("tel:" + secondNumber));
            }
        });

        return view;
    }

    public void callNumber(Uri phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(phoneNumber);

        Log.d("call number:", phoneNumber.toString());

        startActivity(intent);
    }
}
