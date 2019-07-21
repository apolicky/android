package org.policky.ninthappgetmysqltable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RowAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] names, prices;

    RowAdapter(Context c, String[] n, String[] p){
        names = n;
        prices = p;

        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int i) {
        return names[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflater.inflate(R.layout.table_layout, null);

        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView priceTextView = (TextView) v.findViewById(R.id.priceTextView);

        nameTextView.setText(names[i]);
        priceTextView.setText(prices[i]);

        return v;
    }
}
