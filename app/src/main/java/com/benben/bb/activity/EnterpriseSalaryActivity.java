package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyRecruitResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.TitleBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-05-15.
 * email:shoxgov@126.com
 */

public class EnterpriseSalaryActivity extends BaseActivity {
    @Bind(R.id.mainlayout)
    LinearLayout mainlayout;
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recyclerview);
        ButterKnife.bind(this);
        initViews();
        init();
    }

    private void initViews() {
        TitleBar titleLayout = (TitleBar) findViewById(R.id.titlelayout);
        titleLayout.setTitleName("工资核算");
        titleLayout.setTitleBarListener(new TitleBarListener() {

            @Override
            public void rightClick() {

            }

            @Override
            public void leftClick() {
                finish();
            }
        });
        mainlayout.setBackgroundResource(R.color.mainbg);
        recyclerSwipeLayout.createAdapter(R.layout.list_enterprise_salary);
        recyclerSwipeLayout.setDividerColor(this, getResources().getColor(R.color.mainbg), Utils.dip2px(this, 5));
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
    }

    private void init() {
        requestEmployList();
    }

    private void requestEmployList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        OkHttpUtils.getAsyn(NetWorkConfig.COMPANY_RECRUIT_LIST, params, CompanyRecruitResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    CompanyRecruitResponse crr = (CompanyRecruitResponse) resultDesc;
                    if (crr.getCode() == 1) {
                        pageNo = crr.getData().getPageNum();
                        totalPage = crr.getData().getPages();
                        List<CompanyRecruitResponse.RecruitInfo> temp = crr.getData().getList();
                        if (temp == null || temp.isEmpty()) {
                            if (totalPage == 0) {
                                recyclerSwipeLayout.setEmpty();
                            }
                            return;
                        }
                        recyclerSwipeLayout.addData(temp);
                        recyclerSwipeLayout.loadComplete();
                    }
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

    BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener = new BaseQuickAdapter.RequestLoadMoreListener() {

        @Override
        public void onLoadMoreRequested() {
            if (pageNo < totalPage) {
                pageNo++;
                requestEmployList();
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };


    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final CompanyRecruitResponse.RecruitInfo ri = (CompanyRecruitResponse.RecruitInfo) itemModel;
            baseViewHolder.setText(R.id.enterprise_salary_item_positionname, ri.getPositionName());
            baseViewHolder.setText(R.id.enterprise_salary_item_salary, ri.getSalary() + "元/小时");
            baseViewHolder.setText(R.id.enterprise_salary_item_commission, ri.getCommision() + "元/小时");
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent();
                    detail.setClass(EnterpriseSalaryActivity.this, EnterpriseSalaryDetailActivity.class);
                    detail.putExtra("id", ri.getId());
                    detail.putExtra("companyId", ri.getCompanyId());
                    detail.putExtra("positionName", ri.getPositionName());
                    detail.putExtra("salary", ri.getSalary());
                    detail.putExtra("commision", ri.getCommision());
                    startActivity(detail);
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
