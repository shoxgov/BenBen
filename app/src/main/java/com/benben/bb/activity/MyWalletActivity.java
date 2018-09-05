package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.benben.bb.R;
import com.benben.bb.adapter.SettingAdapter;
import com.benben.bb.bean.SettingItem;
import com.benben.bb.bean.UserData;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-10.
 * email:shoxgov@126.com
 */

public class MyWalletActivity extends BaseActivity {
    @Bind(R.id.mywallet_list)
    ListView list;
    @Bind(R.id.mywallet_balance)
    TextView balanceTv;
    private SettingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
                Intent detail = new Intent();
                detail.setClass(MyWalletActivity.this, MyWalletTradeDetailActivity.class);
                startActivity(detail);
            }
        });
        balanceTv.setText("¥ " + UserData.getUserData().getBalance());
        adapter = new SettingAdapter(this);
        list.setAdapter(adapter);
        List<SettingItem> data = new ArrayList<>();
        data.add(new SettingItem(0, "提现", ""));
//        data.add(new SettingItem(0, "充值", ""));
//        data.add(new SettingItem(R.mipmap.wallet_bankcard, "银行卡管理", ""));
//        if (UserData.getUserData().getValidateStatus() == 1) {//validateStatus 0未认证1已通过2认证失败3认证中
//            data.add(new SettingItem(R.mipmap.wallet_realname, "实名认证", "已认证"));
//        } else {
//            data.add(new SettingItem(R.mipmap.wallet_realname, "实名认证", ""));
//        }
//        if (UserData.getUserData().getIsCompany() != 1) {
//            data.add(new SettingItem(R.mipmap.wallet_salary_sheet, "工资条", ""));
//        }
        adapter.setData(data);
        list.setOnItemClickListener(onItemClickListener);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    withdraw();
                    break;
//                case 0:
//                    if (UserData.getUserData().getValidateStatus() != 1) {
//                        ToastUtil.showText("请先实名认证");
//                        return;
//                    }
//                    Intent bank = new Intent();
//                    bank.setClass(MyWalletActivity.this, BandCardManagerActivity.class);
//                    startActivity(bank);
//                    break;
                case 1:
                    if (UserData.getUserData().getValidateStatus() == 1) {
                        return;
                    }
                    Intent realname = new Intent();
                    realname.setClass(MyWalletActivity.this, RealNameCertifyActivity.class);
                    realname.putExtra("status", UserData.getUserData().getValidateStatus());
                    startActivity(realname);
                    break;
                case 2:
                    Intent salary = new Intent();
                    salary.setClass(MyWalletActivity.this, MySalaryListActivity.class);
                    startActivity(salary);
                    break;
            }
        }
    };

//    @OnClick(R.id.mywallet_withdraw)
//    public void onViewClicked(View v) {
//        switch (v.getId()) {
//            case R.id.mywallet_withdraw:
//                withdraw();
//                break;
//        }
//    }

    private void withdraw() {
        if (UserData.getUserData().getBalance() <= 0) {
            ToastUtil.showText("当前无余额可提现");
            return;
        }
        if (UserData.getUserData().getValidateStatus() != 1) {//0未认证3认证中2认证失败1已通过
            ToastUtil.showText("你还未通过实名认证");
            return;
        }
        Intent withdraw = new Intent();
        withdraw.setClass(MyWalletActivity.this, WithdrawActivity.class);
        startActivityForResult(withdraw, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            balanceTv.setText("¥ " + UserData.getUserData().getBalance());
        }
    }
}
