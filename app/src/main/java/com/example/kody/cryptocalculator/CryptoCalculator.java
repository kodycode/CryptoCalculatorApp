package com.example.kody.cryptocalculator;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CryptoCalculator extends AppCompatActivity {

    StringBuffer inputDisplay = new StringBuffer("0");
    JSONObject cryptoData = new JSONObject();
    // String APIKey = "57767db4-a9c8-4ba0-863e-7e299d41363a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_crypto_calculator);
        updateResult();
        getCryptoCurrencyData();
    }

    private void getCryptoCurrencyData() {
        final TextView displayBox = (TextView)findViewById(R.id.textView);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.coinmarketcap.com/v2/listings/";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cryptoData = response;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Error Alert..
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void updateResult() {
        TextView displayBox = (TextView)findViewById(R.id.textView);
        displayBox.setText(inputDisplay);
    }

    public void appendResult(View v) {
        Button inputButton = (Button)v;
        if (inputButton.getText().toString().equals(".") && inputDisplay.indexOf(".") > -1) {
            return;
        }
        else if (inputButton.getText().toString().equals("⌫")) {
            if (inputDisplay.length() == 0) {
                return;
            }
            inputDisplay.deleteCharAt(inputDisplay.length() - 1);
        } else {
            if (inputDisplay.length() == 1 && inputDisplay.charAt(0) == '0') {
                inputDisplay.replace(0,1, inputButton.getText().toString());
            } else {
                inputDisplay.append(inputButton.getText().toString());
            }
        }
        updateResult();
    }

    public void clearResult(View v) {
        inputDisplay = new StringBuffer("0");
        updateResult();
    }
}
