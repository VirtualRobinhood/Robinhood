package com.codepath.virtualrobinhood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.models.Stock;

import org.parceler.Parcels;

public class StockTradeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_trade);

        Intent intent = getIntent();
        final Stock stock = Parcels.unwrap(intent.getParcelableExtra("stock"));
        Log.d("debug", "stock");

    }
}
