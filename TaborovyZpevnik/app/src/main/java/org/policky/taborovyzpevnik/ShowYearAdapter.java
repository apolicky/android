package org.policky.taborovyzpevnik;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;

public class ShowYearAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    File yearDirectory;
    Context context;

    String[] fileNames;

    ShowYearAdapter(Context c, File yD){
        context = c;
        yearDirectory = yD;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        getSongs();
    }

    @Override
    public int getCount() {
        return fileNames.length;
    }

    @Override
    public Object getItem(int i) {
        return fileNames[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = (View) mInflater.inflate(R.layout.show_year_layout, null);

        final String fileName = fileNames[i];

        final TextView pageNumberTextView = (TextView) v.findViewById(R.id.pageNumberTextView);
        pageNumberTextView.setText(fileNames[i].split("-")[0]);

        final TextView authorTextView = (TextView) v.findViewById(R.id.authorTextView);
        authorTextView.setText(fileNames[i].split("-")[1]);

        final TextView songTextView = (TextView) v.findViewById(R.id.songNameTextView);
        songTextView.setText(fileNames[i].split("-")[2]);

        return v;
    }

    private void getSongs(){
        int numberOfItems = yearDirectory.listFiles().length;
        fileNames = new String[numberOfItems];

        int i = 0;
        for(File f : yearDirectory.listFiles()){
            fileNames[i] = f.getName();
            i++;
        }

    }
}
