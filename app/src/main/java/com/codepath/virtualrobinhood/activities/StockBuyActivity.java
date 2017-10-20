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

public class StockBuyActivity extends AppCompatActivity {
    public static int currentStockQuantity;
    public static long lastHistory;
    public Double stockPrice;

    TextView etEstimatedCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_buy);

        Intent intent = getIntent();

        final Stock stock = Parcels.unwrap(intent.getParcelableExtra("stock"));
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final Trade trade = new Trade();
        final FireBaseClient fireBaseClient = new FireBaseClient();
        final Button btnBuyStock = findViewById(R.id.btnBuyStock);
        final TextView tvPrice = findViewById(R.id.tvMktPriceValueBuy);
        final EditText etQuantity = findViewById(R.id.etQuantity);
        final History stockHistory = new History();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        etEstimatedCredit = findViewById(R.id.tvEstCreditValueBuy);

        getPortfolio(userId, stock.symbol);
        getHistory(userId);

        trade.symbol = stock.symbol;
        trade.price = stock.getLastClosePrice();
        trade.price = round(trade.price, 2);

        stockPrice = trade.price;

        etQuantity.addTextChangedListener(mTextEditorWatcher);
        tvPrice.setText(trade.price.toString());

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

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String quantity = s.toString();

            if (quantity.isEmpty()) {
                etEstimatedCredit.setText("0");
                return;
            }

            int price = stockPrice.intValue() * Integer.parseInt(quantity);

            etEstimatedCredit.setText(Integer.toString(price));
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
}
