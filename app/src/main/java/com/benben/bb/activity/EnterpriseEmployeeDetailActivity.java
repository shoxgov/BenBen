package com.benben.bb.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.widget.TextView;

import com.benben.bb.R;
import com.benben.bb.adapter.PagerFragmentsAdapter;
import com.benben.bb.fragment.EnterpriseEmployeeEntryedFragment;
import com.benben.bb.fragment.EnterpriseEmployeeUnentryFragment;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.view.TabPageIndicator;
import com.benben.bb.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-05-15.
 * email:shoxgov@126.com
 */

public class EnterpriseEmployeeDetailActivity extends BaseFragmentActivity {
    @Bind(R.id.indicator)
    TabPageIndicator indicator;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.enterprise_employee_detail_positionname)
    TextView positionnameTv;
    @Bind(R.id.enterprise_employee_detail_hint)
    TextView hintTv;
    private int positionId;

    public int getPositionId() {
        return positionId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_employee_detail);
        ButterKnife.bind(this);
        positionId = getIntent().getIntExtra("positionId", 0);
        positionnameTv.setText("" + getIntent().getStringExtra("positionName"));
        hintTv.setText(Html.fromHtml("<font color=#9B9B9B>招聘：</font>"+getIntent().getIntExtra("enrollNum", 0) + "/" + getIntent().getIntExtra("hiringCount", 0)));
        initview();
        String[] titles = getResources().getStringArray(R.array.enterprise_employee_status);
        Fragment[] fragments = {new EnterpriseEmployeeUnentryFragment(), new EnterpriseEmployeeEntryedFragment()};
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
        titleLayout.setTitleBarListener(new TitleBarListener() {

            @Override
            public void rightClick() {
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
