package org.policky.taborovyzpevnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;

public class ShowYear extends AppCompatActivity {

    ListView showYearListView;
    //String[] filenames;
    String yearDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_year);

        showYearListView = (ListView) findViewById(R.id.showYearListView);

        if(getIntent().hasExtra("org.policky.taborovyzpevnik.yeardir")){
            File directory = new File(getIntent().getStringExtra("org.policky.taborovyzpevnik.yeardir"));
            yearDir = getIntent().getStringExtra("org.policky.taborovyzpevnik.yeardir");

            ShowYearAdapter showYearAdapter = new ShowYearAdapter(getApplicationContext(), directory);
            showYearListView.setAdapter(showYearAdapter);

        }

        showYearListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ShowSong.class);
                intent.putExtra("org.policky.taborovyzpevnik.fileindex", i);
                intent.putExtra("org.policky.taborovyzpevnik.yeardir", yearDir);
                startActivity(intent);
            }
        });

    }
}
