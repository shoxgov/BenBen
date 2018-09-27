package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.dialog.GoCertifyDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CooperateEnterpriseResponse;
import com.benben.bb.okhttp3.response.MyCompanyResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.SwitchButton;
import com.benben.bb.view.TitleBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-14.
 * email:shoxgov@126.com
 */

public class EnterpriseCooperationActivity extends BaseActivity {
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    @Bind(R.id.enterprise_cooperate_self_name)
    TextView enterpriseName;
    @Bind(R.id.enterprise_cooperate_self_addr)
    TextView enterpriseAddr;
    @Bind(R.id.enterprise_cooperate_self_tag)
    ImageView enterpriseTag;
    private String companyRegion;
    /**
     * 保存选择的
     */
    private int companyId;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_cooperation);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
        recyclerSwipeLayout.createAdapter(R.layout.list_enterprise_cooperation);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        getCompanyInfo();
        requestCooperationEnterprise();
    }

    BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener = new BaseQuickAdapter.RequestLoadMoreListener() {

        @Override
        public void onLoadMoreRequested() {
            if (pageNo < totalPage) {
                pageNo++;
                requestCooperationEnterprise();
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };

    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final CooperateEnterpriseResponse.CooperateEnterprise ce = (CooperateEnterpriseResponse.CooperateEnterprise) itemModel;
            baseViewHolder.setText(R.id.enterprise_cooperate_name, ce.getCompanyName());
            final SwitchButton sbBtn = baseViewHolder.getView(R.id.enterprise_cooperate_open);
            switch (ce.getStatus()) {
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
                    if (v.getTag() != null && (boolean) v.getTag()) {
                        sbBtn.setTag(null);
                        return;
                    }
                    if (isChecked) {
                        String info = "如果打开合作，该企业下的你发布的招聘信息将全部开放";
                        GoCertifyDialog warnDialog = new GoCertifyDialog(EnterpriseCooperationActivity.this, "确认开放合作", info, "取消", true, "确认", true, new DialogCallBack() {
                            @Override
                            public void OkDown(Object score) {
                                /*id ID
                                status 0开1关
                                companyId 企业ID*/
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("id", ce.getId() + "");
                                params.put("status", "0");
                                params.put("companyId", ce.getCompanyId() + "");
                                OkHttpUtils.getAsyn(NetWorkConfig.COOPERATE_ENTERPRISE_OPEN, params, BaseResponse.class, new HttpCallback() {
                                    @Override
                                    public void onSuccess(BaseResponse baseResponse) {
                                        super.onSuccess(baseResponse);
                                        if (baseResponse.getCode() == 1) {
                                            ToastUtil.showText("修改成功");
                                        } else {
                                            ToastUtil.showText(baseResponse.getMessage());
                                            sbBtn.setTag(true);
                                            sbBtn.setChecked(false);
                                        }
                                    }

                                    @Override
                                    public void onFailure(int code, String message) {
                                        super.onFailure(code, message);
                                        ToastUtil.showText(message);
                                        sbBtn.setTag(true);
                                        sbBtn.setChecked(false);
                                    }
                                });
                            }

                            @Override
                            public void CancleDown() {
                                sbBtn.setTag(true);
                                sbBtn.setChecked(false);
                            }
                        });
                        warnDialog.show();
                    } else {
                        String info = "如果关闭合作，该企业下的你发布的招聘信息将全部停止";
                        GoCertifyDialog warnDialog = new GoCertifyDialog(EnterpriseCooperationActivity.this, "确认关闭合作", info, "取消", true, "确认", true, new DialogCallBack() {
                            @Override
                            public void OkDown(Object score) {
                                /*id ID
                                status 0开1关
                                companyId 企业ID*/
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("id", ce.getId() + "");
                                params.put("status", "1");
                                params.put("companyId", ce.getCompanyId() + "");
                                OkHttpUtils.getAsyn(NetWorkConfig.COOPERATE_ENTERPRISE_OPEN, params, BaseResponse.class, new HttpCallback() {
                                    @Override
                                    public void onSuccess(BaseResponse baseResponse) {
                                        super.onSuccess(baseResponse);
                                        if (baseResponse.getCode() == 1) {
                                            ToastUtil.showText("修改成功");
                                        } else {
                                            ToastUtil.showText(baseResponse.getMessage());
                                            sbBtn.setTag(true);
                                            sbBtn.setChecked(true);
                                        }
                                    }

                                    @Override
                                    public void onFailure(int code, String message) {
                                        super.onFailure(code, message);
                                        ToastUtil.showText(message);
                                        sbBtn.setTag(true);
                                        sbBtn.setChecked(true);
                                    }
                                });
                            }

                            @Override
                            public void CancleDown() {
                                sbBtn.setTag(true);
                                sbBtn.setChecked(true);
                            }
                        });
                        warnDialog.show();

                    }

                }
            });

            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent edit = new Intent();
                    edit.setClass(EnterpriseCooperationActivity.this, EnterpriseInfoEditActivity.class);
                    edit.putExtra("companyId", ce.getCompanyId());
                    edit.putExtra("edit", false);
                    startActivity(edit);
                }
            });
        }
    };


    private void requestCooperationEnterprise() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        OkHttpUtils.getAsyn(NetWorkConfig.GET_COOPERATE_ENTERPRISE, CooperateEnterpriseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                CooperateEnterpriseResponse cr = (CooperateEnterpriseResponse) br;
                if (cr.getCode() == 1) {
                    pageNo = cr.getData().getPageNum();
                    totalPage = cr.getData().getPages();
                    if (totalPage == 0) {
                        recyclerSwipeLayout.setEmpty();
                        return;
                    }
                    recyclerSwipeLayout.openLoadMore(totalPage);
                    recyclerSwipeLayout.addData(cr.getData().getList());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    private void getCompanyInfo() {
        OkHttpUtils.getAsyn(NetWorkConfig.GET_MY_COMPANY, MyCompanyResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                super.onSuccess(baseResponse);
                MyCompanyResponse cir = (MyCompanyResponse) baseResponse;
                if (cir.getCode() == 1) {
                    try {
                        companyId = cir.getData().getCompanyId();
                        companyRegion = cir.getData().getCompanyRegion();
                        enterpriseName.setText(cir.getData().getCompanyName());
                        if (cir.getData().getAuditStatus() == 1) {
                            enterpriseTag.setVisibility(View.VISIBLE);
                        } else {
                            enterpriseTag.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(companyRegion)) {
                            enterpriseAddr.setText(companyRegion);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    @OnClick({R.id.enterprise_cooperate_edit, R.id.enterprise_cooperate_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.enterprise_cooperate_edit:
                Intent edit = new Intent();
                edit.setClass(this, EnterpriseInfoEditActivity.class);
                edit.putExtra("companyId", companyId);
                edit.putExtra("edit", true);
                startActivityForResult(edit, 11);
                break;
            case R.id.enterprise_cooperate_add:
                Intent add = new Intent();
                add.setClass(this, EnterpriseCooperationSearchActivity.class);
                startActivityForResult(add, 11);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            recyclerSwipeLayout.clear();
            pageNo = 1;
            totalPage = -1;
            requestCooperationEnterprise();
        }
    }
}
