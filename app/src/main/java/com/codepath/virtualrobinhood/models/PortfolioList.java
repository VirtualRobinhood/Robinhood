package com.codepath.virtualrobinhood.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GANESH on 10/14/17.
 */

public class PortfolioList {

    public String name;
    public List<Portfolio> portfolio;

    public PortfolioList() {
        portfolio = new ArrayList<>();
    }
}
