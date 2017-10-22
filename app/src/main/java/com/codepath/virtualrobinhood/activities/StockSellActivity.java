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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockSellActivity extends AppCompatActivity {
    private List<Trade> positions = new ArrayList<>();
    private double credit;
    private Trade trade;
    private DecimalFormat df = new DecimalFormat("##.##");

    private TextView tvQuantity;
    private TextView tvEstimatedCost;
    private TextView tvEstimatedGain;
    private TextView tvCostPareShare;
    private EditText etQuantityToSell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_sell);

        Intent intent = getIntent();
        trade = Parcels.unwrap(intent.getParcelableExtra("trade"));
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final FireBaseClient fireBaseClient = new FireBaseClient();
        final TextView tvSymbol = findViewById(R.id.tvSymbol);
        final TextView tvCompanyName = findViewById(R.id.tvCompanyName);
        tvQuantity = findViewById(R.id.tvQuantity);
        final TextView tvMarketPrice = findViewById(R.id.tvMarketPrice);
        etQuantityToSell = findViewById(R.id.etQuantityToSell);
        tvEstimatedCost = findViewById(R.id.tvEstimatedCost);
        tvEstimatedGain = findViewById(R.id.tvEstimatedGain);
        tvCostPareShare = findViewById(R.id.tvCostPareShare);
        final Button btnSell = findViewById(R.id.btnSell);

        tvSymbol.setText(trade.symbol);
        tvCompanyName.setText(trade.companyName);
        tvMarketPrice.setText(df.format(trade.price));
        tvQuantity.setText(String.valueOf(trade.quantity));
        tvCostPareShare.setText(df.format(trade.price));

        getUserPortfolio(userId);
        getUserCredit(userId);

        etQuantityToSell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    tvEstimatedCost.setText("");
                    tvEstimatedGain.setText("");
                    return;
                }

                int quantity = Integer.parseInt(s.toString());
                double estimatedCost = quantity * trade.price;
                tvEstimatedCost.setText(df.format(estimatedCost));
                tvEstimatedGain.setText(df.format(estimatedCost));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing.
            }
        });

        btnSell.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (etQuantityToSell.getText().toString().isEmpty()) {
                    Toast.makeText(StockSellActivity.this, "Please enter valid integer",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int sellQuantity = Integer.parseInt(etQuantityToSell.getText().toString());

                if (sellQuantity > trade.quantity) {
                    Toast.makeText(v.getContext(), "Count is more than available stocks.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                Trade t = new Trade();
                t.companyName = trade.companyName;
                t.symbol = trade.symbol;
                t.price = trade.price;
                t.quantity = sellQuantity;
                t.submittedOn = date;
                t.filledOn = date;
                t.tradeType = Trade.TradeType.SELL;

                if (sellQuantity == trade.quantity) {
                    fireBaseClient.removePositionFromPortfolio(userId, Constants.DEFAULT_PORTFOLIO,
                            trade);
                } else {
                    fireBaseClient.updatePosition(userId, Constants.DEFAULT_PORTFOLIO,
                            trade, trade.quantity - sellQuantity);
                }

                fireBaseClient.addTradeToTransactions(userId, t);
                fireBaseClient.updateCredit(userId, credit + trade.price * trade.quantity);

                Toast.makeText(StockSellActivity.this, "Stock sold successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserPortfolio(String userId) {
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.child("users")
                .child(userId)
                .child("portfolios")
                .child(Constants.DEFAULT_PORTFOLIO)
                .child("positions")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.getValue() != null) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Trade trade = child.getValue(Trade.class);
                        positions.add(trade);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    /**
     * Gets the list of positions for given stock symbol.
     */
    private List<Trade> getPositions(String symbol) {
        List<Trade> result = new ArrayList<>();
        for (Trade t : positions) {
            if (t.symbol.equals(symbol)) {
                result.add(t);
            }
        }

        return result;
    }
}