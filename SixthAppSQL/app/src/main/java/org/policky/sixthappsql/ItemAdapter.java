package org.policky.sixthappsql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    Map<String,Double> map;

    List<String> names;
    List<Double> prices;

    public ItemAdapter(Context context, Map m){
        map = m;
        names = new ArrayList<>(map.keySet());
        prices = new ArrayList<>(map.values());


        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int i) {
        return names.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflater.inflate(R.layout.item_layout, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView priceTextView = (TextView) v.findViewById(R.id.priceTextView);

        nameTextView.setText(names.get(i));
        priceTextView.setText("$" + prices.get(i).toString());

        return v;
    }
}