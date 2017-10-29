package com.codepath.virtualrobinhood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.fragments.ArticlesListFragment;
import com.codepath.virtualrobinhood.fragments.LineChartFragment;
import com.codepath.virtualrobinhood.fragments.TransactionsFragment;
import com.codepath.virtualrobinhood.models.Stock;
import com.codepath.virtualrobinhood.models.Trade;
import com.codepath.virtualrobinhood.utils.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TradeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_detail);

        Intent intent = getIntent();
        final Trade trade = Parcels.unwrap(intent.getParcelableExtra("trade"));
        final String callerName = intent.getStringExtra("caller");

        DecimalFormat df = new DecimalFormat("##.##");

        TextView tvSymbol = findViewById(R.id.tvSymbol);
        TextView tvCompanyName = findViewById(R.id.tvCompanyName);
        TextView tvQuantity = findViewById(R.id.tvQuantity);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvEstimatedCost = findViewById(R.id.tvEstimatedCost);
        TextView tvFilledDate = findViewById(R.id.tvFilledDate);

        tvSymbol.setText(trade.symbol);
        tvCompanyName.setText(trade.companyName);
        tvPrice.setText("$" + df.format(trade.price));
        tvQuantity.setText(String.valueOf(trade.quantity));
        tvEstimatedCost.setText("$" + df.format(trade.quantity * trade.price));
        tvFilledDate.setText(trade.filledOn);

        Button btnSell = (Button) findViewById(R.id.btnSell);

        if (callerName != null && callerName.equals(TransactionsFragment.class.getName())) {
            btnSell.setVisibility(View.GONE);
            findViewById(R.id.tvArticles).setVisibility(View.GONE);
        } else {
            fetchStockInfo(trade);
        }

        btnSell.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TradeDetailActivity.this, StockSellActivity.class);
                intent.putExtra("trade", Parcels.wrap(trade));
                startActivity(intent);
            }
        });
    }

    public void fetchStockInfo(final Trade trade) {

        OkHttpClient client = HttpClient.getClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://www.alphavantage.co/query").newBuilder();
        urlBuilder.addQueryParameter("apikey", "G54EIJN1HQ6Q296J");
        urlBuilder.addQueryParameter("function", "TIME_SERIES_DAILY_ADJUSTED");
        urlBuilder.addQueryParameter("symbol", trade.symbol);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        // Get a handler that can be used to post to the main thread
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                try {
                    final String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    Stock stock = new Stock(json);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flLineChart,
                            LineChartFragment.newInstance(stock))
                            .commit();

                    fragmentManager.beginTransaction().replace(R.id.flArticles,
                            ArticlesListFragment.newInstance(trade.companyName))
                            .commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}