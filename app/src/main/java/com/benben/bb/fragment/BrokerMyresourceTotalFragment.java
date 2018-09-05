package com.benben.bb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.activity.UserInfoActivity;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.base.BaseFragment;
import com.benben.bb.bean.UserData;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.MyInviteResourceResponse;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class BrokerMyresourceTotalFragment extends BaseFragment {
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

    public BrokerMyresourceTotalFragment() {
        LogUtil.d("BrokerMyresourceTotalFragment non-parameter constructor");
    }

    public static BrokerMyresourceTotalFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        BrokerMyresourceTotalFragment fragment = new BrokerMyresourceTotalFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int provieResourceID() {
        return R.layout.list_recyclerview;
    }

    @Override
    protected void init() {
        recyclerSwipeLayout.createAdapter(R.layout.list_broker_myres_total_item);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
    }

    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final MyInviteResourceResponse.MyInviteResource mir = (MyInviteResourceResponse.MyInviteResource) itemModel;
            if (!TextUtils.isEmpty(mir.getAvatar())) {
                Glide.with(getActivity())
                        .load(mir.getAvatar())
                        .placeholder(R.mipmap.default_image)
                        .error(R.mipmap.default_image)
                        .into((ImageView) baseViewHolder.getView(R.id.item_img));
            }
            if (TextUtils.isEmpty(mir.getTrueName())) {
                baseViewHolder.setText(R.id.item_name, "未实名");
            } else {
                baseViewHolder.setText(R.id.item_name, mir.getTrueName());
            }
            baseViewHolder.setText(R.id.item_year, mir.getAge() + "");
            baseViewHolder.setText(R.id.item_edu, mir.getEducation());
            if (mir.getSex() == 1) {
                baseViewHolder.setText(R.id.item_sex, "女");
            } else {
                baseViewHolder.setText(R.id.item_sex, "男");
            }
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent person = new Intent();
                    person.setClass(getActivity(), UserInfoActivity.class);
                    person.putExtra("userId", mir.getUserId() + "");
                    startActivity(person);
                }
            });
        }
    };

    BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener = new BaseQuickAdapter.RequestLoadMoreListener() {

        @Override
        public void onLoadMoreRequested() {
            if (pageNo < totalPage) {
                pageNo++;
                requestResource();
            } else {
                recyclerSwipeLayout.loadComplete();
            }//这段代码是用来请求下一页数据的
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestResource();
    }

    private void requestResource() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        OkHttpUtils.getAsyn(NetWorkConfig.GET_MY_INVITE_RESOURCE, params, MyInviteResourceResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    MyInviteResourceResponse mar = (MyInviteResourceResponse) resultDesc;
                    if (mar.getCode() == 1) {
                        pageNo = mar.getData().getPageNum();
                        totalPage = mar.getData().getPages();
                        if (totalPage == 0) {
                            recyclerSwipeLayout.setEmpty();
                            return;
                        }
                        recyclerSwipeLayout.openLoadMore(totalPage);
                        recyclerSwipeLayout.addData(mar.getData().getList());
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