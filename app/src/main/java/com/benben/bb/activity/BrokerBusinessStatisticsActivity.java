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
import com.benben.bb.okhttp3.response.BrokerEnrollSignupPositionResponse;
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
 * Created by wangshengyin on 2018-05-25.
 * email:shoxgov@126.com
 */

public class BrokerBusinessStatisticsActivity extends BaseActivity {
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
        titleLayout.setTitleName("业务统计");
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
        recyclerSwipeLayout.createAdapter(R.layout.list_broker_myres_statistics_item);
        recyclerSwipeLayout.setDividerColor(this, getResources().getColor(R.color.mainbg), Utils.dip2px(this, 10));
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
    }

    private void init() {
        requestPosition();
    }

    /**
     * 该接口按code的值来分，2表示的是业务统计下已入职的数据
     */
    private void requestPosition() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("code", "2");//状态：1已报名2已入职
        OkHttpUtils.getAsyn(NetWorkConfig.BROKER_GET_POSITION, params, BrokerEnrollSignupPositionResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    BrokerEnrollSignupPositionResponse bpr = (BrokerEnrollSignupPositionResponse) resultDesc;
                    if (bpr.getCode() == 1) {
                        pageNo = bpr.getData().getPageNum();
                        totalPage = bpr.getData().getPages();
                        if (totalPage == 0) {
                            recyclerSwipeLayout.setEmpty();
                            return;
                        }
                        recyclerSwipeLayout.addData(bpr.getData().getList());
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
                requestPosition();
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };


    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final BrokerEnrollSignupPositionResponse.SignupPositionInfo spi = (BrokerEnrollSignupPositionResponse.SignupPositionInfo) itemModel;
            baseViewHolder.setText(R.id.broker_statistics_positionname, spi.getPositionName());
            baseViewHolder.setText(R.id.broker_statistics_agreenum, "已输送：" + spi.getAgreeNum() + "人");
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent();
                    detail.setClass(BrokerBusinessStatisticsActivity.this, BrokerBusinessStatisticsDetailActivity.class);
                    detail.putExtra("positionId", spi.getPositionId());
                    detail.putExtra("salary", spi.getSalary());
                    detail.putExtra("commision", spi.getCommision());
                    detail.putExtra("positionName", spi.getPositionName());
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
