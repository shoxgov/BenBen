package com.benben.bb.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import com.benben.bb.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-09.
 * email:shoxgov@126.com
 */

public class LoginPwdActivity extends BaseActivity {

    @Bind(R.id.loginpwd_tel)
    EditText telEdit;
    @Bind(R.id.loginpwd_edit)
    EditText pwdEdit;
    @Bind(R.id.loginpwd_hint)
    TextView hintTv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pwd);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void init() {
//        hintTv.setText(Html.fromHtml("还没账户? <font color=#3b9cff>立即注册</font>"));
        SpannableString spanableInfo = new SpannableString("还没账户?立即注册");
        spanableInfo.setSpan(new Clickable(clickListener), 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        hintTv.setText(spanableInfo);
        hintTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            i.setClass(LoginPwdActivity.this, RegisterActivity.class);
            startActivity(i);
            finish();
        }
    };

    class Clickable extends ClickableSpan {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#3b9cff"));
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @OnClick({R.id.loginpwd_clear, R.id.loginpwd_code, R.id.loginpwd_ok,R.id.login_findpwd})
    public void onViewClicked(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.loginpwd_clear:
                telEdit.setText("");
                break;
            case R.id.loginpwd_ok:
                if (TextUtils.isEmpty(telEdit.getText().toString())) {
                    ToastUtil.showText("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(pwdEdit.getText().toString())) {
                    ToastUtil.showText("您输入登录密码");
                    return;
                }
                login(telEdit.getText().toString(), pwdEdit.getText().toString());
                break;
            case R.id.loginpwd_code:
                Intent i = new Intent();
                i.setClass(LoginPwdActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.login_findpwd:
                Intent find = new Intent();
                find.setClass(LoginPwdActivity.this, FindPwdActivity.class);
                startActivity(find);
                break;
        }
    }


    private void login(final String phone, final String passWord) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("passWord", Utils.getMd5(passWord));
        OkHttpUtils.getAsyn(NetWorkConfig.LOGIN, params, LoginResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                try {
                    LoginResponse lr = (LoginResponse) br;
                    if (lr.getCode() == 1) {
                        UserData.updateAccount(lr);
                        PreferenceUtil.commitString("LoginTel", phone);
                        PreferenceUtil.commitString("passWord", Utils.getMd5(passWord));
                        Intent i = new Intent();
                        i.setClass(LoginPwdActivity.this, MainFragmentActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        ToastUtil.showText(lr.getMessage());
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MyApplication.finishAllActivity();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
