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
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class CryptoCalculator extends AppCompatActivity {

    StringBuffer inputDisplay = new StringBuffer("0");
    JSONObject tickerData = new JSONObject();
    Map<String, Double> cryptoData = new HashMap<String, Double>();
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
        String tickerURL ="https://api.coinmarketcap.com/v2/ticker/?limit=0";

        // Obtains all ticker data
        JsonObjectRequest tickerRequest = new JsonObjectRequest
                (Request.Method.GET, tickerURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        tickerData = response;
                        try {
                            JSONObject dataObject = tickerData.getJSONObject("data");
                            Iterator<?> keys = dataObject.keys();
                            int count = 0;
                            while( keys.hasNext()) {
                                count++;
                                String key = (String) keys.next();
                                JSONObject currencyObject = dataObject.getJSONObject(key);
                                String currencyName = currencyObject.getString("name");
                                String currencySymbol = currencyObject.getString("symbol");
                                JSONObject currencyQuotes = currencyObject.getJSONObject("quotes");
                                JSONObject currencyFiat = currencyQuotes.getJSONObject("USD");
                                Double currencyPrice = currencyFiat.getDouble("price");
                                cryptoData.put(currencyName + " (" + currencySymbol + ")", currencyPrice);
                            }
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
