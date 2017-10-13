package com.codepath.virtualrobinhood.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkurghin on 10/11/17.
 */

public class Portfolio {
    public String name;
    public List<Trade> trades;

    public Portfolio(){
        trades = new ArrayList<>();
    }
}
