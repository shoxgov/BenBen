package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.PagerFragmentsAdapter;
import com.benben.bb.fragment.BrokerMyresourceSignupFragment;
import com.benben.bb.fragment.BrokerMyresourceSignupingFragment;
import com.benben.bb.fragment.BrokerMyresourceTotalFragment;
import com.benben.bb.fragment.BrokerMyresourceWokingFragment;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.MyResourceCountsResponse;
import com.benben.bb.view.TabPageIndicator;
import com.benben.bb.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-05-24.
 * email:shoxgov@126.com
 */

public class BrokerMyResourceActivity extends BaseFragmentActivity {
    @Bind(R.id.indicator)
    TabPageIndicator indicator;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    private PagerFragmentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_fragment);
        ButterKnife.bind(this);
        initview();
        String[] titles = getResources().getStringArray(R.array.broker_myres_signuping_status);//我的资源,可报名,报名中,已入职
        Fragment[] fragments = {new BrokerMyresourceTotalFragment(), new BrokerMyresourceSignupFragment(), new BrokerMyresourceSignupingFragment(), new BrokerMyresourceWokingFragment()};
        adapter = new PagerFragmentsAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(adapter);// 设置adapter
        viewPager.setOffscreenPageLimit(4);
        indicator.setViewPager(viewPager);// 绑定indicator
//        setTabPagerIndicator();
        requestFragmentCount();
        initTagFragment(1, 1, 1, 1);
    }

    /**
     * 报名中 signNum,已入职entryNum,我的资源resourcesNum,可报名enrollNum
     *
     * @param resources
     * @param enroll
     * @param sign
     * @param entry
     */
    private void initTagFragment(int resources, int enroll, int sign, int entry) {
        String[] titles = getResources().getStringArray(R.array.broker_myres_signuping_status);//我的资源,可报名,报名中,已入职
        if (resources > 0) {
            if (resources > 99) {
                titles[0] += "(99+)";
            } else {
                titles[0] += "(" + resources + ")";
            }
            if (enroll > 99) {
                titles[1] += "(99+)";
            } else {
                titles[1] += "(" + enroll + ")";
            }
            if (sign > 99) {
                titles[2] += "(99+)";
            } else {
                titles[2] += "(" + sign + ")";
            }
            if (entry > 99) {
                titles[3] += "(99+)";
            } else {
                titles[3] += "(" + entry + ")";
            }
        }
        adapter.setTitles(titles);
        setTabPagerIndicator();
    }

    private void requestFragmentCount() {
        OkHttpUtils.getAsyn(NetWorkConfig.BROKER_RESOURCE_COUNTS, MyResourceCountsResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                MyResourceCountsResponse mcr = (MyResourceCountsResponse) resultDesc;
                if (mcr.getCode() == 1) {
                    initTagFragment(mcr.getData().getResourcesNum(), mcr.getData().getEnrollNum(), mcr.getData().getSignNum(), mcr.getData().getEntryNum());
                } else {
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    /**
     * 通过一些set方法，设置控件的属性
     */
    private void setTabPagerIndicator() {
        indicator.setIndicatorMode(TabPageIndicator.IndicatorMode.MODE_WEIGHT_NOEXPAND_NOSAME);// 设置模式，一定要先设置模式
//        indicator.setDividerColor(Color.parseColor("#00bbcf"));// 设置分割线的颜色
//        indicator.setDividerPadding(10);//设置
        indicator.setIndicatorColor(getResources().getColor(R.color.bluetheme));// 设置底部导航线的颜色
        indicator.setTextColorSelected(getResources().getColor(R.color.textgrey));// 设置tab标题选中的颜色
        indicator.setTextColor(getResources().getColor(R.color.textgrey));// 设置tab标题未被选中的颜色
        indicator.setTextSize(getResources().getDimensionPixelSize(R.dimen.font_small));// 设置字体大小
    }

    private void initview() {
        TitleBar titleLayout = (TitleBar) findViewById(R.id.titlelayout);
        titleLayout.setTitleName("人力资源");
//        titleLayout.setRightVisible(true, "邀请");
        titleLayout.setTitleBarListener(new TitleBarListener() {

            @Override
            public void rightClick() {
                Intent qr = new Intent();
                qr.setClass(BrokerMyResourceActivity.this, PersonQrActivity.class);
                startActivity(qr);
            }

            @Override
            public void leftClick() {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
