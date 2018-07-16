package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.SignupDialog;
import com.benben.bb.dialog.SignupSuccessDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.share.AndroidShare;
import com.benben.bb.share.Defaultcontent;
import com.benben.bb.share.ShareStyle;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.NoAdWebViewClient;
import com.benben.bb.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-16.
 * email:shoxgov@126.com
 */

public class RecruitDetailActivityBake extends BaseActivity {
    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.recruit_detail_agentsignup)
    TextView agentSignup;

    private String url;
    /**
     * 职位
     */
    private int positionId;
    private String positionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_detail_bake);
        ButterKnife.bind(this);
        //H5 职位详情   http://ip或域名/show?id=职位ID
        positionId = getIntent().getIntExtra("positionId", 0);
        positionName = getIntent().getStringExtra("positionName");
        url = NetWorkConfig.HTTP + "/show?id=" + positionId;//getIntent().getStringExtra("url");
        TitleBar titleBar = (TitleBar) findViewById(R.id.titlelayout);
        titleBar.setTitleBarListener(new TitleBarListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        LogUtil.d("WebActivity url= " + url);
        initWebview();
        init();
        if (UserData.getUserData().getIsAgent() > 0 && UserData.getUserData().getIsAgent() < 88) {
            agentSignup.setVisibility(View.VISIBLE);
        }
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
                webView.loadUrl("javascript:ltxApp.resize(document.body.getBoundingClientRect().height)");
                super.onPageFinished(view, url);
            }
        });
        webView.addJavascriptInterface(this, "ltxApp");
    }

    private void init() {
        gotoUrl();
    }

    private void gotoUrl() {
        webView.loadUrl(url);
        // 防止跳到系统浏览器
        webView.setWebViewClient(new NoAdWebViewClient(this, webView) {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                webView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                ToastUtil.showText("网页加载出错!");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                //拦截垃圾电信运营商的广告方法  关键代码
                if (!url.contains("zhibenben")) {
                    return new WebResourceResponse(null, null, null);
                }
                return super.shouldInterceptRequest(view, url);
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
                webView.setLayoutParams(new RelativeLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.recruit_detail_signup, R.id.recruit_detail_share, R.id.recruit_detail_agentsignup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recruit_detail_signup:
                if (UserData.getUserData().getValidateStatus() != 1) {//validateStatus 0未认证1已通过2认证失败3认证中
                    ToastUtil.showText("未实名认证用户不能报名");
                    return;
                }
                if (UserData.getUserData().getIsCompany() == 1) {
                    ToastUtil.showText("企业用户不能报名");
                    return;
                }
                SignupDialog signupDialog = new SignupDialog(this, new DialogCallBack() {
                    @Override
                    public void OkDown(Object score) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("positionId", positionId + "");//职位ID
                        params.put("positionName", positionName);//职位
                        params.put("code", "2");//1经纪人报名2自主报名
                        params.put("userIds", UserData.getUserData().getId() + "");//用户ID逗号分隔
                        OkHttpUtils.getAsyn(NetWorkConfig.USER_SIGNUP_EVENT, params, BaseResponse.class, new HttpCallback() {
                            @Override
                            public void onSuccess(BaseResponse br) {
                                super.onSuccess(br);
                                try {
                                    if (br.getCode() == 1) {
                                        SignupSuccessDialog signupSucDialog = new SignupSuccessDialog(RecruitDetailActivityBake.this, new DialogCallBack() {
                                            @Override
                                            public void OkDown(Object score) {

                                            }

                                            @Override
                                            public void CancleDown() {

                                            }
                                        });
                                        signupSucDialog.show();
                                    } else {
                                        ToastUtil.showText(br.getMessage());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int code, String message) {
                                super.onFailure(code, message);
                                ToastUtil.showText(message);
                            }
                        });
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                signupDialog.show();
                break;

            case R.id.recruit_detail_agentsignup:
                if (UserData.getUserData().getValidateStatus() != 1) {//validateStatus 0未认证1已通过2认证失败3认证中
                    ToastUtil.showText("未实名认证用户不能报名");
                    return;
                }
                if (UserData.getUserData().getIsCompany() == 1) {
                    ToastUtil.showText("企业用户不能报名");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(RecruitDetailActivityBake.this, BrokerSignupActivity.class);
                intent.putExtra("positionId", positionId);
                intent.putExtra("positionName", positionName);
                startActivity(intent);
                break;
            case R.id.recruit_detail_share:
                Intent share = new Intent();
                share.setClass(this, AndroidShare.class);
                share.putExtra("text", "犇犇分享！");
                share.putExtra("type", ShareStyle.ShareType.WEB.ordinal());
                share.putExtra("url", url);
                share.putExtra("title", "一款好工作会赚钱的APP-职犇犇");
                share.putExtra("description", Defaultcontent.description);
                startActivity(share);
                break;
        }
    }
}
