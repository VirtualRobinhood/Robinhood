package com.codepath.virtualrobinhood.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.models.Article;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {

    public static final String ARTICLE_DATA_KEY = "article";

    @BindView(R.id.wvArticle)
    WebView mwvArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ButterKnife.bind(this);

        mwvArticle.getSettings().setLoadsImagesAutomatically(true);
        mwvArticle.getSettings().setJavaScriptEnabled(true);
        mwvArticle.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mwvArticle.setWebViewClient(new ArticleBrowser());

        // we could just pass the web url to this activity, but the goal is to demonstrate
        // the usage of Parcels library.
        Article article = Parcels.unwrap(getIntent().getParcelableExtra(ARTICLE_DATA_KEY));

        mwvArticle.loadUrl(article.getWebUrl());
    }

    private class ArticleBrowser extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
}
