package org.policky.seventhappfiles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class showDirsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dirs);

        TextView textView = (TextView) findViewById(R.id.outputTextView);
        textView.setText(getFilesDir().getName());
    }
}
