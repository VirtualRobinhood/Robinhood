package com.codepath.virtualrobinhood.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SagarMutha on 10/11/17.
 */


@Parcel(analyze = {Stock.class})
public class Stock {
    public String symbol;
    public String name;
    public List<StockQuotation> quotations;

    public Stock() {
        quotations = new ArrayList<>();
    }

    public double getLastPrice() {
        return quotations.size() > 0 ? quotations.get(0).close : 0;
    }

    public Stock(JSONObject jsonObject) {
        try {
            this.symbol = jsonObject.getJSONObject("Meta Data").getString("2. Symbol");
            this.name = "";
            this.quotations = StockQuotation.fromJSONObject(jsonObject.getJSONObject("Time Series (Daily)"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Stock(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public static ArrayList<Stock> getSuggestions(JSONObject jsonObject) throws JSONException {
        ArrayList<Stock> results = new ArrayList<>();
        JSONArray array = jsonObject.getJSONObject("ResultSet").getJSONArray("Result");

        for (int x = 0; x < array.length(); x++) {
            JSONObject json = array.getJSONObject(x);
            String exchange = json.getString("exchDisp");
            if (exchange.equalsIgnoreCase("NASDAQ") || exchange.equalsIgnoreCase("NYSE")) {
                results.add(new Stock(json.getString("symbol"), json.getString("name")));
            }
        }

        return results;
    }
}
