package com.codepath.virtualrobinhood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.models.Stock;
import com.codepath.virtualrobinhood.models.Trade;
import com.codepath.virtualrobinhood.utils.FireBaseClient;
import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

public class StockBuyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_buy);

        Intent intent = getIntent();
        final Stock stock = Parcels.unwrap(intent.getParcelableExtra("stock"));
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d("debug", "stock");

        final Trade trade = new Trade();
        trade.symbol = stock.symbol;
        trade.price = stock.getLastClosePrice();
        final FireBaseClient fireBaseClient = new FireBaseClient();


        final Button btnBuyStock = findViewById(R.id.btnBuyStock);
        btnBuyStock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("debug", "debug");
                fireBaseClient.addTradeToPortfolio(userId, "TestPortfolio",
                        trade);
            }
        });

    }
}
