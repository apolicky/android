package org.policky.taborovyzpevnik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;

public class ShowSong extends AppCompatActivity {

    TextView pageTextView, authorSongTextView;
    TextView songEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_song);

        pageTextView = (TextView) findViewById(R.id.pageTextView);
        authorSongTextView = (TextView) findViewById(R.id.auhtorSongTextView);
        songEditText = (TextView) findViewById(R.id.textTextView);


        int fileIndex;
        String yearDir;

        if(getIntent().hasExtra("org.policky.taborovyzpevnik.fileindex") && getIntent().hasExtra("org.policky.taborovyzpevnik.yeardir")) {
            fileIndex = getIntent().getIntExtra("org.policky.taborovyzpevnik.fileindex",-1);
            yearDir = getIntent().getStringExtra("org.policky.taborovyzpevnik.yeardir");

            File directory = new File(yearDir);
            File wantedSong = directory.listFiles()[fileIndex];
            System.out.println(wantedSong.toString());

            FileInputStream FIS = null;
            BufferedReader BR = null;

            if(wantedSong != null){
                pageTextView.setText(wantedSong.getName().split("-")[0]);
                authorSongTextView.setText(wantedSong.getName().split("-")[1] + " - " + wantedSong.getName().split("-")[2]);
                try {
                    FIS = new FileInputStream(wantedSong); //openFileInput(wantedSong.toString());
                    BR = new BufferedReader(new InputStreamReader(FIS));
                    StringBuilder res = new StringBuilder();
                    String line;
                    while((line = BR.readLine()) != null){
                        res.append(line).append("\n");
                    }
                    songEditText.setText(res.toString());

                    BR.close();
                    FIS.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    try{
                        FIS.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try{
                        BR.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                songEditText.setText("file not found.\n");
            }
        }

    }


}
