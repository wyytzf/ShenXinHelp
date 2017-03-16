package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xd.shenxinhelp.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private String url;
    private String title = "身心帮";
    private CollapsingToolbarLayout coll;
    private ImageView background;
    private String image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        image_url = getIntent().getStringExtra("image_url");
        initViews();


    }

    private void initViews() {
        coll = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        coll.setTitle(title);

        background = (ImageView) findViewById(R.id.toolbar_background);
        Glide.with(this).load(image_url).into(background);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setDefaultTextEncodingName("GBK");
        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
