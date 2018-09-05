package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.benben.bb.R;
import com.benben.bb.adapter.SettingAdapter;
import com.benben.bb.bean.SettingItem;
import com.benben.bb.bean.UserData;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-05-11.
 * email:shoxgov@126.com
 */

public class BrokerActivity extends BaseActivity {

    @Bind(R.id.broker_list)
    ListView list;
    @Bind(R.id.broker_vip)
    TextView vipTv;
//    @Bind(R.id.broker_truename)
//    TextView truenameTv;
//    @Bind(R.id.broker_id)
//    TextView idTv;
    private SettingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker);
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

            }
        });
        vipTv.setText(Html.fromHtml("就业顾问V" + UserData.getUserData().getIsAgent() + "<font color=#cccccc>（可设置" + (UserData.getUserData().getIsAgent() - 1) + "个就业顾问）</font>"));
//        truenameTv.setText(UserData.getUserData().getTrueName());
//        if (!TextUtils.isEmpty(UserData.getUserData().getIdentityCard())) {
//            try {
//                idTv.setText(UserData.getUserData().getIdentityCard().replace(UserData.getUserData().getIdentityCard().substring(4, 8), "****"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        adapter = new SettingAdapter(this);
        list.setAdapter(adapter);
        List<SettingItem> data = new ArrayList<>();
        data.add(new SettingItem(R.mipmap.broker_myresource, "我的资源", ""));
//        data.add(new SettingItem(R.mipmap.broker_statics, "业务统计", ""));
        data.add(new SettingItem(R.mipmap.broker_myteam, "我的就业顾问", ""));
        adapter.setData(data);
        list.setOnItemClickListener(onItemClickListener);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    Intent res = new Intent();
                    res.setClass(BrokerActivity.this, BrokerMyResourceActivity.class);
                    startActivity(res);
                    break;
                case 1:
//                    Intent statistics = new Intent();
//                    statistics.setClass(BrokerActivity.this, BrokerBusinessStatisticsActivity.class);
//                    startActivity(statistics);
//                    break;
//                case 2:
                    Intent team = new Intent();
                    team.setClass(BrokerActivity.this, BrokerMyteamActivity.class);
                    startActivity(team);
                    break;
            }
        }
    };
}
