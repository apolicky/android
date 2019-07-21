package org.policky.taborovyzpevnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button getZpevnik, delZpevnik;
    ListView mainZpevnikListView;
    File rootDir;
    FloatingActionButton reloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getZpevnik = (Button) findViewById(R.id.getZpevnikButton);
        delZpevnik = (Button) findViewById(R.id.delZpevnikButton);
        mainZpevnikListView = (ListView) findViewById(R.id.mainZpevnikListView);
        reloadButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        //
        final List<String> downloadedYears, downloadedYearsPaths;
        rootDir = getFilesDir();
        downloadedYears = new ArrayList<>();
        downloadedYearsPaths = new ArrayList<>();

        updateZpevnikListView(downloadedYearsPaths, downloadedYears);

        mainZpevnikListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ShowYear.class);
                intent.putExtra("org.policky.taborovyzpevnik.yeardir", downloadedYearsPaths.get(i));
                startActivity(intent);
            }
        });

        getZpevnik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AvailibleYears.class);
                startActivity(i);
                //updateZpevnikListView(downloadedYearsPaths,downloadedYears);
            }
        });

        delZpevnik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DeletableYears.class);
                startActivity(i);
                //updateZpevnikListView(downloadedYearsPaths,downloadedYears);
            }
        });

        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateZpevnikListView(downloadedYearsPaths,downloadedYears);
            }
        });

    }

    private void updateZpevnikListView(List downloadedYearsPaths, List downloadedYears){
        downloadedYears.clear();
        downloadedYearsPaths.clear();

        for(File f : rootDir.listFiles()) {
            downloadedYears.add(f.getName());
            downloadedYearsPaths.add(f.getAbsolutePath());
        }

        ShowYearsAdapter showYearsAdapter = new ShowYearsAdapter(getApplicationContext(), downloadedYears);
        mainZpevnikListView.setAdapter(showYearsAdapter);
    }


}
