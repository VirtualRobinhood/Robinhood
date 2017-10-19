package com.codepath.virtualrobinhood.models;

import android.util.Log;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gkurghin on 10/11/17.
 */


@Parcel(analyze = {HistoryList.class})
public class HistoryList {
    public String name;
    public List<History> history;

    public HistoryList(){
        history = new ArrayList<>();
    }

    public HistoryList(JSONObject jsonObject) {
        Log.d("debug", "test");
    }
}
