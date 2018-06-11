package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.benben.bb.MyApplication;
import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.LoginResponse;
import com.benben.bb.utils.PreferenceUtil;
import com.benben.bb.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;


public class WelcomeActivity extends BaseActivity {

    /**
     * 初次访问者角色
     */
    private Handler mHandler = new Handler();
    private String loginTel, loginCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        PreferenceUtil.init(this);
        loginTel = PreferenceUtil.getString("LoginTel", "");
        loginCode = PreferenceUtil.getString("LoginCode", "");
        if (TextUtils.isEmpty(loginTel)) {
            goLoginActivity();
        } else {
            login(loginTel, loginCode);
        }
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
        }, 2000);
    }

    private void login(String phone, final String loginCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("logCode", loginCode);
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
