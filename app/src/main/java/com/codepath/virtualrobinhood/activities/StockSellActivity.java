package com.codepath.virtualrobinhood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codepath.virtualrobinhood.activities.StockBuyActivity.lastHistory;

public class StockSellActivity extends AppCompatActivity {

    public static int currentStockQuantity;
    TextView tvEstCreditValueSell;
    public Double stockPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_sell);

        Intent intent = getIntent();
        final Stock stock = Parcels.unwrap(intent.getParcelableExtra("stock"));
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d("debug", "stock");

        tvEstCreditValueSell = findViewById(R.id.tvEstCreditValueSell);

        final Trade trade = new Trade();
        trade.symbol = stock.symbol;
        trade.price = stock.getLastClosePrice();
        final FireBaseClient fireBaseClient = new FireBaseClient();
        getPortfolio(userId, stock.symbol);


        final Button btnBuyStock = findViewById(R.id.btnSellStock);
        final TextView tvPrice = findViewById(R.id.tvMktPriceValueSell);
        final EditText etQuantity = findViewById(R.id.etQuantitySell);
        etQuantity.addTextChangedListener(mTextEditorWatcher);

        trade.price = round(trade.price,2);
        stockPrice = trade.price;
        tvPrice.setText(trade.price.toString());

        final History stockHistory = new History();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());


        stockHistory.symbol = trade.symbol;
        stockHistory.stockPrice = trade.price;
        stockHistory.date = date;
        stockHistory.buySell = "Market Sell";

        getHistory(userId);

        btnBuyStock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("debug", "debug");
                int sellQuantity = Integer.parseInt(etQuantity.getText().toString());
                trade.quantity = currentStockQuantity - sellQuantity;

                stockHistory.quantity = sellQuantity;
                fireBaseClient.removeTradeFromPortfolio(userId, "TestPortfolio",
                        trade);
                fireBaseClient.addToHistory(userId, stockHistory, lastHistory+1);
                Toast.makeText(v.getContext(), "Stock sold successfully", Toast.LENGTH_SHORT).show();
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

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //This sets a textview to the current length

            String quantity = s.toString();
            if (quantity.isEmpty()) {
                tvEstCreditValueSell.setText("0");
                return;
            }
            Log.d("debug", "quantity");
            Log.d("debug", quantity);

            int price = stockPrice.intValue() * Integer.parseInt(quantity);

            tvEstCreditValueSell.setText(Integer.toString(price));
        }

        public void afterTextChanged(Editable s) {
        }
    };

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
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
