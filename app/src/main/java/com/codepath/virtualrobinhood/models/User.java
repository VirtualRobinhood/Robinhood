package com.codepath.virtualrobinhood.models;

import java.util.List;

/**
 * Created by gkurghin on 10/11/17.
 */

public class User {
    public String id;
    public String displayName;
    public String email;
    public List<Portfolio> portfolios;
    public List<Watchlist> watchlists;

    public User() {}
}
