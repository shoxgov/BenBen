package com.benben.bb.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.SmsCodeResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-07-30.
 * email:shoxgov@126.com
 */

public class FindPwdActivity extends BaseActivity {

    @Bind(R.id.findpwd_tel)
    EditText telEdit;
    @Bind(R.id.findpwd_code)
    EditText codeEdit;
    @Bind(R.id.findpwd_newpwd)
    EditText newPwdEdit;
    //    @Bind(R.id.findpwd_imagecode)
//    ImageView imageCode;
    @Bind(R.id.findpwd_ok)
    Button okBtn;
    @Bind(R.id.findpwd_smscode)
    TextView smsTx;
    /**
     * 计数器
     */
    private TimeCount countTime;
    private int counter = 0;
    private String loginTel = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (countTime != null) {
            countTime.cancel();
            countTime.onFinish();
        }
    }

    private void init() {
        TitleBar titleLayout = (TitleBar) findViewById(R.id.titlelayout);
        titleLayout.setTitleBarListener(new TitleBarListener() {

            @Override
            public void rightClick() {
            }

            @Override
            public void leftClick() {
                finish();
            }
        });
//        String code = ImageCodeUtils.createCode();
//        okBtn.setTag(code);
//        imageCode.setImageBitmap(ImageCodeUtils.createBitmap(code));
    }

    @OnClick({/*R.id.findpwd_imagecode, */R.id.findpwd_tel_clear, R.id.findpwd_ok, R.id.findpwd_smscode})
    public void onViewClicked(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
//            case R.id.findpwd_imagecode:
//                String code = ImageCodeUtils.createCode();
//                okBtn.setTag(code);
//                imageCode.setImageBitmap(ImageCodeUtils.createBitmap(code));
//                break;
            case R.id.findpwd_tel_clear:
                telEdit.setText("");
                break;
            case R.id.findpwd_ok:
                String tel = telEdit.getText().toString();
                String code = codeEdit.getText().toString();
                String newpwd = newPwdEdit.getText().toString();
                if (TextUtils.isEmpty(tel)) {
                    ToastUtil.showText("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showText("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(newpwd)) {
                    ToastUtil.showText("请输入密码");
                    return;
                }
                if (!loginTel.equals(tel)) {
                    ToastUtil.showText("您输入的手机号与请求验证码的不匹配");
                    return;
                }
                if (okBtn.getTag() == null || !code.equals(okBtn.getTag().toString())) {
                    ToastUtil.showText("输入验证码不正确");
                    return;
                }
//                Intent vertifyCode = new Intent();
//                vertifyCode.setClass(this, PhoneVertifyCodeActivity.class);
//                startActivity(vertifyCode);
                resetPwd(tel, newpwd, code);
                break;

            case R.id.findpwd_smscode:
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
                smsTx.setText("60S");
                counter = 60;
                countTime.start();
                smsTx.setEnabled(false);
                break;
        }
    }

    /**
     * phone
     * 手机号码
     * passWord
     * 密码
     * vCode
     * 验证码
     */
    private void resetPwd(String phone, String newPwd, String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("passWord", newPwd);
        params.put("vCode", code);
        OkHttpUtils.getAsyn(NetWorkConfig.RESET_PWD, params, SmsCodeResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
//                "code":1, "message":"验证码已发送", "data":"1823"
                try {
                    ToastUtil.showText(br.getMessage());
                    if (br.getCode() == 1) {
                        finish();
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText("修改失败：" + message);
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
            smsTx.setText(counter + "S");
            smsTx.setEnabled(false);
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
                    ToastUtil.showText(scr.getMessage());
                    if (scr.getCode() == 1) {
                        okBtn.setTag(scr.getData());
                    } else {
                        resetAuthCodeButton();
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

    private void resetAuthCodeButton() {
        smsTx.setText("获取验证码");
        smsTx.setEnabled(true);
    }
}
