package com.codepath.virtualrobinhood.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.utils.FireBaseClient;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by GANESH on 10/13/17.
 */

public class DepositFundsFragment extends Fragment {
    public DepositFundsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PortfolioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DepositFundsFragment newInstance(String userId) {
        DepositFundsFragment fragment = new DepositFundsFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);



        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FireBaseClient fireBaseClient = new FireBaseClient();

        View view = inflater.inflate(R.layout.fragment_deposit_funds, container, false);
        final Button btnAddMoney = view.findViewById(R.id.btnAddMoney);
        final EditText etMoney = view.findViewById(R.id.etMoney);

        btnAddMoney.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("debug", "debug");

                fireBaseClient.createDeposit(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        Double.parseDouble(etMoney.getText().toString()));

                // Code here executes on main thread after user presses button
            }
        });

        return view;
    }





}
