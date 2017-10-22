package com.codepath.virtualrobinhood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.models.Stock;
import com.codepath.virtualrobinhood.models.Trade;
import com.codepath.virtualrobinhood.utils.Constants;
import com.codepath.virtualrobinhood.utils.FireBaseClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StockBuyActivity extends AppCompatActivity {
    private double credit;
    DecimalFormat df = new DecimalFormat("##.##");

    private TextView tvAvailableCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_buy);

        Intent intent = getIntent();
        final Stock stock = Parcels.unwrap(intent.getParcelableExtra("stock"));
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FireBaseClient fireBaseClient = new FireBaseClient();
        getUserCredit(userId);

        final TextView tvSymbol = findViewById(R.id.tvSymbol);
        final TextView tvCompanyName = findViewById(R.id.tvCompanyName);
        tvAvailableCredit = findViewById(R.id.tvAvailableCredit);
        final TextView tvMarketPrice = findViewById(R.id.tvMarketPrice);
        final EditText etQuantity = findViewById(R.id.etQuantity);
        final TextView tvEstimatedCost = findViewById(R.id.tvEstimatedCost);
        final Button btnBuy = findViewById(R.id.btnBuy);

        tvSymbol.setText(stock.symbol);
        tvCompanyName.setText(stock.companyName);
        tvMarketPrice.setText(df.format(stock.getLastClosePrice()));

        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    tvEstimatedCost.setText("");
                    return;
                }
                int quantity = Integer.parseInt(s.toString());
                tvEstimatedCost.setText(df.format(quantity * stock.getLastClosePrice()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing.
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int buyQuantity = Integer.parseInt(etQuantity.getText().toString());

                if (buyQuantity * stock.getLastClosePrice() > credit) {
                    Toast.makeText(v.getContext(), "Estimated cost is more that available credit",
                            Toast.LENGTH_SHORT).show();
                }

                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                Trade trade = new Trade();
                trade.companyName = stock.companyName;
                trade.symbol = stock.symbol;
                trade.price = stock.getLastClosePrice();
                trade.quantity = buyQuantity;
                trade.submittedOn = date;
                trade.filledOn = date;
                trade.tradeType = Trade.TradeType.BUY;

                fireBaseClient.addPositionToPortfolio(userId, Constants.DEFAULT_PORTFOLIO, trade);
                fireBaseClient.addTradeToTransactions(userId, trade);
                fireBaseClient.updateCredit(userId, credit - buyQuantity * stock.getLastClosePrice());

                Toast.makeText(StockBuyActivity.this, "Stock purchased successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserCredit(String userId) {
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.child("users")
                .child(userId)
                .child("credit")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.getValue() != null) {
                    credit = Double.parseDouble(snapshot.getValue().toString());
                    tvAvailableCredit.setText(df.format(credit));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}