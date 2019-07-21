package org.policky.fourthappactivities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if(getIntent().hasExtra("org.policky.fourthappactivities"/*.fmaintsecond"*/)){
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText(getIntent().getStringExtra("org.policky.fourthappactivites.fmaintsecond"));

        }
        else{
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText("nothing was passed");
        }
    }
}
