package com.codepath.virtualrobinhood.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.activities.StockDetailActivity;
import com.codepath.virtualrobinhood.models.Stock;
import com.codepath.virtualrobinhood.viewHolders.StockViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


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
    // TODO: Rename and change types and number of parameters
    public static WatchlistFragment newInstance(String userId, String watchlistName) {
        WatchlistFragment fragment = new WatchlistFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("watchlistName", watchlistName);
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
                return new StockViewHolder(inflater.inflate(R.layout.item_stock, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(StockViewHolder viewHolder, int position, final Stock model) {
                final DatabaseReference stockRef = getRef(position);

                // Set click listener for the whole stock view
                final String stockKey = stockRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), StockDetailActivity.class);
                        intent.putExtra(StockDetailActivity.EXTRA_STOCK_KEY, stockKey);
                        startActivity(intent);
                    }
                });

                // Bind Stock to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model);
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

    private Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("users")
                .child(getArguments().getString("userId"))
                .child("watchlists")
                .child(getArguments().getString("watchlistName"))
                .child("stocks");
    }
}