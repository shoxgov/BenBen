package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.base.BaseFragment;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyRecruitResponse;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.SwitchButton;
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

public class EnterpriseRecruitFragment extends BaseFragment {
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
    private String positionType = "0";

    public EnterpriseRecruitFragment() {
        LogUtil.d("EnterpriseRecruitFragment non-parameter constructor");
    }

    public static EnterpriseRecruitFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        EnterpriseRecruitFragment fragment = new EnterpriseRecruitFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int provieResourceID() {
        return R.layout.list_recyclerview;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        positionType = getArguments().getString(ARG);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void freshUp() {
        pageNo = 1;
        totalPage = -1;
        recyclerSwipeLayout.setNewData(new ArrayList<CompanyRecruitResponse.RecruitInfo>());
        requestEnterpriseEmploy();
    }

    @Override
    protected void init() {
        recyclerSwipeLayout.createAdapter(R.layout.list_enterprise_recruit);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        requestEnterpriseEmploy();
    }

    private void requestEnterpriseEmploy() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("positionType", positionType);
        OkHttpUtils.getAsyn(NetWorkConfig.COMPANY_RECRUIT_LIST, params, CompanyRecruitResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    CompanyRecruitResponse mar = (CompanyRecruitResponse) resultDesc;
                    if (mar.getCode() == 1) {
                        pageNo = mar.getData().getPageNum();
                        totalPage = mar.getData().getPages();
                        if (totalPage == 0) {
                            recyclerSwipeLayout.setEmpty();
                            return;
                        }
                        List<CompanyRecruitResponse.RecruitInfo> temp = mar.getData().getList();
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
                requestEnterpriseEmploy();
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };


    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final CompanyRecruitResponse.RecruitInfo ri = (CompanyRecruitResponse.RecruitInfo) itemModel;
            baseViewHolder.setText(R.id.enterprise_recruit_name, ri.getPositionName());
            baseViewHolder.setText(R.id.enterprise_recruit_salary, ri.getSalary() + "元/小时");
            baseViewHolder.setText(R.id.enterprise_recruit_commission, ri.getCommision() + "元/小时");
//            baseViewHolder.setText(R.id.enterprise_recruit_num, "招聘" + ri.getEnrollNum() + "人");
            baseViewHolder.setText(R.id.enterprise_recruit_count, ri.getHiringCount() + "人");
            if (ri.getEndTime().contains(" ")) {
                baseViewHolder.setText(R.id.enterprise_recruit_date, ri.getEndTime().split(" ")[0]);
            } else {
                baseViewHolder.setText(R.id.enterprise_recruit_date, ri.getEndTime());
            }
            SwitchButton sbBtn = baseViewHolder.getView(R.id.recruit_sb);
            switch (ri.getPositionStatus()) {
                case 0://开放
                    sbBtn.setChecked(true);
                    break;
                case 1://关闭
                    sbBtn.setChecked(false);
                    break;
            }
            sbBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton v, boolean isChecked) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", ri.getId() + "");
                    params.put("positionStatus", v.isChecked() ? 0 + "" : 1 + "");
                    OkHttpUtils.postAsynFiles(NetWorkConfig.COMPANY_RECRUIT_ADD, "", null, params, BaseResponse.class, new HttpCallback() {
                        @Override
                        public void onSuccess(BaseResponse baseResponse) {
                            super.onSuccess(baseResponse);
                            if (baseResponse.getCode() == 1) {
                                ToastUtil.showText("修改成功");
                            } else {
                                ToastUtil.showText(baseResponse.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(int code, String message) {
                            super.onFailure(code, message);
                            ToastUtil.showText(message);
                        }
                    });
                }
            });
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), EnterpriseAddRecruitActivity.class);
                    intent.putExtra("positionId", ri.getId());
                    startActivityForResult(intent, 11);
                }
            });
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
