package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.dialog.WheelBottomDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.MySalaryHistoryResponse;
import com.benben.bb.utils.ToastUtil;
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
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-18.
 * email:shoxgov@126.com
 */

public class MySalaryListActivity extends BaseActivity {

    private static final String[] DATE_ARRAYS = new String[]{"2019", "2018", "2017"};
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    @Bind(R.id.salary_history_date)
    TextView salaryDate;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;

    private String oldDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysalary);
        ButterKnife.bind(this);
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
        recyclerSwipeLayout.createAdapter(R.layout.list_salary_item);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        salaryDate.setText(DATE_ARRAYS[1]);
        requestHistory(salaryDate.getText().toString());
    }

    private void requestHistory(String date) {
        oldDate = date;
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("date", date);
        OkHttpUtils.getAsyn(NetWorkConfig.USER_SALARY_LIST, params, MySalaryHistoryResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    MySalaryHistoryResponse shr = (MySalaryHistoryResponse) resultDesc;
                    if (shr.getCode() == 1) {
                        pageNo = shr.getData().getPageNum();
                        totalPage = shr.getData().getPages();
                        List<MySalaryHistoryResponse.SalaryInfo> temp = shr.getData().getList();
                        if (temp == null || temp.isEmpty()) {
                            if (totalPage == 0) {
                                recyclerSwipeLayout.setEmpty();
                            }
                            return;
                        }
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
                requestHistory(salaryDate.getText().toString());
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };


    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final MySalaryHistoryResponse.SalaryInfo si = (MySalaryHistoryResponse.SalaryInfo) itemModel;
            baseViewHolder.setText(R.id.salary_history_item_content, si.getWagesTitle());
            baseViewHolder.setText(R.id.salary_history_item_date, si.getCreateDate());
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent();
                    detail.setClass(MySalaryListActivity.this, MySalaryListDetailActivity.class);
                    detail.putExtra("id", si.getId());
                    detail.putExtra("salary", si.getSalary());
                    detail.putExtra("factMoney", si.getFactMoney());
                    detail.putExtra("workHours", si.getWorkHours());
                    detail.putExtra("buckleMoney", si.getBuckleMoney());
                    detail.putExtra("rewardMoney", si.getRewardMoney());
                    detail.putExtra("buckleDetails", si.getBuckleDetails());
                    detail.putExtra("wagesTitle", si.getWagesTitle());
                    detail.putExtra("complain", si.getComplain());
                    startActivityForResult(detail, 12);
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == RESULT_OK) {
            pageNo = 1;
            totalPage = -1;
            recyclerSwipeLayout.clear();
            requestHistory(oldDate);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.salary_history_date)
    public void onViewClicked() {
        WheelBottomDialog sexDla = new WheelBottomDialog(this, DATE_ARRAYS, new DialogCallBack() {
            @Override
            public void OkDown(Object score) {
                int position = (int) score;
                String temp = salaryDate.getText().toString();
                if (temp.equals(DATE_ARRAYS[position])) {
                    return;
                }
                salaryDate.setText(DATE_ARRAYS[position]);
                recyclerSwipeLayout.clear();
                requestHistory(salaryDate.getText().toString());
            }

            @Override
            public void CancleDown() {

            }
        });
        sexDla.show();
    }
}
