package com.example.zhong.lasagna.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zhong.lasagna.MainActivity;
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
        weiboId = getIntent().getStringExtra(HomeRecyclerAdapter.WEIBO_ID);
        userId = getIntent().getStringExtra(HomeRecyclerAdapter.USER_ID);
        initWebView();
//        sendHttp();
    }

    private void initWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 设置允许JS弹窗
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

        Log.e("hello", "url="+urlStatusesGo + MyApplication.getAccessToken() + "&uid=" + userId + "&id=" + weiboId);
        webView.loadUrl(urlStatusesGo + MyApplication.getAccessToken() + "&uid=" + userId + "&id=" + weiboId);

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

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//                new AlertDialog.Builder(OriginDetailActivity.this)
//                        .setTitle("Alert")
//                        .setMessage(message)
//                        .setPositiveButton("OK", (dialog, which) -> result.confirm())
//                        .setNegativeButton("Cancel", (dialogInterface, i) -> result.cancel())
////                        .setCancelable(false)
//                        .show();
//                return true;
                Toast.makeText(OriginDetailActivity.this, "Alert:\n" + message, Toast.LENGTH_LONG).show();
                return true;
            }
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(OriginDetailActivity.this)
                        .setTitle("Confirm")
                        .setMessage(message)
                        .setPositiveButton("OK", (dialog, which) -> result.confirm())
                        .setNegativeButton("Cancel", (dialog, which) -> result.cancel())
                        .setCancelable(false)
                        .show();
                       // 返回布尔值：判断点击时确认还是取消
                      // true表示点击了确认；false表示点击了取消；
                return true;
            }
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                final EditText et = new EditText(OriginDetailActivity.this);
                et.setText(defaultValue);
                new AlertDialog.Builder(OriginDetailActivity.this)
                        .setTitle(message)
                        .setView(et)
                        .setPositiveButton("OK", (dialog, which) -> result.confirm(et.getText().toString()))
                        .setNegativeButton("Cancel", (dialog, which) -> result.cancel())
                        .setCancelable(false)
                        .show();
                return true;
            }
        });
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
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack();
        }
        finish();
    }
}
