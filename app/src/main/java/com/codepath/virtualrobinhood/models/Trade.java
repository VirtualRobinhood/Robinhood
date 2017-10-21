package com.codepath.virtualrobinhood.models;

import org.parceler.Parcel;

import java.util.UUID;

/**
 * Created by gkurghin on 10/12/17.
 */

@Parcel(analyze = {Trade.class})
public class Trade {
    public String id;
    public String symbol;
    public String companyName;
    public TradeType tradeType;
    public Double price;
    public String submittedOn;
    public String filledOn;
    public int quantity;

    public Trade() {
        id = UUID.randomUUID().toString();
    }

    public enum TradeType{
        BUY,
        SELL
    }

    public enum TradeState {
        ACTIVE,
        ARCHIVED
    }
}