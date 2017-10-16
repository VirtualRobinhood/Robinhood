package com.codepath.virtualrobinhood.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SagarMutha on 10/11/17.
 */


@Parcel(analyze = {StockQuotation.class})
public class StockQuotation {
    public Date date;
    public float open;
    public float high;
    public float low;
    public float close;

    public StockQuotation() {}

    public StockQuotation(String dateString, JSONObject jsonObject) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            this.date = sdf.parse(dateString);
            this.open = Float.parseFloat(jsonObject.getString("1. open"));
            this.high = Float.parseFloat(jsonObject.getString("2. high"));
            this.low = Float.parseFloat(jsonObject.getString("3. low"));
            this.close = Float.parseFloat(jsonObject.getString("4. close"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<StockQuotation> fromJSONObject(JSONObject jsonObject) {
        ArrayList<StockQuotation> results = new ArrayList<>();
        JSONArray array = jsonObject.names();

        for (int x = 0; x < array.length(); x++) {
            try {
                String date = array.getString(x);
                results.add(new StockQuotation(date, jsonObject.getJSONObject(date)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }
}
