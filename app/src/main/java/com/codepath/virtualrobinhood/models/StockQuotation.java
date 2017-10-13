package com.codepath.virtualrobinhood.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SagarMutha on 10/11/17.
 */

public class StockQuotation {
    public Date date;
    public float open;
    public float high;
    public float low;
    public float close;

    public StockQuotation() {}

    public StockQuotation(JSONObject jsonObject) {
        try {
            this.open = Float.parseFloat(jsonObject.getString("1. open"));
            this.high = Float.parseFloat(jsonObject.getString("2. high"));
            this.low = Float.parseFloat(jsonObject.getString("3. low"));
            this.close = Float.parseFloat(jsonObject.getString("4. close"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<StockQuotation> fromJSONObject(JSONObject jsonObject) {
        ArrayList<StockQuotation> results = new ArrayList<>();
        JSONArray array = jsonObject.names();

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new StockQuotation(jsonObject.getJSONObject(array.getString(x))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }
}
