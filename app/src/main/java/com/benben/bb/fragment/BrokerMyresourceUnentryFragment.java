package com.benben.bb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.GridView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.BrokerGridViewAdapter;
import com.benben.bb.base.BaseFragment;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.MyAgentsResponse;
import com.benben.bb.okhttp3.response.MyResourceResponse;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.AutoLoadListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class BrokerMyresourceUnentryFragment extends BaseFragment {
    private static final String ARG = "arg";
    @Bind(R.id.gridview)
    GridView gridview;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    private BrokerGridViewAdapter adapter;

    public BrokerMyresourceUnentryFragment() {
        LogUtil.d("BrokerMyresourceUnentryFragment non-parameter constructor");
    }

    public static BrokerMyresourceUnentryFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        BrokerMyresourceUnentryFragment fragment = new BrokerMyresourceUnentryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int provieResourceID() {
        return R.layout.gridview;
    }

    @Override
    protected void init() {
        adapter = new BrokerGridViewAdapter(getActivity(), false);
        gridview.setAdapter(adapter);
        AutoLoadListener autoLoadListener = new AutoLoadListener(callBack);
        gridview.setOnScrollListener(autoLoadListener);
    }

    AutoLoadListener.AutoLoadCallBack callBack = new AutoLoadListener.AutoLoadCallBack() {

        public void execute() {
            if (pageNo < totalPage) {
                pageNo++;
                requestResource();
            } else {

            }//这段代码是用来请求下一页数据的
        }

    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initdata();
    }

    private void initdata() {
        requestResource();
    }

    private void requestResource() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        OkHttpUtils.getAsyn(NetWorkConfig.BROKER_RESOURCE_ISENTRY, params, MyResourceResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    MyResourceResponse mar = (MyResourceResponse) resultDesc;
                    if (mar.getCode() == 1) {
                        pageNo = mar.getData().getPageNum();
                        totalPage = mar.getData().getPages();
                        if (totalPage == 0) {
                            ToastUtil.showText("暂无数据");
                            return;
                        }
                        adapter.setData(mar.getData().getList());
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