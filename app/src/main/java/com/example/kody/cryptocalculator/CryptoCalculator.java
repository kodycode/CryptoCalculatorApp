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

import java.util.Iterator;

public class CryptoCalculator extends AppCompatActivity {

    StringBuffer inputDisplay = new StringBuffer("0");
    // JSONObject listingData = new JSONObject();
    JSONObject tickerData = new JSONObject();
    // String APIKey = "57767db4-a9c8-4ba0-863e-7e299d41363a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_crypto_calculator);
        getCryptoCurrencyData();
        updateResult();
    }

    private void getCryptoCurrencyData() {
        final TextView displayBox = (TextView)findViewById(R.id.textView);
        RequestQueue queue = Volley.newRequestQueue(this);
        String tickerURL ="https://api.coinmarketcap.com/v2/ticker/";

        // Obtains all ticker data
        JsonObjectRequest tickerRequest = new JsonObjectRequest
                (Request.Method.GET, tickerURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        tickerData = response;
                        try {
                            JSONObject dataObject = tickerData.getJSONObject("data");
                            Iterator<?> keys = dataObject.keys();
                            String key = (String) keys.next();
                            JSONObject currencyObject = dataObject.getJSONObject(key);
                            // System.out.println(currencyObject.toString());
                            String currencyName = currencyObject.getString("name");
                            String currencySymbol = currencyObject.getString("symbol");
                            int currencyID = currencyObject.getInt("id");
                            System.out.println(currencyName + " (" + currencySymbol + ")");
                            /*
                            while( keys.hasNext() ) {
                                String key = (String) keys.next();
                                System.out.println("Key: " + key);
                                System.out.println("Value: " + json_array.get(key));
                            }
                            */
                        } catch (JSONException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(tickerRequest);
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
