package org.policky.ghotaapp2019v2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ContactsAdapter extends BaseAdapter {
    ArrayMap<String,String> names_numbers;

    Context context;
    LayoutInflater mInflater;

    ContactsAdapter(Context cont, ArrayMap<String,String> names_numbers_){
        context = cont;
        names_numbers = names_numbers_;
        mInflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names_numbers.size();
    }

    @Override
    public Object getItem(int i) {
        return names_numbers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = (View) mInflater.inflate(R.layout.contacts_adapter_l, null);

        final TextView contact_name_text_view = (TextView) v.findViewById(R.id.cont_ada_cont_name_text_view);
        contact_name_text_view.setText(names_numbers.keyAt(i));

        Button call_contact_btn = (Button) v.findViewById(R.id.cont_ada_call_cont_btn);

        call_contact_btn.setOnClickListener(new ArgumentedOnClickListener(i){
            @Override
            public void onClick(View v) {
                callNumber(argument);
            }
        });

        return  v;
    }



    private void callNumber(int i){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + names_numbers.get(names_numbers.keyAt(i))));
        context.startActivity(callIntent);
    }
}

