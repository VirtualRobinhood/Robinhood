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
import com.codepath.virtualrobinhood.utils.Billing.IabHelper;
import com.codepath.virtualrobinhood.utils.Billing.IabResult;
import com.codepath.virtualrobinhood.utils.Constants;
import com.codepath.virtualrobinhood.utils.FireBaseClient;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by GANESH on 10/13/17.
 */

public class DepositFundsFragment extends Fragment {

    private IabHelper mHelper;

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

        mHelper = new IabHelper(getContext(), Constants.base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
//                    checkBilling = false;
                    Log.e("INAPP_BILLING", "In-app Billing setup failed: " +
                            result.getMessage()+ " "+result.getResponse());
                } else {
//                    checkBilling = true;


                            Log.d("INAPP_BILLING", "In-app Billing is set up OK");

                }
            }
        });

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
