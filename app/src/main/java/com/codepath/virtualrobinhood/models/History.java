package com.codepath.virtualrobinhood.models;

import org.parceler.Parcel;

/**
 * Created by gkurghin on 10/12/17.
 */

@Parcel(analyze = {History.class})
public class History {
    public String symbol;
    public String buySell;
    public String date;
    public int quantity;
    public Double stockPrice;
    public Double amount;
    public Double total;

    public History() {
        symbol = "CSCO";
        buySell = "SELL";
        date = "1/1/17";
        quantity = 10;
        stockPrice = 10.00;
        amount = 10.00;
        total = 10.00;

    }
}
