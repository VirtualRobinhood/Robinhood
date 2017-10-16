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

    private List<Portfolio> portfolios;
    private List<Watchlist> watchlists;

    public User() {
        portfolios = new ArrayList<>();
        watchlists = new ArrayList<>();
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
}
