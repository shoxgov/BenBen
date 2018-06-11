package com.benben.bb.activity;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.benben.bb.R;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.utils.DialogUtil;
import com.benben.bb.view.TitleBar;


public class WebActivity extends BaseActivity {

    private WebView webView;
    private String title, url;
    private ProgressBar webProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        TitleBar titleBar = (TitleBar) findViewById(R.id.titlelayout);
        titleBar.setTitleName(title);
        titleBar.setTitleBarListener(new TitleBarListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        webView = (WebView) findViewById(R.id.webview);
        webProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        Log.d("ZCPICE", "WebActivity url= " + url);
        initWebview();
        init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        finish();
        return super.onKeyDown(keyCode, event);
    }

    private void initWebview() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //启动缓存
        settings.setAppCacheEnabled(true);
        //设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 设置加载进来的页面自适应手机屏幕 ,页面适应手机屏幕的分辨率，完整的显示在屏幕上，可以放大缩小
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
        settings.setUseWideViewPort(true);// 设置webview推荐使用的窗口，设置为true);设置此属性，可任意比例缩放。
        settings.setLoadWithOverviewMode(true);// 设置webview加载的页面的模式，也设置为true
        settings.setDisplayZoomControls(false); // 隐藏webview缩放按钮
        settings.setSupportZoom(false); // 支持缩放
        settings.setBuiltInZoomControls(true);
        ///////////////
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true);

        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(webView.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
                super.onPageFinished(view, url);
            }
        });
        webView.addJavascriptInterface(this, "App");
    }

    private void init() {
        gotoUrl();
    }

    private void gotoUrl() {

        DialogUtil.showDialogLoading(this, "");
        webView.loadUrl(url);
        // 防止跳到系统浏览器
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest url) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(url.getUrl().toString());
                } else {
                    view.loadUrl(url.toString());
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                DialogUtil.hideDialogLoading();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Toast.makeText(WebActivity.this, "网页加载出错!", Toast.LENGTH_LONG).show();
                DialogUtil.hideDialogLoading();
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    webProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    webProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    webProgressBar.setProgress(newProgress);//设置进度值
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    /**
     * 重新计算Webview的高度，以便滑动
     *
     * @param height
     */
    @JavascriptInterface
    public void resize(final float height) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
            }
        });
    }

}
