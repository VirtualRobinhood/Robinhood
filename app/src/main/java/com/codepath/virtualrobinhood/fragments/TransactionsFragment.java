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
import com.codepath.virtualrobinhood.activities.TradeDetailActivity;
import com.codepath.virtualrobinhood.models.Trade;
import com.codepath.virtualrobinhood.viewHolders.TradeViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.parceler.Parcels;


public class TransactionsFragment extends Fragment {
    private static final String TAG = "TransactionsFragment";
    private DatabaseReference dbRerence;
    private FirebaseRecyclerAdapter<Trade, TradeViewHolder> mAdapter;
    private RecyclerView rvTrades;
    private LinearLayoutManager linearLayoutManager;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WatchlistFragment.
     */
    public static TransactionsFragment newInstance(String userId) {
        TransactionsFragment fragment = new TransactionsFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        dbRerence = FirebaseDatabase.getInstance().getReference();
        rvTrades = view.findViewById(R.id.rvTrades);
        //rvTrades.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTrades.setLayoutManager(linearLayoutManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query tradesQuery = getQuery(dbRerence);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Trade>()
                .setQuery(tradesQuery, Trade.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Trade, TradeViewHolder>(options) {

            @Override
            public void onError(DatabaseError error) {
                super.onError(error);
            }

            @Override
            public TradeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new TradeViewHolder(inflater.inflate(R.layout.item_trade, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(TradeViewHolder viewHolder, int position, final Trade trade) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), TradeDetailActivity.class);
                        intent.putExtra("trade", Parcels.wrap(trade));
                        startActivity(intent);
                    }
                });

                viewHolder.bindToPost(trade);
            }
        };

        rvTrades.setAdapter(mAdapter);
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
                .child("transactions");
    }
}