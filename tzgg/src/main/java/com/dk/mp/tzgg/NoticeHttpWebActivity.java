package com.dk.mp.tzgg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;

/**
 * 作者：janabo on 2017/10/26 10:54
 */
public class NoticeHttpWebActivity extends MyActivity{
    private WebView mWebView;
    private ProgressBar progressbar;
    private RelativeLayout layout_top;
    private TextView contenttitle;
    private TextView time;
    private ErrorLayout mError;

    @Override
    protected int getLayoutID() {
        return R.layout.app_notice_details;
    }

    @Override
    protected void initView() {
        super.initView();
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        layout_top = (RelativeLayout) findViewById(R.id.layout_top);
        mWebView = (WebView) findViewById(R.id.webview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        contenttitle = (TextView) findViewById(R.id.contenttitle);
        contenttitle.setText(getIntent().getStringExtra("contenttitle"));
        time = (TextView) findViewById(R.id.time);
        time.setText(getIntent().getStringExtra("time"));
        setTitle(getIntent().getStringExtra("title"));

        if (getIntent().getBooleanExtra("hideTitle", false)) {
            layout_top.setVisibility(View.GONE);
        }

        try {
            Button back = (Button) findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                }
            });
        } catch (Exception e) {
        }

        initViews();

        loadurl();
    }

    @SuppressLint("JavascriptInterface")
    private void initViews() {
        WebSettings webSetting = mWebView.getSettings();
        //设置js可用
        webSetting.setJavaScriptEnabled(true);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setLightTouchEnabled(true);
        webSetting.setSupportZoom(false);
        mWebView.setHapticFeedbackEnabled(false);
        mWebView.addJavascriptInterface(this, "android");

        //载入具体的web地址
        mWebView.setVisibility(View.VISIBLE);
        mWebView.requestFocus();
        mWebView.setWebViewClient(new HttpWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
                // 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void loadurl() {
        if (DeviceUtil.checkNet()) {
            String url = getIntent().getStringExtra("url");
            if(getIntent().getStringExtra("url") == null || getIntent().getStringExtra("url").equals("")){
                mWebView.setVisibility(View.GONE);
                mError.setErrorType(ErrorLayout.DATAFAIL);
            }else{
                mWebView.loadData(getIntent().getStringExtra("url"), "text/html; charset=UTF-8", null);
            }
        } else {
            mWebView.setVisibility(View.GONE);
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    //web视图客户端
    public class HttpWebViewClient extends WebViewClient {
        public boolean shouldOverviewUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        //开始加载
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        //结束加载
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        }
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(View.GONE);
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            } else {
                if (progressbar.getVisibility() == View.GONE)
                    progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
