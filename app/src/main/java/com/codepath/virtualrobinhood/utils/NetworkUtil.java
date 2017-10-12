package com.codepath.virtualrobinhood.utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SagarMutha on 10/11/17.
 */

public class NetworkUtil {

    private static NetworkUtil INSTANCE = null;
    private static OkHttpClient client = new OkHttpClient();

    // other instance variables can be here

    private NetworkUtil() {};

    public static NetworkUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkUtil();
            client = new OkHttpClient();
        }
        return(INSTANCE);
    }

    public void fetchStockInfo(String symbol) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://www.alphavantage.co/query").newBuilder();
        urlBuilder.addQueryParameter("apikey", "G54EIJN1HQ6Q296J");
        urlBuilder.addQueryParameter("function", "TIME_SERIES_DAILY_ADJUSTED");
        urlBuilder.addQueryParameter("symbol", symbol);
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

                // Read data on the worker thread
                final String responseData = response.body().string();
                Log.d("RESPONSE", responseData);
            }
        });
    }
}
