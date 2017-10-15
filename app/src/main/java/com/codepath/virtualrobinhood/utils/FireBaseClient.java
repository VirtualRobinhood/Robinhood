package com.codepath.virtualrobinhood.utils;

import com.codepath.virtualrobinhood.models.PortfolioList;
import com.codepath.virtualrobinhood.models.Stock;
import com.codepath.virtualrobinhood.models.Trade;
import com.codepath.virtualrobinhood.models.User;
import com.codepath.virtualrobinhood.models.Watchlist;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by gkurghin on 10/12/17.
 */

public class FireBaseClient {
    private final FirebaseDatabase database;
    private final DatabaseReference dbRef;

    public FireBaseClient() {
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
    }

    public void registerNewUser(FirebaseUser user) {
        final User u = new User();
        u.id = user.getUid();
        u.displayName = user.getDisplayName();
        u.email = user.getEmail();

        dbRef.child("users").child(u.id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    dbRef.child("users").child(u.id).setValue(u);
                    Watchlist defaultWatchlist = new Watchlist();
                    defaultWatchlist.name = Constants.DEFAULT_WATCHLIST;
                    dbRef.child("users")
                            .child(u.id)
                            .child("warchlists")
                            .child(Constants.DEFAULT_WATCHLIST)
                            .setValue(defaultWatchlist);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getUser(String userId) {
        dbRef.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot == null) {
                    User user = snapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void createWatchlist(final String userId, final String watchlistName) {
        Watchlist watchlist = new Watchlist();
        watchlist.name = watchlistName;

        dbRef.child("users").child(userId)
                .child("watchlists")
                .child(watchlistName)
                .setValue(watchlist);
    }

    public void deleteWatchlist(final String userId, final String watchlistName) {
        dbRef.child("users").child(userId)
                .child("watchlists")
                .child(watchlistName)
                .removeValue();
    }

    public void createPortfolio(final String userId, final String portfolioName) {
        //Portfolio portfolio = new Portfolio();
        //portfolio.name = portfolioName;
        PortfolioList portfolioList = new PortfolioList();
        portfolioList.name = portfolioName;

        dbRef.child("users").child(userId)
                .child("portfolios")
                .child(portfolioName)
                .setValue(portfolioList);
    }


    public void createDeposit(final String userId, double amount) {
        dbRef.child("users").child(userId)
                .child("amount")
                .setValue(amount);
    }

    public void deletePortfolio(final String userId, final String portfolioName) {
        dbRef.child("users").child(userId)
                .child("portfolios")
                .child(portfolioName)
                .removeValue();
    }

    public void addSymbolToWatchlist(final String userId, final String watchlistName,
                                     final Stock stock) {
        dbRef.child("users").child(userId)
                .child("watchlists")
                .child(watchlistName)
                .child("stocks")
                .child(stock.symbol)
                .setValue(stock);
    }

    public void removeSymbolFromWatchlist(final String userId, final String watchlistName,
                                     final String stockSymbol) {
        dbRef.child("users").child(userId)
                .child("watchlists")
                .child(watchlistName)
                .child("stocks")
                .child(stockSymbol)
                .removeValue();
    }

    public void addTradeToPortfolio(final String userId, final String portfolioName,
                                    final Trade trade) {
        /*dbRef.child("users").child(userId)
                .child("portfolios")
                .child(portfolioName)
                .child("trades")
                .child(trade.id)
                .setValue(trade);*/

        dbRef.child("users").child(userId)
                .child("portfolios")
                .child(portfolioName)
                .child("stocks")
                .child(trade.symbol)
                .setValue(trade);


        /*dbRef.child("users").child(userId)
                .child("watchlists")
                .child(watchlistName)
                .child("stocks")
                .child(stock.symbol)
                .setValue(stock);*/

    }
}
