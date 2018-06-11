package com.benben.bb.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.LoginResponse;
import com.benben.bb.okhttp3.response.SmsCodeResponse;
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

public class LoginActivity extends BaseActivity {

    @Bind(R.id.login_tel)
    EditText telEdit;
    @Bind(R.id.login_msg_code_edit)
    EditText codeEdit;
    @Bind(R.id.login_msg_code)
    TextView codeTv;
    @Bind(R.id.login_hint)
    TextView hintTv;
    /**
     * 计数器
     */
    private TimeCount countTime;
    private int counter = 0;
    private String loginTel = "";
    private String loginCode = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
            i.setClass(LoginActivity.this, RegisterActivity.class);
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
        if (countTime != null) {
            countTime.cancel();
            countTime.onFinish();
        }
    }

    @OnClick({R.id.login_clear, R.id.login_msg_code, R.id.login_ok})
    public void onViewClicked(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.login_clear:
                telEdit.setText("");
                break;
            case R.id.login_msg_code:
                String temp = telEdit.getText().toString();
                if (TextUtils.isEmpty(temp)) {
                    ToastUtil.showText("请输入手机号");
                    return;
                }
                if (!Utils.isMobliePhone(temp)) {
                    ToastUtil.showText("请输入合法的手机号");
                    return;
                }
                loginTel = temp;
                sendTelCode(loginTel);
                countTime = new TimeCount(60000, 1000);// 构造CountDownTimer对象
                codeTv.setText("60S");
                counter = 60;
                countTime.start();
                codeTv.setEnabled(false);
                break;
            case R.id.login_ok:
                if (TextUtils.isEmpty(telEdit.getText().toString())) {
                    ToastUtil.showText("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(codeEdit.getText().toString())) {
                    ToastUtil.showText("请输入验证码");
                    return;
                }
                if (!loginCode.equals(codeEdit.getText().toString())) {
                    ToastUtil.showText("验证码不匹配");
                    return;
                }
                if (!loginTel.equals(telEdit.getText().toString())) {
                    ToastUtil.showText("您输入的手机号与请求验证码的不匹配");
                    return;
                }
//                loginTel = telEdit.getText().toString();
//                loginCode = codeEdit.getText().toString();
                login(loginTel, loginCode);
                break;
        }
    }

    private void sendTelCode(String phone) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("code", "0");//是否注册调用:0否;1是
        OkHttpUtils.getAsyn(NetWorkConfig.OBTAIN_TEL_CODE, params, SmsCodeResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
//                "code":1, "message":"验证码已发送", "data":"1823"
                try {
                    SmsCodeResponse scr = (SmsCodeResponse) resultDesc;
                    if (scr.getCode() == 1) {
                        loginCode = scr.getData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText("获取验证码失败：" + message);
            }
        });
    }

    private void login(String phone, final String loginCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("logCode", loginCode);
        OkHttpUtils.getAsyn(NetWorkConfig.LOGIN, params, LoginResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                try {
                    LoginResponse lr = (LoginResponse) br;
                    UserData.updateAccount(lr);
                    PreferenceUtil.commitString("LoginTel", loginTel);
                    PreferenceUtil.commitString("LoginCode", loginCode);
                    Intent i = new Intent();
                    i.setClass(LoginActivity.this, MainFragmentActivity.class);
                    startActivity(i);
                    finish();
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

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            resetAuthCodeButton();
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            counter--;
            if (counter < 0)
                counter = 0;
            codeTv.setText(counter + "S");
            codeTv.setEnabled(false);
        }
    }

    private void resetAuthCodeButton() {
        codeTv.setText("获取验证码");
        codeTv.setEnabled(true);
    }
}
