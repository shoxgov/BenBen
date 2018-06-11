package com.benben.bb.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-29.
 * email:shoxgov@126.com
 */

public class BankcardAddActivity extends BaseActivity {

    @Bind(R.id.bank_add_account_type)
    EditText accountType;
    @Bind(R.id.bank_add_account_no)
    EditText accountNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankcard_add);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.bank_add_ok)
    public void onViewClicked() {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        final String type = accountType.getText().toString();
        final String no = accountNo.getText().toString();
        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(no)) {
            ToastUtil.showText("请完善信息");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("bindingPaytype", type);
        params.put("bindingPayAccount", no);
        OkHttpUtils.postAsynFile(NetWorkConfig.USER_UPDATEINFO, "", null, params, BaseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                super.onSuccess(baseResponse);
                if (baseResponse.getCode() == 1) {
                    UserData.getUserData().setBindingPaytype(type);
                    UserData.getUserData().setBindingPayAccount(no);
                    setResult(RESULT_OK);
                    finish();
                }
                ToastUtil.showText(baseResponse.getMessage());

            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText(message);
            }
        });
    }
}
