package com.codepath.virtualrobinhood.models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkurghin on 10/11/17.
 */

@Parcel(analyze = {User.class})
public class User {
    public String id;
    public String displayName;
    public String email;
    public String photoUrl;
    public double credit;

    private List<Portfolio> portfolios;
    private List<Watchlist> watchlists;
    private List<Trade> transitions;

    public User() {
        portfolios = new ArrayList<>();
        watchlists = new ArrayList<>();
        transitions = new ArrayList<>();
        credit = 0;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    public void setWatchlists(List<Watchlist> watchlists) {
        this.watchlists = watchlists;
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public List<Watchlist> getWatchlists() {
        return watchlists;
    }

    public void setTransitions(List<Trade> transitions) {
        this.transitions = transitions;
    }

    public List<Trade> getTransitions() {
        return transitions;
    }
}