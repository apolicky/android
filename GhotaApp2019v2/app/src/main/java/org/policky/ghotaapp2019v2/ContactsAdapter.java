package org.policky.ghotaapp2019v2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ContactsAdapter extends BaseAdapter {

    List<String> names,numbers;

    int contact_used;

    Context context;
    File rootDir;
    LayoutInflater mInflater;

    ContactsAdapter(Context cont, List<String> names_, List<String> numbers_){
        context = cont;
        names = names_;
        numbers = numbers_;
        mInflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int i) {
        return numbers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = (View) mInflater.inflate(R.layout.select_config_l, null);

        contact_used = i;

        final TextView contact_name_text_view = (TextView) v.findViewById(R.id.cont_ada_cont_name_text_view);
        contact_name_text_view.setText(names.get(i));

        Button call_contact_btn = (Button) v.findViewById(R.id.cont_ada_call_cont_btn);

        call_contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNumber();
            }
        });

        return  v;
    }

    private void callNumber(){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + numbers.get(contact_used)));
        context.startActivity(callIntent);
    }
}

