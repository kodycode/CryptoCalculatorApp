package com.example.kody.cryptocalculator;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CryptoCalculator extends AppCompatActivity {

    StringBuffer inputDisplay = new StringBuffer("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_crypto_calculator);
        updateResult();
    }

    public void updateResult() {
        TextView displayBox = (TextView)findViewById(R.id.textView);
        if (inputDisplay.charAt(inputDisplay.length() - 1) == '.') {
            inputDisplay.deleteCharAt(inputDisplay.length() - 1);
        }
        displayBox.setText(inputDisplay);
    }

    public void appendResult(View v) {
        Button inputButton = (Button)v;
        if (inputButton.getText().toString().equals("⌫")) {
            inputDisplay.deleteCharAt(inputDisplay.length() - 1);
        } else {
            inputDisplay.append(inputButton.getText().toString());
        }
        updateResult();
    }
}
