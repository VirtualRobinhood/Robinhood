package com.codepath.virtualrobinhood.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.List;

/**
 * Created by SagarMutha on 10/11/17.
 */


@Parcel(analyze = {Stock.class})
public class Stock {
    public String symbol;
    public String name;
    public List<StockQuotation> quotations;

    public Stock() {}

    public Stock(JSONObject jsonObject) {
        try {
            this.symbol = jsonObject.getJSONObject("Meta Data").getString("2. Symbol");
            this.name = "";
            this.quotations = StockQuotation.fromJSONObject(jsonObject.getJSONObject("Time Series (Daily)"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
