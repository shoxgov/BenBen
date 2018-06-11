package com.benben.bb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.activity.BrokerMyresSignupDetailActivity;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.base.BaseFragment;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.BrokerEnrollSignupPositionResponse;
import com.benben.bb.okhttp3.response.MyResourceResponse;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class BrokerMyresourceSignupingFragment extends BaseFragment {
    private static final String ARG = "arg";
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;

    public BrokerMyresourceSignupingFragment() {
        LogUtil.d("BrokerMyresourceSignupingFragment non-parameter constructor");
    }

    public static BrokerMyresourceSignupingFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        BrokerMyresourceSignupingFragment fragment = new BrokerMyresourceSignupingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int provieResourceID() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    protected void init() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerSwipeLayout.createAdapter(R.layout.list_broker_myres_signuping);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        initdata();
    }

    private void initdata() {
        requestPosition();
    }

    /**
     * 该接口按code的值来分，1表示的是我的资源下的已报名的数据
     */
    private void requestPosition() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("code", "1");//状态：1已报名2已入职
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
            Glide.with(getActivity())
                    .load(spi.getAvatar())
                    .placeholder(R.mipmap.default_image)
                    .error(R.mipmap.default_image)
                    .into((ImageView) baseViewHolder.getView(R.id.broker_myres_signuping_photo));
            baseViewHolder.setText(R.id.broker_myres_signuping_name, spi.getPositionName());
            baseViewHolder.setText(R.id.broker_myres_signuping_region, "工作地点：" + spi.getRegion());
            baseViewHolder.setText(R.id.broker_myres_signuping_price, spi.getCommision() + "元/小时");
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent();
                    detail.setClass(getActivity(), BrokerMyresSignupDetailActivity.class);
                    detail.putExtra("positionId", spi.getPositionId());
                    startActivity(detail);
                }
            });
        }
    };
}