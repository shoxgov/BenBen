package com.benben.bb.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
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

public class RegisterActivity extends BaseActivity {


    @Bind(R.id.register_tel)
    EditText telEdit;
    @Bind(R.id.register_msg_code_edit)
    EditText codeEdit;
    @Bind(R.id.register_msg_code)
    TextView codeTv;
    @Bind(R.id.register_hint)
    TextView hintTv;
    @Bind(R.id.register_invite_code_edit)
    EditText inviteCodeEdit;
    @Bind(R.id.register_agreement_cb)
    CheckBox agreementCb;
    /**
     * 计数器
     */
    private TimeCount countTime;
    private int counter = 0;
    private String registerTel = "";
    private String registerCode = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void init() {
        hintTv.setText(Html.fromHtml("已有账户，<font color=#3b9cff>立即登录</font>"));
        SpannableString spanableInfo = new SpannableString("已有账户，立即登录");
        spanableInfo.setSpan(new Clickable(clickListener), 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        hintTv.setText(spanableInfo);
        hintTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            i.setClass(RegisterActivity.this, LoginActivity.class);
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

    @OnClick({R.id.register_clear, R.id.register_msg_code, R.id.register_ok})
    public void onViewClicked(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.register_clear:
                telEdit.setText("");
                break;
            case R.id.register_msg_code:
                String temp = telEdit.getText().toString();
                if (TextUtils.isEmpty(temp)) {
                    ToastUtil.showText("请输入手机号");
                    return;
                }
                if (!Utils.isMobliePhone(temp)) {
                    ToastUtil.showText("请输入合法的手机号");
                    return;
                }
                sendTelCode(temp);
                countTime = new TimeCount(60000, 1000);// 构造CountDownTimer对象
                codeTv.setText("60S");
                counter = 60;
                countTime.start();
                codeTv.setEnabled(false);
                break;
            case R.id.register_ok:
                if (TextUtils.isEmpty(telEdit.getText().toString())) {
                    ToastUtil.showText("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(codeEdit.getText().toString())) {
                    ToastUtil.showText("请输入验证码");
                    return;
                }
                if (!registerCode.equals(codeEdit.getText().toString())) {
                    ToastUtil.showText("验证码不匹配");
                    return;
                }
                if (!registerTel.equals(telEdit.getText().toString())) {
                    ToastUtil.showText("您输入的手机号与请求验证码的不匹配");
                    return;
                }
                if (!agreementCb.isChecked()) {
                    ToastUtil.showText("请同意用户协议");
                    return;
                }
                register(registerTel, registerCode, inviteCodeEdit.getText().toString());
                break;
        }
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

    private void sendTelCode(final String phone) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("code", "1");//是否注册调用:0否;1是
        OkHttpUtils.getAsyn(NetWorkConfig.OBTAIN_TEL_CODE, params, SmsCodeResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
//                "code":1, "message":"验证码已发送", "data":"1823"
                SmsCodeResponse scr = (SmsCodeResponse) br;
                if (scr.getCode() == 1) {
                    registerTel = phone;
                    registerCode = scr.getData();
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText("获取验证码失败：" + message);
            }
        });
    }

    private void register(String phone, final String loginCode, final String inviteCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        if (!TextUtils.isEmpty(inviteCode)) {
            params.put("userCode", inviteCode);//邀请码
        }
        params.put("regCode", loginCode);
        PreferenceUtil.init(this);
        OkHttpUtils.getAsyn(NetWorkConfig.REGISTER, params, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                PreferenceUtil.commitString("LoginTel", registerTel);
                PreferenceUtil.commitString("LoginCode", registerCode);
                ToastUtil.showText("注册成功");
                Intent i = new Intent();
                i.setClass(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
