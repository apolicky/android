package org.policky.seventhappfiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    String filename = "tmp.txt";
    EditText input;
    Button saveButton, readButton, showDirsButton, makeDirButton;
    TextView output;

    File rootFolder;
    int id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.inputEditText);
        saveButton = (Button) findViewById(R.id.saveButton);
        readButton = (Button) findViewById(R.id.readButton);
        showDirsButton = (Button) findViewById(R.id.showDirectoriesButton);
        makeDirButton = (Button) findViewById(R.id.makeDirButton);
        output = (TextView) findViewById(R.id.outputTextView);

        rootFolder = getApplicationContext().getFilesDir();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeFile();
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFile();
            }
        });

        showDirsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder SB = new StringBuilder();

                SB.append(getFilesDir().toString()).append(":\n");

                for(File f : rootFolder.listFiles()){

                    if(f.isDirectory()) {
                        SB.append("dir ").append(f.getName()).append("\n");
                    }
                    else{
                        SB.append(f.getName()).append("\n");
                    }
                }

                SB.append(getFilesDir().getAbsolutePath());

                output.setText(SB.toString());
            }
        });

        makeDirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String documents = "doc"+id;
                id++;

                String intStorageDirectory = getFilesDir().toString();
                File folder = new File(intStorageDirectory, "testthreepdf" + id);
                folder.mkdirs();

                //File myDir = getDir(,MODE_PRIVATE);
                /*FileOutputStream FOS = null;

                try {
                    FOS = openFileOutput(documents, MODE_PRIVATE);
                    FOS.write("hello".getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        FOS.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
            }
        });
    }

    private void readFile() {
        try {
            BufferedReader BR = new BufferedReader(new InputStreamReader(openFileInput(filename)));
            StringBuilder SB = new StringBuilder();

            String line;

            while((line = BR.readLine()) != null){
                SB.append(line).append("\n");
            }

            output.setText(SB.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(){
        try {
            FileOutputStream FOS = openFileOutput(filename,MODE_APPEND);
            FOS.write(input.getText().append("\n").toString().getBytes());
            FOS.close();

            Toast.makeText(getApplicationContext(), "Text saved.", Toast.LENGTH_SHORT).show();

            input.setText("");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
