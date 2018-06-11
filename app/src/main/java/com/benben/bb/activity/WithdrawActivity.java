package com.benben.bb.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyRecruitResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.TitleBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-10.
 * email:shoxgov@126.com
 */

public class WithdrawActivity extends BaseActivity {

    @Bind(R.id.withdraw_bancard)
    TextView bancardTv;
    @Bind(R.id.withdraw_count)
    EditText countTv;
    @Bind(R.id.withdraw_balance)
    TextView balanceTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
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
        String cardNo = UserData.getUserData().getBindingPayAccount();
        bancardTv.setText(UserData.getUserData().getBindingPaytype() + "(****" + cardNo.substring(cardNo.length() - 4, cardNo.length()) + ")");
        balanceTv.setText("可用余额：¥ " + UserData.getUserData().getBalance());
    }

    @OnClick({R.id.withdraw_all, R.id.withdraw_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.withdraw_all:
                withdraw(UserData.getUserData().getBalance());
                break;
            case R.id.withdraw_submit:
                String temp = countTv.getText().toString();
                if (TextUtils.isEmpty(temp) || temp.equals("0")) {
                    ToastUtil.showText("请输入提现金额");
                    return;
                }
                try {
                    float count = Float.parseFloat(temp);
                    if (count > UserData.getUserData().getBalance()) {
                        ToastUtil.showText("提现金额不能超出可提现余额");
                        return;
                    }
                    withdraw(count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void withdraw(final float count) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("money", count + "");
//        params.put("billDetail",  "");
        OkHttpUtils.getAsyn(NetWorkConfig.USER_WITHDRAW, params, BaseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                try {
                    if (br.getCode() == 1) {
                        UserData.getUserData().setBalance(UserData.getUserData().getBalance() - count);
                        setResult(RESULT_OK);
                        finish();
                    }
                    ToastUtil.showText(br.getMessage());
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
}
