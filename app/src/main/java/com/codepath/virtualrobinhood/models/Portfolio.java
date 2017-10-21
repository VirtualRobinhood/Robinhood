package com.codepath.virtualrobinhood.models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkurghin on 10/11/17.
 */


@Parcel(analyze = {Portfolio.class})
public class Portfolio {
    public String name;
    public List<Trade> positions;

    public Portfolio(){
        positions = new ArrayList<>();
    }
}
