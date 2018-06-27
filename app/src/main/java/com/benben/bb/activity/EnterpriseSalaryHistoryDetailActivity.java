package com.benben.bb.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyEmployeeEntryResponse;
import com.benben.bb.okhttp3.response.EnterpriseSalaryHistoryDetailResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.TitleBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-18.
 * email:shoxgov@126.com
 */

public class EnterpriseSalaryHistoryDetailActivity extends BaseActivity {
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    @Bind(R.id.salary_history_detail_positionname)
    TextView positionnameTv;
    @Bind(R.id.salary_history_detail_base)
    TextView baseTv;
    @Bind(R.id.salary_history_detail_total)
    TextView totalTv;

    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    private String wagesTitle;
    private int wagesId;
    private float wagesSalary, wagesCommision, wagesSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_salary_history_detail);
        ButterKnife.bind(this);
        wagesId = getIntent().getIntExtra("id", 0);
        wagesTitle = getIntent().getStringExtra("wagesTitle");
        wagesSalary = getIntent().getFloatExtra("salary", 0);
        wagesCommision = getIntent().getFloatExtra("commision", 0);
        wagesSum = getIntent().getFloatExtra("wagesSum", 0);
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
        positionnameTv.setText(wagesTitle);
        baseTv.setText("基本薪资：" + wagesSalary + "元/小时" + "    " + "经纪人佣金：" + wagesCommision + "元/小时");
        totalTv.setText(Html.fromHtml("合计：<font color=#FD7979>" + wagesSum + "</font>"));
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        requestHistory();
    }

    BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener = new BaseQuickAdapter.RequestLoadMoreListener() {

        @Override
        public void onLoadMoreRequested() {
            if (pageNo < totalPage) {
                pageNo++;
                requestHistory();
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };


    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            EnterpriseSalaryHistoryDetailResponse.SalaryHistoryInfo shi = (EnterpriseSalaryHistoryDetailResponse.SalaryHistoryInfo) itemModel;
            baseViewHolder.setText(R.id.salary_history_detail_username, shi.getTrueName() + "    " + shi.getUserName());
            baseViewHolder.setText(R.id.salary_history_detail_factgive, "实发：" + shi.getFactMoney() + "元");
            baseViewHolder.setText(R.id.salary_history_detail_workhour, shi.getWorkHours() + "");
            baseViewHolder.setText(R.id.salary_history_detail_deduck, shi.getBuckleMoney() + "");
            baseViewHolder.setText(R.id.salary_history_detail_award, shi.getRewardMoney() + "");
            if (TextUtils.isEmpty(shi.getBuckleDetails())) {
                baseViewHolder.setText(R.id.salary_history_detail_soso, "无");
            } else {
                baseViewHolder.setText(R.id.salary_history_detail_soso, shi.getBuckleDetails());
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void requestHistory() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("wagesId", wagesId + "");
        OkHttpUtils.getAsyn(NetWorkConfig.COMPANY_SALARY_HISTORY_DETAIL, params, EnterpriseSalaryHistoryDetailResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                EnterpriseSalaryHistoryDetailResponse er = (EnterpriseSalaryHistoryDetailResponse) br;
                if (er.getCode() == 1) {
                    pageNo = er.getData().getPageNum();
                    totalPage = er.getData().getPages();
                    if (totalPage == 0) {
                        recyclerSwipeLayout.setEmpty();
                        return;
                    }
                    recyclerSwipeLayout.openLoadMore(totalPage);
                    recyclerSwipeLayout.addData(er.getData().getList());
                } else {
                    ToastUtil.showText(er.getMessage());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText(message);
            }
        });
    }
}
