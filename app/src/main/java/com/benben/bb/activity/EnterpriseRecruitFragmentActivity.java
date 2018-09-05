package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.benben.bb.R;
import com.benben.bb.adapter.PagerFragmentsAdapter;
import com.benben.bb.fragment.BrokerMyresourceSignupFragment;
import com.benben.bb.fragment.BrokerMyresourceSignupingFragment;
import com.benben.bb.fragment.BrokerMyresourceTotalFragment;
import com.benben.bb.fragment.BrokerMyresourceWokingFragment;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.response.CompanyRecruitResponse;
import com.benben.bb.view.TabPageIndicator;
import com.benben.bb.view.TitleBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-05-24.
 * email:shoxgov@126.com
 */

public class EnterpriseRecruitFragmentActivity extends BaseFragmentActivity {
    @Bind(R.id.indicator)
    TabPageIndicator indicator;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    private EnterpriseRecruitFragment fragment1, fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_fragment);
        ButterKnife.bind(this);
        initview();
        String[] titles = getResources().getStringArray(R.array.enterprise_recruit_type);
        fragment1 = EnterpriseRecruitFragment.newInstance("0");
        fragment2 = EnterpriseRecruitFragment.newInstance("1");
        Fragment[] fragments = {fragment1, fragment2};
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
        titleLayout.setTitleName("招聘岗位");
        titleLayout.setRightVisible(true, "新增");
        titleLayout.setTitleBarListener(new TitleBarListener() {

            @Override
            public void rightClick() {
                Intent add = new Intent();
                add.setClass(EnterpriseRecruitFragmentActivity.this, EnterpriseAddRecruitActivity.class);
                startActivityForResult(add, 11);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            fragment1.freshUp();
            fragment2.freshUp();
        }
    }
}
