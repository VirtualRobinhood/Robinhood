package com.codepath.virtualrobinhood.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.activities.StockDetailActivity;
import com.codepath.virtualrobinhood.models.Trade;
import com.codepath.virtualrobinhood.models.Watchlist;
import com.codepath.virtualrobinhood.viewHolders.PortfolioViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PortfolioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PortfolioFragment extends Fragment {
    private static final String TAG = "PortfolioFragment";
    private DatabaseReference dbRerence;
    private FirebaseRecyclerAdapter<Trade, PortfolioViewHolder> mAdapter;
    private RecyclerView rvPortfolio;
    private Button btnCreateWatchlist;
    private EditText etWatchlist;
    private Spinner spWatchlist;
    private LinearLayoutManager linearLayoutManager;
    private List<Watchlist> watchlists;
    //public String userIdValue;

    public PortfolioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PortfolioFragment.
     */
    public static PortfolioFragment newInstance(String userId) {
        PortfolioFragment fragment = new PortfolioFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        //userIdValue = userId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);

        dbRerence = FirebaseDatabase.getInstance().getReference();
        //watchlists = new ArrayList<>();

        rvPortfolio = view.findViewById(R.id.rvPortfolio);

        //rvStocks.setHasFixedSize(true);

        setupWatchlistSpinner();
        //setupCreateWatchlistButton();

        return view;


        // return inflater.inflate(R.layout.fragment_portfolio, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvPortfolio.setLayoutManager(linearLayoutManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query portfolioQuery = getQuery(dbRerence);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Trade>()
                .setQuery(portfolioQuery, Trade.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Trade, PortfolioViewHolder>(options) {

            @Override
            public void onError(DatabaseError error) {
                super.onError(error);
            }

            @Override
            public PortfolioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PortfolioViewHolder(inflater.inflate(R.layout.item_portfolio, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(PortfolioViewHolder viewHolder, int position, final Trade model) {
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

        rvPortfolio.setAdapter(mAdapter);
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



    private void setupWatchlistSpinner() {

    }

    private Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        Log.d("debug", "getQuery");
        return databaseReference.child("users")
                //.child("JJCSBtsEvkSZdFTdFuMxsAE5Pes1")
                .child(getArguments().getString("userId"))
                .child("portfolios")
                .child("TestPortfolio")
                .child("stocks");


        /*return databaseReference.child("users")
                .child(getArguments().getString("userId"))
                .child("watchlists")
                .child(getArguments().getString("watchlistName"))
                .child("stocks");*/
    }
}

  /*dbRef.child("users").child(userId)
          .child("portfolios")
          .child(portfolioName)
          .child("trades")
          .child(trade.id)
          .setValue(trade);*/
