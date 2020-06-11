package cz.apolicky.summercampapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * An adapter class used for Contacts fragment
 */
public class ContactsAdapter extends BaseAdapter {
    private ArrayMap<String,String> namesAndNumbers;

    private Context context;
    private LayoutInflater mInflater;

    /**
     * Constructor.
     * @param cont application context
     * @param names_numbers_ a map with names and their numbers
     */
    ContactsAdapter(Context cont, ArrayMap<String,String> names_numbers_){
        context = cont;
        namesAndNumbers = names_numbers_;
        mInflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return namesAndNumbers.size();
    }

    @Override
    public Object getItem(int i) {
        return namesAndNumbers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflater.inflate(R.layout.contacts_adapter, null);

        final TextView contactNameTextView = v.findViewById(R.id.cont_ada_cont_name_text_view);
        contactNameTextView.setText(namesAndNumbers.keyAt(i));

        ImageButton callButton = v.findViewById(R.id.cont_ada_call_cont_btn);

        callButton.setOnClickListener(new ArgumentedOnClickListener(i){
            @Override
            public void onClick(View v) {
                callNumber(argument);
            }
        });

        return  v;
    }

    /**
     * Moves the user to appropriate application where he can call the i-th number
     * @param i an index of number pressed
     */
    private void callNumber(int i){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + namesAndNumbers.get(namesAndNumbers.keyAt(i))));
        context.startActivity(callIntent);
    }
}

