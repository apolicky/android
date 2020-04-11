package org.policky.jiri_vstavani;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    File root_folder;
    int jiriho_prachy;
    TextView jiri_kapesne_TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button vzhuru_button = (Button) findViewById(R.id.button);
        vzhuru_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jiri_klikl(view);
            }
        });

        jiri_kapesne_TV = (TextView) findViewById(R.id.curr_money_val);

        root_folder = getApplicationContext().getFilesDir();

        nahraj_prachy();

        Thread t = new Thread(){
            @Override
            public void run() {
                try{
                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView curr_time_view = (TextView) findViewById(R.id.currTimeTV);
                                        long date = System.currentTimeMillis();
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                                        String time_str = sdf.format(date);
                                        curr_time_view.setText(time_str);
                                    }
                                }
                        );
                    }
                }
                catch(InterruptedException e){

                }
            }
        };
        t.start();
    }

    private void jiri_klikl(View v){

    }

    private void nahraj_prachy(){
        root_folder.
    }

}
