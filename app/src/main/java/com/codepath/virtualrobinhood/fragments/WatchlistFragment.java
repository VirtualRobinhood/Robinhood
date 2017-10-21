package com.codepath.virtualrobinhood.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.activities.StockDetailActivity;
import com.codepath.virtualrobinhood.models.Stock;
import com.codepath.virtualrobinhood.utils.Constants;
import com.codepath.virtualrobinhood.utils.FireBaseClient;
import com.codepath.virtualrobinhood.viewHolders.StockViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.parceler.Parcels;


public class WatchlistFragment extends Fragment {

    private static final String TAG = "WatchlistFragment";
    private DatabaseReference dbRerence;
    private FirebaseRecyclerAdapter<Stock, StockViewHolder> mAdapter;
    private RecyclerView rvStocks;
    private LinearLayoutManager linearLayoutManager;

    public WatchlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WatchlistFragment.
     */
    public static WatchlistFragment newInstance(String userId) {
        WatchlistFragment fragment = new WatchlistFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);

        dbRerence = FirebaseDatabase.getInstance().getReference();
        rvStocks = view.findViewById(R.id.rvStocks);
        rvStocks.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvStocks.setLayoutManager(linearLayoutManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query stocksQuery = getQuery(dbRerence);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Stock>()
                .setQuery(stocksQuery, Stock.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Stock, StockViewHolder>(options) {

            @Override
            public void onError(DatabaseError error) {
                super.onError(error);
            }

            @Override
            public StockViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new StockViewHolder(inflater.inflate(R.layout.item_stock, viewGroup, false), getActivity());
            }

            @Override
            protected void onBindViewHolder(StockViewHolder viewHolder, int position, final Stock stock) {
                final DatabaseReference stockRef = getRef(position);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), StockDetailActivity.class);
                        intent.putExtra("stock", Parcels.wrap(stock));

                        startActivity(intent);
                    }
                });

                viewHolder.bindToPost(stock);

                setupRemoveSymbolImageView(viewHolder.itemView);
            }
        };

        rvStocks.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    private void setupRemoveSymbolImageView(View view){
        ImageView ivRemoveSymbol = view.findViewById(R.id.ivRemoveSymbol);
        final String stockSymbol =  ((TextView)view.findViewById(R.id.tvSymbol)).getText().toString();
        ivRemoveSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(stockSymbol);
            }
        });
    }

    private void showDeleteConfirmationDialog(final String stockSymbol) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.remove_symbol_msg);
        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FireBaseClient fireBaseClient = new FireBaseClient();
                fireBaseClient.removeSymbolFromWatchlist(
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        Constants.DEFAULT_WATCHLIST, stockSymbol);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the reminder.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("users")
                .child(getArguments().getString("userId"))
                .child("watchlists")
                .child(Constants.DEFAULT_WATCHLIST)
                .child("stocks");
    }
}