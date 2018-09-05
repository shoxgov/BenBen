package com.benben.bb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.BrokerSingupingExpandableListAdapter;
import com.benben.bb.base.BaseFragment;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.BrokerEnrollSignupPositionResponse;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.AutoLoadListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class BrokerMyresourceWokingFragment extends BaseFragment {
    private static final String ARG = "arg";
    @Bind(R.id.searchbar_nodata)
    LinearLayout nodataLayout;
    @Bind(R.id.expand_list)
    ExpandableListView expandableList;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int requestPageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    private BrokerSingupingExpandableListAdapter adapter;

    public BrokerMyresourceWokingFragment() {
        LogUtil.d("BrokerMyresourceWokingFragment non-parameter constructor");
    }

    public static BrokerMyresourceWokingFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        BrokerMyresourceWokingFragment fragment = new BrokerMyresourceWokingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int provieResourceID() {
        return R.layout.fragment_broker_myres_signuping;
    }

    @Override
    protected void init() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new BrokerSingupingExpandableListAdapter(getActivity());
        expandableList.setAdapter(adapter);
        expandableList.setGroupIndicator(null);
        expandableList.setOnScrollListener(new AutoLoadListener(new AutoLoadListener.AutoLoadCallBack() {
            @Override
            public void execute() {
                if (requestPageNo == pageNo && pageNo < totalPage) {
                    pageNo++;
                    requestEntry();
                } else {
                }
            }
        }));
//        expandableList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                switch (scrollState) {
//                    // 当不滚动时
//                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                        // 判断滚动到底部
//                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                            if (requestPageNo == pageNo && pageNo < totalPage) {
//                                pageNo++;
//                                requestEntry();
//                            } else {
//                            }
//                        }
//                        break;
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//            }
//        });
        requestEntry();
    }

    private void requestEntry() {
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
                        requestPageNo = pageNo;
                        totalPage = bpr.getData().getPages();
                        if (totalPage == 0) {
                            nodataLayout.setVisibility(View.VISIBLE);
                            expandableList.setVisibility(View.GONE);
                            return;
                        }
                        adapter.setData(bpr.getData().getList());
                        for (int i = 0; i < adapter.getGroupCount(); i++) {
                            expandableList.expandGroup(i);
                        }
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

}