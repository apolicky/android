package cz.apolicky.summer_camp2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SelectConfig extends AppCompatActivity {

    TextView CurrConf = (TextView) findViewById(R.id.sel_conf_curr);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_config);

        listResources();
    }

    private void listResources(){
        CurrConf.setText("Hey.");
    }
}
