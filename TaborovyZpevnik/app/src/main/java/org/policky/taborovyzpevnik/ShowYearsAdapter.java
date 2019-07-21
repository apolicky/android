package org.policky.taborovyzpevnik;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class ShowYearsAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    List<String> years;
    Context context;


    ShowYearsAdapter(Context c, List<String> y){
        context = c;
        years = y;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return years.size();
    }

    @Override
    public Object getItem(int i) {
        return years.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = (View) mInflater.inflate(R.layout.show_years_layout, null);

        final TextView yearTextView = (TextView) v.findViewById(R.id.showYearTextView);
        yearTextView.setText(years.get(i));

        final String year = years.get(i);

        /*v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowYear.class);
                intent.putExtra("org.policky.taborovyzpevnik.year", year);
                context.startActivity(intent);
            }
        });
        */
        return v;
    }
}
