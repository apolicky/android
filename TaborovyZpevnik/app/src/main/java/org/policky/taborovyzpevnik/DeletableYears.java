package org.policky.taborovyzpevnik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeletableYears extends AppCompatActivity {

    List<String> downloadedZpevnikYears;
    ListView deletableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletable_years);

        deletableListView = (ListView) findViewById(R.id.deletableListView);
        downloadedZpevnikYears = new ArrayList<>();

        getDownloadedZpevniks();

        DownloadDeleteYearAdapter downloadDeleteYearAdapter = new DownloadDeleteYearAdapter(
                getApplicationContext(),
                downloadedZpevnikYears,
                ButtonType.DELETE,
                getFilesDir());
        deletableListView.setAdapter(downloadDeleteYearAdapter);

    }

    private void getDownloadedZpevniks(){
        File rootDirectory = getFilesDir();

        for(File f : rootDirectory.listFiles()){
            if(f.isDirectory()){
                downloadedZpevnikYears.add(f.getName());
            }
        }

    }
}
