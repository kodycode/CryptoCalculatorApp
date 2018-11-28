package com.example.kody.cryptocalculator;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CryptoCalculator extends AppCompatActivity {

    StringBuffer inputDisplay;
    JSONObject tickerData;
    HashMap<String, List<Double>> cryptoData;
    boolean isEUR; // 1 if USD, 0 if EUR
    String currentFiat;
    Character fiatSymbol;
    String cryptoName;
    List<Double> cryptoPrice;
    // String APIKey = "57767db4-a9c8-4ba0-863e-7e299d41363a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        inputDisplay = new StringBuffer("0");
        tickerData = new JSONObject();
        cryptoData = new HashMap<>();
        isEUR = true;
        currentFiat = "USD";
        fiatSymbol = '$';
        cryptoName = "Bitcoin (BTC)";
        setContentView(R.layout.activity_crypto_calculator);
        getCryptoCurrencyData();
    }

    private void setCryptoSetting() {
        try {
            Button cryptoBtn = (Button)findViewById(R.id.cryptoBtn);
            Pattern pattern = Pattern.compile("(?<=\\().*(?=\\))");
            cryptoPrice = cryptoData.get(cryptoName);
            Matcher m = pattern.matcher(cryptoName);
            if (m.find()) {
                cryptoName = m.group(0);
                cryptoBtn.setText(cryptoName);
            }
            updateResult();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void getCryptoCurrencyData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String tickerURL ="https://api.coinmarketcap.com/v2/ticker/?limit=0&convert=EUR&sort=id";

        // Obtains all ticker data
        JsonObjectRequest tickerRequest = new JsonObjectRequest
                (Request.Method.GET, tickerURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        tickerData = response;
                        try {
                            JSONObject dataObject = tickerData.getJSONObject("data");
                            Iterator<?> keys = dataObject.keys();
                            while( keys.hasNext()) {
                                String key = (String) keys.next();
                                JSONObject currencyObject = dataObject.getJSONObject(key);
                                String currencyName = currencyObject.getString("name");
                                String currencySymbol = currencyObject.getString("symbol");
                                JSONObject currencyQuotes = currencyObject.getJSONObject("quotes");
                                // JSONObject currencyFiat = currencyQuotes.getJSONObject(fiat);
                                List<Double> fiatPrices = new ArrayList<>();
                                Iterator<?> quoteKeys = currencyQuotes.keys();
                                while (quoteKeys.hasNext()) {
                                    String fiat = (String) quoteKeys.next();
                                    fiatPrices.add(currencyQuotes.getJSONObject(fiat).getDouble("price"));
                                }
                                cryptoData.put(currencyName + " (" + currencySymbol + ")", fiatPrices);
                                // Double currencyPrice = currencyFiat.getDouble("price");
                            }
                            setCryptoSetting();
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

    public void search(View v) {
        Intent intent = new Intent(this, CryptoCurrencySearch.class);
        intent.putExtra("cryptoData", cryptoData);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                cryptoName = data.getStringExtra("currencyName");
                setCryptoSetting();
            }
        }
    }

    public void updateResult() {
        TextView displayBox = (TextView)findViewById(R.id.textView);
        if (!cryptoPrice.isEmpty()) {
            String formattedResult = String.format("%.8f", Double.parseDouble(String.valueOf(inputDisplay)) / cryptoPrice.get(isEUR ? 1 : 0));
            
            displayBox.setText(fiatSymbol.toString() + inputDisplay + "\n" + currentFiat + " ⇨ " + cryptoName + "\n" + formattedResult);
        } else {
            displayBox.setText(inputDisplay);
        }
    }

    public void appendResult(View v) {
        Button inputButton = (Button)v;
        if (inputButton.getText().toString().equals("⌫")) {
            if (inputDisplay.length() <= 1) {
                inputDisplay.setCharAt(0, '0');
            } else {
                inputDisplay.deleteCharAt(inputDisplay.length() - 1);
            }
        }
        else if (inputDisplay.length() < 16) {
            if (inputDisplay.indexOf(".") != -1 &&
                    (inputDisplay.length()-1) - inputDisplay.indexOf(".") >= 2) {
                return;
            } else if (inputButton.getText().toString().equals(".") &&
                    inputDisplay.indexOf(".") > -1) {
                return;
            } else {
                if (inputDisplay.length() == 1 && inputDisplay.charAt(0) == '0' &&
                        !inputButton.getText().toString().equals(".")) {
                    inputDisplay.replace(0, 1, inputButton.getText().toString());
                } else {
                    inputDisplay.append(inputButton.getText().toString());
                }
            }
        }
        updateResult();
    }

    public void clearResult(View v) {
        inputDisplay = new StringBuffer("0");
        updateResult();
    }

    public void changeFiat(View v) {
        Button fiatBtn = (Button)findViewById(R.id.fiatBtn);
        if (isEUR) {
            currentFiat = "USD";
            fiatSymbol = '$';
            isEUR = false;
        } else {
            currentFiat = "EUR";
            fiatSymbol = '€';
            isEUR = true;
        }
        fiatBtn.setText(currentFiat);
        updateResult();
    }
}
