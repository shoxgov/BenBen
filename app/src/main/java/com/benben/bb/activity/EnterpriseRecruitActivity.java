package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyRecruitResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.SwitchButton;
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

public class EnterpriseRecruitActivity extends BaseActivity {
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
        setContentView(R.layout.activity_enterprise_recruit);
        ButterKnife.bind(this);
        initViews();
        init();
    }

    private void initViews() {
        TitleBar titleLayout = (TitleBar) findViewById(R.id.titlelayout);
        titleLayout.setTitleBarListener(new TitleBarListener() {

            @Override
            public void rightClick() {
                Intent add = new Intent();
                add.setClass(EnterpriseRecruitActivity.this, EnterpriseAddRecruitActivity.class);
                startActivityForResult(add, 11);
            }

            @Override
            public void leftClick() {
                finish();
            }
        });
        recyclerSwipeLayout.createAdapter(R.layout.list_enterprise_recruit);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
    }

    private void init() {
        requestEnterpriseEmploy();
    }

    private void requestEnterpriseEmploy() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
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
            baseViewHolder.setText(R.id.enterprise_recruit_num, "招聘" + ri.getEnrollNum() + "人");
            baseViewHolder.setText(R.id.enterprise_recruit_count, ri.getHiringCount() + "元/小时");
            baseViewHolder.setText(R.id.enterprise_recruit_date, ri.getEndTime());
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
                    intent.setClass(EnterpriseRecruitActivity.this, EnterpriseAddRecruitActivity.class);
                    intent.putExtra("positionId", ri.getId());
                    startActivity(intent);
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            pageNo = 1;
            totalPage = -1;
            recyclerSwipeLayout.setNewData(new ArrayList<CompanyRecruitResponse.RecruitInfo>());
            requestEnterpriseEmploy();
        }
    }
}
