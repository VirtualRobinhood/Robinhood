package com.codepath.virtualrobinhood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.models.History;
import com.codepath.virtualrobinhood.models.Stock;
import com.codepath.virtualrobinhood.models.Trade;
import com.codepath.virtualrobinhood.utils.FireBaseClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StockBuyActivity extends AppCompatActivity {
    public static int currentStockQuantity;
    public static long lastHistory;

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

        getPortfolio(userId, stock.symbol);
        getHistory(userId);


        final Button btnBuyStock = findViewById(R.id.btnBuyStock);
        final TextView tvPrice = findViewById(R.id.tvMktPriceValueBuy);
        final EditText etQuantity = findViewById(R.id.etQuantity);
        tvPrice.setText(trade.price.toString());

        final History stockHistory = new History();


        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());


        stockHistory.symbol = trade.symbol;
        stockHistory.stockPrice = trade.price;
        stockHistory.date = date;
        stockHistory.buySell = "Market Buy";


        btnBuyStock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("debug", "debug");
                int buyQuantity = Integer.parseInt(etQuantity.getText().toString());
                trade.quantity = currentStockQuantity + buyQuantity;
                fireBaseClient.addTradeToPortfolio(userId, "TestPortfolio",
                        trade);
                stockHistory.quantity = buyQuantity;
                fireBaseClient.addToHistory(userId, stockHistory, lastHistory+1);
                lastHistory = lastHistory + 1;

                Toast.makeText(v.getContext(), "Stock purchased successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPortfolio(String userId, String stockSymbol) {
        //final Double amount;

        final FirebaseDatabase database;
        final DatabaseReference dbRef;

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        dbRef.child("users")
                .child(userId)
                .child("portfolios")
                .child("TestPortfolio")
                .child("stocks")
                .child(stockSymbol)
                .child("quantity")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("debug", "onDataChange");
                if (snapshot != null && snapshot.getValue() != null) {
                    String quantity = snapshot.getValue().toString();
                    currentStockQuantity = Integer.parseInt(quantity);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getHistory(String userId) {
        //final Double amount;

        final FirebaseDatabase database;
        final DatabaseReference dbRef;

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        dbRef.child("users")
                .child(userId)
                .child("history")
                .child("TestHistory")
                .child("stocks")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d("debug", "onDataChange");
                        if (snapshot != null && snapshot.getValue() != null) {
                            Log.d("debug", "onDataChange");
                            //String quantity = snapshot.getValue().toString();
                            lastHistory = snapshot.getChildrenCount();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}
