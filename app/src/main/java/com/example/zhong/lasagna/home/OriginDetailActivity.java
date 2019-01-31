package com.example.zhong.lasagna.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.zhong.lasagna.R;
import com.example.zhong.lasagna.common.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OriginDetailActivity extends AppCompatActivity {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private String weiboId;
    private String userId;
    //跳转单条微博页url
    private String urlStatusesGo = "http://api.weibo.com/2/statuses/go";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.origin_detail_activity);
        ButterKnife.bind(this);
        weiboId = getIntent().getStringExtra(HomeFragment.WEIBO_ID);
        userId = getIntent().getStringExtra(HomeFragment.USER_ID);
        initWebView();
//        sendHttp();
    }

    private void initWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setAppCacheEnabled(true);
        //设置 缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webView.setWebViewClient(new WebViewClient() {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void  onPageStarted(WebView view, String url, Bitmap favicon) {
                //设定加载开始的操作
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                //设定加载结束的操作
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                switch(errorCode)
                {
                    case 404:
                        view.loadUrl("file:///android_assets//error.html");
                        break;
                }
            }
        });

        Log.e("hello", "url="+urlStatusesGo + MyApplication.getAccessToken() + "&uid=" + userId + "&id=" + weiboId);
        webView.loadUrl(urlStatusesGo + MyApplication.getAccessToken() + "&uid=" + userId + "&id=" + weiboId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        webView.onResume();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }
}
