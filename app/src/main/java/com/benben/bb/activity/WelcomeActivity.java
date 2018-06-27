package com.benben.bb.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.LoginResponse;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.PreferenceUtil;
import com.benben.bb.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;


public class WelcomeActivity extends BaseActivity {

    /**
     * 初次访问者角色
     */
    private Handler mHandler = new Handler();
    private String loginTel, passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("WelcomeActivity onCreate");
        setContentView(R.layout.activity_welcome);
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", "支付宝发红包啦！即日起还有机会额外获得余额宝消费红包！长按复制此消息，打开最新版支付宝就能领取！a3oAgG56Ip");
// 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        PreferenceUtil.init(this);
        loginTel = PreferenceUtil.getString("LoginTel", "");
        passWord = PreferenceUtil.getString("passWord", "");
        LogUtil.d("WelcomeActivity  loginTel=" + loginTel + ",passWord=" + passWord);
        if (TextUtils.isEmpty(loginTel)) {
            goLoginActivity();
        } else {
            login();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
    }

    private void goMainActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent();
                i.setClass(WelcomeActivity.this, MainFragmentActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);
    }

    private void goLoginActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent();
                i.setClass(WelcomeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);
    }

    private void login() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", loginTel);
        if (!TextUtils.isEmpty(passWord)) {
            params.put("passWord", passWord);
        } else {
            goLoginActivity();
        }
        OkHttpUtils.getAsyn(NetWorkConfig.LOGIN, params, LoginResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    LoginResponse lr = (LoginResponse) resultDesc;
                    if (lr.getCode() == 1) {
                        UserData.updateAccount(lr);
                        goMainActivity();
                    } else {
                        ToastUtil.showText(lr.getMessage());
                        goLoginActivity();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText(message);
                goLoginActivity();
            }
        });
    }
}
