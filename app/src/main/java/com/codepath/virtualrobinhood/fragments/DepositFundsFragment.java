package com.codepath.virtualrobinhood.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.utils.Billing.IabHelper;
import com.codepath.virtualrobinhood.utils.Billing.IabResult;
import com.codepath.virtualrobinhood.utils.Billing.Inventory;
import com.codepath.virtualrobinhood.utils.Billing.Purchase;
import com.codepath.virtualrobinhood.utils.Constants;
import com.codepath.virtualrobinhood.utils.FireBaseClient;

import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * Created by GANESH on 10/13/17.
 */

public class DepositFundsFragment extends Fragment {

    private IabHelper mHelper;

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase) {
            if (result.isFailure()) {
                Log.e("result1", "" + result.getMessage());
                // Handle error
                //  showAlert(result.getMessage());
//                Utils.showSnak(PayCoinsActivity.this,result.getMessage());
//                consumeItem(purchase);
                return;
            } else if (
                    purchase.getSku().equals(Constants.SKU_COINS)) {
                    consumeItem(purchase);

                Log.e("HO GAYA PURCHASE", "OK");
            }

        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d("CONSUME_FINISHED", "Consumption successful. Provisioning.");
            }
            else {
                Log.d("CONSUME_FAILED", "Consumption successful. Provisioning.");
            }
        }
    };

    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {

        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) {
                return;
            }

// Is it a failure?
            if (result.isFailure()) {
                Toast.makeText(getActivity(),
                        "Failed to query inventory: " + result,
                        Toast.LENGTH_LONG).show();
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

/*
* Check for items we own. Notice that for each purchase, we check
* the developer payload to see if it's correct! See
* verifyDeveloperPayload().
*/

// Check for coins -- you can get coins
// tank immediately
            Purchase coinsPurchase = inventory.getPurchase(Constants.SKU_COINS);
            if (coinsPurchase != null) {

                Log.d(TAG, "We have Coins. Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(Constants.SKU_COINS),
                        mConsumeFinishedListener);
                return;
            } else {
                Log.d(TAG, "Unable to fetch the coins SKU");
            }
        }
    };

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

    private void consumeItem(Purchase purchase) {
        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
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
                    mHelper.queryInventoryAsync(mGotInventoryListener);

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

                Random Rand = new Random();
                int Rndnum = Rand.nextInt(10000) + 1;
                mHelper.launchPurchaseFlow(getActivity(), Constants.SKU_COINS, 10001,
                        mPurchaseFinishedListener, "token-" + Rndnum);
            }
        });

        return view;
    }





}
