package com.codepath.virtualrobinhood.models;

import org.parceler.Parcel;

/**
 * Created by gkurghin on 10/12/17.
 */

@Parcel(analyze = {Trade.class})
public class History {
    String symbol;
    String buySell;
    String date;
    int quantity;
    Double stockPrice;
    Double amount;
    Double total;

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
