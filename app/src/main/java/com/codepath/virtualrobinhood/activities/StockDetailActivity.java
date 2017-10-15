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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StockDetailActivity extends AppCompatActivity {

    public static String EXTRA_STOCK_KEY = "stock_key";
    public static double depositAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        Intent intent = getIntent();
        String stockSymbol = intent.getStringExtra("stock_symbol");
        final String stockPrice = intent.getStringExtra("stock_price");
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

        /* testing start */

        //fireBaseClient.getDeposit(userId);
        getDeposit1(userId);

        /* test end */

        final Button button = findViewById(R.id.btnBuy);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("debug", "debug");
                if (Double.parseDouble(stockPrice) > depositAmount) {
                    Log.d("debug", "insufficient funds");
                }
                 fireBaseClient.addTradeToPortfolio(userId, "Saturday",
                                trade);
                // Code here executes on main thread after user presses button
            }
        });
    }

    public void getDeposit1(String userId) {
        //final Double amount;

        final FirebaseDatabase database;
        final DatabaseReference dbRef;

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        dbRef.child("users").child(userId).child("amount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("debug", "onDataChange");
                if (snapshot != null) {
                    Double amount = Double.parseDouble(snapshot.getValue().toString());
                    Log.d("debug", "onDataChange");
                    depositAmount = amount;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }





}
