package com.codepath.virtualrobinhood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.models.Trade;
import com.codepath.virtualrobinhood.utils.FireBaseClient;

public class StockDetailActivity extends AppCompatActivity {

    public static String EXTRA_STOCK_KEY = "stock_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        Intent intent = getIntent();
        String stockSymbol = intent.getStringExtra("stock_symbol");
        String stockPrice = intent.getStringExtra("stock_price");
        final String userId = intent.getStringExtra("user_id");

        TextView tvPriceDetail = null;
        TextView tvSymbolDetail = null;
        Button btnSubmit;
        final FireBaseClient fireBaseClient = new FireBaseClient();

        final Trade trade = new Trade();
        trade.symbol = stockSymbol;


        tvSymbolDetail  = findViewById(R.id.tvSymbolDetail);
        tvPriceDetail = findViewById(R.id.tvPriceDetail);

        tvSymbolDetail.setText(stockSymbol);
        tvPriceDetail.setText(stockPrice);

        final Button button = findViewById(R.id.btnBuy);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("debug", "debug");
                 fireBaseClient.addTradeToPortfolio(userId, "Saturday",
                                trade);
                // Code here executes on main thread after user presses button
            }
        });
    }





}
