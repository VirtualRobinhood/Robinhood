package com.codepath.virtualrobinhood.utils;

import android.util.Log;

import com.codepath.virtualrobinhood.models.Portfolio;
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
        u.credit = Constants.NEW_USER_CREDIT;
        u.id = user.getUid();
        u.displayName = user.getDisplayName();
        u.email = user.getEmail();
        u.photoUrl = user.getPhotoUrl().toString();

        dbRef.child("users").child(u.id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    dbRef.child("users").child(u.id).setValue(u);
                    Watchlist defaultWatchlist = new Watchlist();
                    defaultWatchlist.name = Constants.DEFAULT_WATCHLIST;

                    Portfolio defaultPortfolio = new Portfolio();
                    defaultPortfolio.name = Constants.DEFAULT_PORTFOLIO;

                    dbRef.child("users")
                            .child(u.id)
                            .child("watchlists")
                            .child(Constants.DEFAULT_WATCHLIST)
                            .setValue(defaultWatchlist);

                    dbRef.child("users")
                            .child(u.id)
                            .child("portfolios")
                            .child(Constants.DEFAULT_PORTFOLIO)
                            .setValue(defaultPortfolio);
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
        Portfolio portfolio = new Portfolio();
        portfolio.name = portfolioName;

        dbRef.child("users").child(userId)
                .child("portfolios")
                .child(portfolioName)
                .setValue(portfolio);
    }

    public void createDeposit(final String userId, double amount) {
        dbRef.child("users").child(userId)
                .child("amount")
                .setValue(amount);
    }

    public void updateCredit(final String userId, double newCredit) {
        dbRef.child("users").child(userId)
                .child("credit")
                .setValue(newCredit);
    }

    public void getDeposit(String userId) {
        //final Double amount;
        dbRef.child("users").child(userId).child("amount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("debug", "onDataChange");
                if (snapshot != null) {
                    Double amount = Double.parseDouble(snapshot.getValue().toString());
                    Log.d("debug", "onDataChange");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
                .child(stock.symbol.toUpperCase())
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

    public void addPositionToPortfolio(final String userId, final String portfolioName,
                                       final Trade trade) {

        dbRef.child("users").child(userId)
                .child("portfolios")
                .child(portfolioName)
                .child("positions")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean merged = false;
                        if (snapshot != null && snapshot.getValue() != null) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Trade t = dataSnapshot.getValue(Trade.class);
                                if (t.symbol.equalsIgnoreCase(trade.symbol)) {
                                    int oldQuantity = t.quantity;
                                    double oldPrice = t.price;
                                    t.quantity = t.quantity + trade.quantity;
                                    t.price = (oldPrice * oldQuantity + trade.price * trade.quantity) / t.quantity;

                                    dbRef.child("users").child(userId)
                                            .child("portfolios")
                                            .child(portfolioName)
                                            .child("positions")
                                            .child(t.id)
                                            .setValue(t);

                                    merged = true;
                                    break;
                                }
                            }
                        }

                        if (snapshot == null || snapshot.getValue() == null || !merged) {
                            dbRef.child("users").child(userId)
                                    .child("portfolios")
                                    .child(portfolioName)
                                    .child("positions")
                                    .child(trade.id)
                                    .setValue(trade);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void removePositionFromPortfolio(final String userId, final String portfolioName,
                                            final Trade trade) {

        dbRef.child("users").child(userId)
                .child("portfolios")
                .child(portfolioName)
                .child("positions")
                .child(trade.id)
                .removeValue();
    }

    public void updatePosition(final String userId, final String portfolioName,
                                            final Trade trade, int quantity) {

        dbRef.child("users").child(userId)
                .child("portfolios")
                .child(portfolioName)
                .child("positions")
                .child(trade.id)
                .child("quantity")
                .setValue(quantity);
    }

    public void addTradeToTransactions(final String userId, final Trade trade) {

        dbRef.child("users").child(userId)
                .child("transactions")
                .child(trade.id)
                .setValue(trade);
    }
}