package com.codepath.virtualrobinhood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.models.Trade;

import org.parceler.Parcels;

import java.text.DecimalFormat;

public class TradeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_detail);

        Intent intent = getIntent();
        final Trade trade = Parcels.unwrap(intent.getParcelableExtra("trade"));

        DecimalFormat df = new DecimalFormat("##.##");

        TextView tvSymbol  = findViewById(R.id.tvSymbol);
        TextView tvCompanyName  = findViewById(R.id.tvCompanyName);
        TextView tvQuantity = findViewById(R.id.tvQuantity);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvEstimatedCost = findViewById(R.id.tvEstimatedCost);
        TextView tvFilledDate = findViewById(R.id.tvFilledDate);

        tvSymbol.setText(trade.symbol);
        tvCompanyName.setText(trade.companyName);
        tvPrice.setText(df.format(trade.price));
        tvQuantity.setText(String.valueOf(trade.quantity));
        tvEstimatedCost.setText(df.format(trade.quantity * trade.price));
        tvFilledDate.setText(trade.filledOn);

        Button btnSell = (Button) findViewById(R.id.btnSell);

        //TODO: if user came from history activity hide sell button.

        btnSell.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(TradeDetailActivity.this, StockSellActivity.class);
                intent.putExtra("trade", Parcels.wrap(trade));
                startActivity(intent);
            }
        });
    }
}