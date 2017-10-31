package com.codepath.virtualrobinhood.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.adaptors.ArticlesAdapter;
import com.codepath.virtualrobinhood.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.virtualrobinhood.models.Article;
import com.codepath.virtualrobinhood.models.ArticlesResponse;
import com.codepath.virtualrobinhood.network.NYTimesClient;
import com.codepath.virtualrobinhood.network.NYTimesService;
import com.codepath.virtualrobinhood.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ArticlesListFragment extends Fragment {

    private static final String LOG_TAG = ArticlesListFragment.class.getSimpleName();
    public static final NYTimesService NEW_YORK_TIMES_SERVICE = NYTimesClient.getInstance()
            .getNYTimesService();
    private static final String STOCK_SYMBOL = "stockSymbol";
    private String stockSymbol;
    private Unbinder unbinder;

    @BindView(R.id.rvArticles)
    RecyclerView mrvArticles;

    private List<Article> mArticles;
    private ArticlesAdapter mArticlesAdapter;

    public ArticlesListFragment() {
        // Required empty public constructor
    }

    public static ArticlesListFragment newInstance(String stockSymbol) {
        ArticlesListFragment fragment = new ArticlesListFragment();
        Bundle args = new Bundle();
        args.putString(STOCK_SYMBOL, stockSymbol);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stockSymbol = getArguments().getString(STOCK_SYMBOL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_articles_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        mArticles = new ArrayList<>();
        mArticlesAdapter = new ArticlesAdapter(getActivity(), mArticles);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mrvArticles.setAdapter(mArticlesAdapter);
        mrvArticles.setLayoutManager(gridLayoutManager);

        mrvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchArticles(page);
            }
        });
        fetchArticles(0);
        return view;
    }

    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void fetchArticles(int page) {
        if (Utils.isNetworkAvailable(getActivity())) {

            if (stockSymbol != null && !stockSymbol.isEmpty()) {
                Call<ArticlesResponse> articlesCall = NEW_YORK_TIMES_SERVICE
                        .getArticles(stockSymbol, page, null, "newest", null);

                articlesCall.enqueue(new Callback<ArticlesResponse>() {
                    @Override
                    public void onResponse(Call<ArticlesResponse> call, Response<ArticlesResponse> response) {
                        if (response != null && response.body() != null) {
                            mArticles.addAll(response.body().getResponse().getArticles());
                            mArticlesAdapter.notifyDataSetChanged();
                        } else {
                            if (response.code() == 429) {
                                // do nothing.
                            } else {
                                //Utils.showSnackBar(mrvArticles, getActivity(), R.string.request_failed_text);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArticlesResponse> call, Throwable t) {
                        Utils.showSnackBar(mrvArticles, getActivity(), R.string.request_failed_text);
                    }
                });
            }
        } else {
            Utils.showSnackBarForInternetConnection(mrvArticles, getActivity());
        }
    }
}
