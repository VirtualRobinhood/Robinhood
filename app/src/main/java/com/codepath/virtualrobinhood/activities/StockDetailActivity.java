package com.codepath.virtualrobinhood.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.fragments.ArticlesListFragment;
import com.codepath.virtualrobinhood.fragments.LineChartFragment;
import com.codepath.virtualrobinhood.models.Stock;
import com.codepath.virtualrobinhood.utils.Constants;
import com.codepath.virtualrobinhood.utils.FireBaseClient;
import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

import java.text.DecimalFormat;

public class StockDetailActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private Transition.TransitionListener mEnterTransitionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        Intent intent = getIntent();
        final Stock stock = Parcels.unwrap(intent.getParcelableExtra("stock"));

        setTitle(stock.symbol.toUpperCase());

        DecimalFormat df = new DecimalFormat("##.##");

        TextView tvSymbol = findViewById(R.id.tvSymbol);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvOpenPrice = findViewById(R.id.tvOpenPrice);
        TextView tvLowPrice = findViewById(R.id.tvLowPrice);
        TextView tvHighPrice = findViewById(R.id.tvHighPrice);
        TextView tvClosePrice = findViewById(R.id.tvClosePrice);

        tvSymbol.setText(stock.symbol);
        tvPrice.setText("$" + df.format(stock.getLastClosePrice()));
        tvOpenPrice.setText("$" + df.format(stock.getLastOpenPrice()));
        tvClosePrice.setText("$" + df.format(stock.getLastClosePrice()));
        tvLowPrice.setText("$" + df.format(stock.getLastLowPrice()));
        tvHighPrice.setText("$" + df.format(stock.getLastHighPrice()));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flArticles,
                ArticlesListFragment.newInstance(stock.companyName))
                .commit();

        fragmentManager.beginTransaction().replace(R.id.flLineChart,
                LineChartFragment.newInstance(stock))
                .commit();

        final Button btnBuy = findViewById(R.id.btnBuy);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(StockDetailActivity.this, StockBuyActivity.class);
                intent.putExtra("stock", Parcels.wrap(stock));
                startActivity(intent);
            }
        });

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseClient fireBaseClient = new FireBaseClient();
                fireBaseClient.addSymbolToWatchlist(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        Constants.DEFAULT_WATCHLIST, stock);

                Snackbar.make(btnBuy, getString(R.string.add_stock_to_watchlist), Snackbar.LENGTH_LONG)
                        .show();
            }
        });

        setupTransitionListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exitReveal();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        exitReveal();
    }

    private void setupTransitionListener() {
        mEnterTransitionListener = new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                enterReveal();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        };
        getWindow().getEnterTransition().addListener(mEnterTransitionListener);
    }

    void enterReveal() {
        // previously invisible view
        final View myView = fab;

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;
        Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        myView.setVisibility(View.VISIBLE);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getWindow().getEnterTransition().removeListener(mEnterTransitionListener);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    void exitReveal() {
        // previously visible view
        final View myView = fab;

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth() / 2;

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);

                // Finish the activity after the exit transition completes.
                supportFinishAfterTransition();
            }
        });

        // start the animation
        anim.start();
    }
}