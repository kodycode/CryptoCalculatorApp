package com.example.kody.cryptocalculator;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;

public class CryptoCurrencySearch extends AppCompatActivity {

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_currency_search);
        ConstraintLayout constraintlayout = new ConstraintLayout(this);
        ListView DynamicListView = new ListView(this);
        final ArrayList<String> arrayCryptoCurrency = new ArrayList<>();
        final HashMap<String, Double> cryptoData = (HashMap<String, Double>)getIntent().getSerializableExtra("cryptoData");
        arrayCryptoCurrency.addAll(cryptoData.keySet());

        adapter = new ArrayAdapter<>(
                CryptoCurrencySearch.this,
                android.R.layout.simple_list_item_1,
                arrayCryptoCurrency
        );
        DynamicListView.setAdapter(adapter);

        constraintlayout.addView(DynamicListView);
        this.setContentView(constraintlayout, new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));

        DynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("currencyName", arrayCryptoCurrency.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setQueryHint("Enter Cryptocurrency..");
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent();
                intent.putExtra("currencyData", "Bitcoin");
                setResult(RESULT_OK, intent);
                finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
