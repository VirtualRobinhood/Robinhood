package com.codepath.virtualrobinhood.models;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkurghin on 10/11/17.
 */

public class Portfolio {
    public String name;
    public List<Trade> trades;

    public Portfolio(){
        //Log.d("debug", "tst");
        trades = new ArrayList<>();
    }

    public Portfolio(JSONObject jsonObject) {
        Log.d("debug", "test");
    }
}
