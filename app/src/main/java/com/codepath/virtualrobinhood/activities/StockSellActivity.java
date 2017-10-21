package com.codepath.virtualrobinhood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockSellActivity extends AppCompatActivity {
    private List<Trade> positions = new ArrayList<>();
    private double credit;
    private Trade selectedPosition;
    private Stock stock;
    private DecimalFormat df = new DecimalFormat("##.##");

    private Spinner spPositions;
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
        stock = Parcels.unwrap(intent.getParcelableExtra("stock"));
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
        spPositions = findViewById(R.id.spPosition);
        final Button btnSell = findViewById(R.id.btnSell);

        tvSymbol.setText(stock.symbol);
        tvCompanyName.setText(stock.companyName);
        tvMarketPrice.setText(df.format(stock.getLastClosePrice()));

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
                double estimatedCost = quantity * stock.getLastClosePrice();
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
                if (selectedPosition == null) {
                    Toast.makeText(v.getContext(), "Please select a position to trade",
                            Toast.LENGTH_SHORT).show();
                }

                int sellQuantity = Integer.parseInt(etQuantityToSell.getText().toString());

                if (sellQuantity > selectedPosition.quantity) {
                    Toast.makeText(v.getContext(), "Count is more than available stocks.",
                            Toast.LENGTH_SHORT).show();
                }

                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                Trade trade = new Trade();
                trade.companyName = stock.companyName;
                trade.symbol = stock.symbol;
                trade.price = stock.getLastClosePrice();
                trade.quantity = sellQuantity;
                trade.submittedOn = date;
                trade.filledOn = date;
                trade.tradeType = Trade.TradeType.SELL;

                fireBaseClient.addPositionToPortfolio(userId, Constants.DEFAULT_PORTFOLIO, trade);
                if (sellQuantity == selectedPosition.quantity) {
                    fireBaseClient.removePositionFromPortfolio(userId, Constants.DEFAULT_PORTFOLIO,
                            selectedPosition);
                } else {
                    fireBaseClient.updatePosition(userId, Constants.DEFAULT_PORTFOLIO,
                            selectedPosition, selectedPosition.quantity - sellQuantity);
                }

                fireBaseClient.updateCredit(userId, credit + trade.price * trade.quantity);

                Toast.makeText(v.getContext(), "Stock sold successfully", Toast.LENGTH_SHORT).show();
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
                    //positions = (Map<String, Trade>) snapshot.getValue();

                    for (DataSnapshot child : snapshot.getChildren()) {
                        Trade trade = child.getValue(Trade.class);
                        positions.add(trade);
                    }

                    setupPositionSpinner(stock.symbol);
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

    private void setupPositionSpinner(final String symbol) {
        final List<Trade> positions = getPositions(symbol);

        List<String> ids = new ArrayList<>();
        for (Trade t : positions) {
            ids.add(t.id);
        }

        ArrayAdapter positionsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, ids);
        positionsAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spPositions.setAdapter(positionsAdapter);

        spPositions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tradeId = (String) parent.getItemAtPosition(position);
                for (Trade t : positions) {
                    if (tradeId.equals(t.id)) {
                        selectedPosition = t;
                        break;
                    }
                }

                if (selectedPosition != null) {
                    int sellQuantity = etQuantityToSell.getText().toString().isEmpty()
                            ? 0
                            : Integer.parseInt(etQuantityToSell.getText().toString());
                    double gain = sellQuantity * (stock.getLastClosePrice() - selectedPosition.price);

                    tvQuantity.setText(String.valueOf(selectedPosition.quantity));
                    tvEstimatedCost.setText(df.format(sellQuantity * selectedPosition.price));
                    tvEstimatedGain.setText(df.format(gain));
                    tvCostPareShare.setText(df.format(selectedPosition.price));
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}