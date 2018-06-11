package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-10.
 * email:shoxgov@126.com
 */

public class BandCardManagerActivity extends BaseActivity {

    @Bind(R.id.bank_manager_username)
    TextView usernameTv;
    @Bind(R.id.bank_manager_account)
    TextView accountTv;
    @Bind(R.id.bank_manager_status)
    TextView statusTv;
    @Bind(R.id.bank_manager_add)
    TextView addTv;
    @Bind(R.id.bank_manager_layout)
    RelativeLayout bankLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_manager);
        ButterKnife.bind(this);
        init();
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

    private void init() {
        if (TextUtils.isEmpty(UserData.getUserData().getBindingPayAccount())) {
            bankLayout.setVisibility(View.GONE);
        } else {
            bankLayout.setVisibility(View.VISIBLE);
            addTv.setText("更换银行卡");
            usernameTv.setText(UserData.getUserData().getTrueName());
            accountTv.setText(UserData.getUserData().getBindingPaytype() + "    (" + UserData.getUserData().getBindingPayAccount().replace(UserData.getUserData().getBindingPayAccount().substring(5, 9), "****") + ")");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.bank_manager_status, R.id.bank_manager_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bank_manager_status:
            case R.id.bank_manager_add:
                //validateStatus  0未认证1认证中2认证失败3已通过
                if (UserData.getUserData().getValidateStatus() == 3) {
                    Intent add = new Intent();
                    add.setClass(this, BankcardAddActivity.class);
                    startActivityForResult(add, 0);
                } else {
                    Intent realname = new Intent();
                    realname.setClass(this, RealNameCertifyActivity.class);
                    realname.putExtra("status", UserData.getUserData().getValidateStatus());
                    startActivity(realname);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            init();
        }
    }
}
