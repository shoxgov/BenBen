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
import com.benben.bb.okhttp3.response.CompanyEmployeeResponse;
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

public class EnterpriseEmployeeActivity extends BaseActivity {
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
        titleLayout.setTitleName("应聘管理");
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
        recyclerSwipeLayout.createAdapter(R.layout.list_enterprise_employee);
        recyclerSwipeLayout.setDividerColor(this, getResources().getColor(R.color.mainbg), Utils.dip2px(this, 10));
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
    }

    private void init() {
        requestRecruit();
    }

    private void requestRecruit(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        OkHttpUtils.getAsyn(NetWorkConfig.COMPANY_EMPLOYEE_LIST, params, CompanyEmployeeResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    CompanyEmployeeResponse mar = (CompanyEmployeeResponse) resultDesc;
                    if (mar.getCode() == 1) {
                        pageNo = mar.getData().getPageNum();
                        totalPage = mar.getData().getPages();
                        if (totalPage == 0) {
                            recyclerSwipeLayout.setEmpty();
                            return;
                        }
                        List<CompanyEmployeeResponse.EmployeeInfo> temp = mar.getData().getList();
                        recyclerSwipeLayout.openLoadMore(totalPage);
                        recyclerSwipeLayout.addData(temp);
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
                requestRecruit();
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };


    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final CompanyEmployeeResponse.EmployeeInfo ei = (CompanyEmployeeResponse.EmployeeInfo) itemModel;
            baseViewHolder.setText(R.id.enterprise_employee_name,ei.getPositionName());
            baseViewHolder.setText(R.id.enterprise_employee_salary,ei.getSalary()+"元/小时");
            baseViewHolder.setText(R.id.enterprise_employee_commission,ei.getCommision()+"元/小时");
            baseViewHolder.setText(R.id.enterprise_employee_apply_num,ei.getApplyNum()+"人");
            baseViewHolder.setText(R.id.enterprise_employee_agree_num,ei.getAgreeNum()+"人");
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent();
                    detail.setClass(EnterpriseEmployeeActivity.this, EnterpriseEmployeeDetailActivity.class);
                    detail.putExtra("positionId",ei.getPositionId());
                    detail.putExtra("positionName",ei.getPositionName());
                    detail.putExtra("enrollNum",ei.getEnrollNum());
                    detail.putExtra("hiringCount",ei.getHiringCount());
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
