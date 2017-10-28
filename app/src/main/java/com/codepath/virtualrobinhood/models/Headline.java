package com.codepath.virtualrobinhood.models;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Headline {
    private String main;
    private String kicker;

    public String getMain() {
        return main;
    }

    public void setMain(String value) {
        main = value;
    }

    public String getKicker() {
        return kicker;
    }

    public String getPrintHeadline() {
        return printHeadline;
    }

    @SerializedName("print_headline")
    private String printHeadline;
}