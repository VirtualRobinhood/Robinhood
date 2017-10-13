package com.codepath.virtualrobinhood.models;

/**
 * Created by gkurghin on 10/12/17.
 */

public class Trade {
    public String id;
    public String symbol;
    public Double price;
    public String date;

    public Trade() {
        id = "10";
        symbol = "csco";
        price = 16.66;
        date = "1/1/2017";

    }
}
