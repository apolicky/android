package org.policky.thirdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button multiplyButton = (Button) findViewById(R.id.multButton);
        multiplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firstNumber = (EditText) findViewById(R.id.firstNumberEditText);
                EditText secondNumber = (EditText) findViewById(R.id.secondNumberEditText);
                TextView result = (TextView) findViewById(R.id.resultTextView);

                int res = (Integer.parseInt(firstNumber.getText().toString())) * (Integer.parseInt(secondNumber.getText().toString()));

                result.setText(res+ "");
            }
        });
    }
}
