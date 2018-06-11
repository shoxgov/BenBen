package com.benben.bb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.activity.EnterpriseAddRecruitActivity;
import com.benben.bb.activity.EnterpriseEmployeeDetailActivity;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.base.BaseFragment;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.WarnDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyEmployeeEntryResponse;
import com.benben.bb.okhttp3.response.CompanyEmployeeResponse;
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

public class EnterpriseEmployeeUnentryFragment extends BaseFragment {
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

    public EnterpriseEmployeeUnentryFragment() {
        LogUtil.d("TestFragment non-parameter constructor");
    }

    public static EnterpriseEmployeeUnentryFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        EnterpriseEmployeeUnentryFragment fragment = new EnterpriseEmployeeUnentryFragment();
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
        recyclerSwipeLayout.createAdapter(R.layout.list_enterprise_employee_entry);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        initdata();
    }

    private void initdata() {
        requestEmployPosition();
    }

    private void requestEmployPosition() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("positionId", "" + ((EnterpriseEmployeeDetailActivity) getActivity()).getPositionId());//职位ID
        params.put("entryStatus", "0");//状态默认0,0未入职1已入职
        OkHttpUtils.getAsyn(NetWorkConfig.COMPANY_EMPLOYEE_DETAIL_LIST, params, CompanyEmployeeEntryResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    CompanyEmployeeEntryResponse mar = (CompanyEmployeeEntryResponse) resultDesc;
                    if (mar.getCode() == 1) {
                        pageNo = mar.getData().getPageNum();
                        totalPage = mar.getData().getPages();
                        if (totalPage == 0) {
                            recyclerSwipeLayout.setEmpty();
                            return;
                        }
                        List<CompanyEmployeeEntryResponse.EmployeeEntryInfo> temp = mar.getData().getList();
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
                requestEmployPosition();
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };


    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final CompanyEmployeeEntryResponse.EmployeeEntryInfo eei = (CompanyEmployeeEntryResponse.EmployeeEntryInfo) itemModel;
            Glide.with(getActivity())
                    .load(eei.getAvatar())
                    .error(R.mipmap.default_image)
                    .into((ImageView) baseViewHolder.getView(R.id.enterprise_employee_entry_photo));
            baseViewHolder.setText(R.id.enterprise_employee_entry_name, eei.getTrueName());
            baseViewHolder.setText(R.id.enterprise_employee_entry_hint, eei.getUserName());
            baseViewHolder.setOnClickListener(R.id.employ_entry_btn1, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WarnDialog warnDialog = new WarnDialog(getActivity(), "取消报名？", new DialogCallBack() {
                        @Override
                        public void OkDown(Object obj) {
                            userStatusOp(eei.getPositionUserId(),eei.getUserId(), 2);
                        }


                        @Override
                        public void CancleDown() {

                        }
                    });
                    warnDialog.show();
                }
            });
            baseViewHolder.setOnClickListener(R.id.employ_entry_btn2, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WarnDialog warnDialog = new WarnDialog(getActivity(), "同意入职？", new DialogCallBack() {
                        @Override
                        public void OkDown(Object obj) {
                            userStatusOp(eei.getPositionUserId(), eei.getUserId(),99);
                        }


                        @Override
                        public void CancleDown() {

                        }
                    });
                    warnDialog.show();
                }
            });
        }
    };

    private void userStatusOp(int id, int userId, int entryStatus) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id + "");
        params.put("userId", userId + "");
        params.put("entryStatus", entryStatus + "");//entryStatus 0 取消报名-用户;1 取消报名-系统;2 取消报名-企业;3 取消报名-经纪人;77 报名中-经纪人代报名;88 报名中-待系统审核;89 报名中-待企业审核;90 待入职;99 已入职;91辞退
        OkHttpUtils.getAsyn(NetWorkConfig.COMPANY_EMPLOYEE_USER_STATUS, params, BaseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                ToastUtil.showText(br.getMessage());
                if (br.getCode() == 1) {
                    pageNo = 1;
                    totalPage = -1;
                    recyclerSwipeLayout.setNewData(new ArrayList<CompanyEmployeeEntryResponse.EmployeeEntryInfo>());
                    requestEmployPosition();
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