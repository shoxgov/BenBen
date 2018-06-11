package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.benben.bb.R;
import com.benben.bb.adapter.PagerFragmentsAdapter;
import com.benben.bb.fragment.BrokerMyresourceSignupingFragment;
import com.benben.bb.fragment.BrokerMyresourceUnentryFragment;
import com.benben.bb.fragment.EnterpriseEmployeeEntryedFragment;
import com.benben.bb.fragment.EnterpriseEmployeeUnentryFragment;
import com.benben.bb.imp.TitleBarListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_fragment);
        ButterKnife.bind(this);
        initview();
        String[] titles = getResources().getStringArray(R.array.broker_myres_signuping_status);
        Fragment[] fragments = {new BrokerMyresourceUnentryFragment(), new BrokerMyresourceSignupingFragment()};
        PagerFragmentsAdapter adapter = new PagerFragmentsAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(adapter);// 设置adapter
        viewPager.setOffscreenPageLimit(2);
        indicator.setViewPager(viewPager);// 绑定indicator
        setTabPagerIndicator();
    }

    /**
     * 通过一些set方法，设置控件的属性
     */
    private void setTabPagerIndicator() {
        indicator.setIndicatorMode(TabPageIndicator.IndicatorMode.MODE_WEIGHT_NOEXPAND_SAME);// 设置模式，一定要先设置模式
//        indicator.setDividerColor(Color.parseColor("#00bbcf"));// 设置分割线的颜色
//        indicator.setDividerPadding(10);//设置
        indicator.setIndicatorColor(getResources().getColor(R.color.bluetheme));// 设置底部导航线的颜色
        indicator.setTextColorSelected(getResources().getColor(R.color.textgrey));// 设置tab标题选中的颜色
        indicator.setTextColor(getResources().getColor(R.color.textgrey));// 设置tab标题未被选中的颜色
        indicator.setTextSize(getResources().getDimensionPixelSize(R.dimen.font_normal));// 设置字体大小
    }

    private void initview() {
        TitleBar titleLayout = (TitleBar) findViewById(R.id.titlelayout);
        titleLayout.setTitleName("我的资源");
        titleLayout.setRightVisible(true, "邀请");
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
