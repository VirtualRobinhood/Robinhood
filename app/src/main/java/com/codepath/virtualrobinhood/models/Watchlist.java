package com.codepath.virtualrobinhood.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkurghin on 10/11/17.
 */

public class Watchlist {
    public String name;
    public List<Stock> stocks;

    public Watchlist() {
        stocks = new ArrayList<>();
    }
}
