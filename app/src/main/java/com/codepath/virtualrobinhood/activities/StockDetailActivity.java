package com.codepath.virtualrobinhood.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.virtualrobinhood.R;

public class StockDetailActivity extends AppCompatActivity {

    public static String EXTRA_STOCK_KEY = "stock_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
    }
}
