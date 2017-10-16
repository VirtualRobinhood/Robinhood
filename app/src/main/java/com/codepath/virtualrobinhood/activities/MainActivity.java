package com.codepath.virtualrobinhood.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.fragments.DepositFundsFragment;
import com.codepath.virtualrobinhood.fragments.PortfolioFragment;
import com.codepath.virtualrobinhood.fragments.WatchlistFragment;
import com.codepath.virtualrobinhood.models.Stock;
import com.codepath.virtualrobinhood.models.StockQuotation;
import com.codepath.virtualrobinhood.models.Trade;
import com.codepath.virtualrobinhood.utils.Constants;
import com.codepath.virtualrobinhood.utils.FireBaseClient;
import com.codepath.virtualrobinhood.utils.HttpClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "vr:MainActivity";

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        tvUsername = navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        tvUsername.setText(intent.getStringExtra("displayName"));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.action_search).setVisible(false);


        // Get current Funds
        getCurrentFunds(FirebaseAuth.getInstance().getCurrentUser().getUid());

        setupDrawerContent(nvDrawer);

        nvDrawer.getMenu().getItem(0).setChecked(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent,
                PortfolioFragment.newInstance(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                .commit();
        setTitle(R.string.portfolio);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        switch (menuItem.getItemId()) {
            case R.id.nav_watchlist:
                fragment = WatchlistFragment.newInstance(userId);
                break;
            case R.id.nav_portfolio:
                fragment = PortfolioFragment.newInstance(userId);
                break;
            case R.id.nav_add_money:
                fragment = DepositFundsFragment.newInstance(userId);
                break;
            case R.id.nav_sign_out:
                signOut();
                return;
            default:
                fragment = PortfolioFragment.newInstance(userId);
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_view, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                fetchStockInfo(query);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void fetchStockInfo(String symbol) {

        OkHttpClient client = HttpClient.getClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://www.alphavantage.co/query").newBuilder();
        urlBuilder.addQueryParameter("apikey", "G54EIJN1HQ6Q296J");
        urlBuilder.addQueryParameter("function", "TIME_SERIES_DAILY_ADJUSTED");
        urlBuilder.addQueryParameter("symbol", symbol);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        // Get a handler that can be used to post to the main thread
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                try {
                    final String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    Stock stock = new Stock(json);
                    Trade trade = new Trade();
                    trade.symbol = stock.symbol;
                    StockQuotation quot = stock.quotations.get(0);
                    //portfolio = new Portfolio();
                    Log.d("STOCK", stock.toString());

                    // searched stocks are going to be added to the watchlist for now
                    FireBaseClient fireBaseClient = new FireBaseClient();
                    fireBaseClient.addSymbolToWatchlist(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            Constants.DEFAULT_WATCHLIST, stock);


                    Intent stockDetailActivity = new Intent(MainActivity.this, StockDetailActivity.class);
                    stockDetailActivity.putExtra("stock_symbol", stock.symbol);
                    stockDetailActivity.putExtra("stock_price", String.valueOf(quot.high));
                    stockDetailActivity.putExtra("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());

                    startActivity(stockDetailActivity);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent signOutIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(signOutIntent);
                    }
                });
    }

    public void getCurrentFunds(String userId) {
        //final Double amount;

        final FirebaseDatabase database;
        final DatabaseReference dbRef;

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        dbRef.child("users").child(userId).child("amount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("debug", "onDataChange");
                if (snapshot != null) {
                    if (snapshot.getValue() != null) {
                        Double amount = Double.parseDouble(snapshot.getValue().toString());
                        Log.d("debug", "onDataChange");

                        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
                        Menu nav_Menu = navigationView.getMenu();

                        MenuItem myItem = nav_Menu.findItem(R.id.portfolio_value);
                        myItem.setTitle("    $" + amount.toString() + "0");
                    } else {
                        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
                        Menu nav_Menu = navigationView.getMenu();

                        MenuItem myItem = nav_Menu.findItem(R.id.portfolio_value);
                        myItem.setTitle("    $0.00" );
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
