package org.policky.fourthappactivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button secondActivityButton = (Button) findViewById(R.id.activityButton);
        secondActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent secondActivityIntent = new Intent(getApplicationContext(), SecondActivity.class);
                secondActivityIntent.putExtra("org.policky.fourthappactivities.fmaintsecond", "Ahoj kamo");
                startActivity(secondActivityIntent);
            }
        });

        final Button googleActivityButton = (Button) findViewById(R.id.googleButton);
        googleActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String google = "https://www.google.com";
                Uri webGoogle = Uri.parse(google);

                Intent googleIntent = new Intent(Intent.ACTION_VIEW, webGoogle);
                if(googleIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(googleIntent);
                }
            }
        });

    }
}
