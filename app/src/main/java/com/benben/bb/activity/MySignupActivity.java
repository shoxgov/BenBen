package com.benben.bb.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.FirstInMySignupDialog;
import com.benben.bb.dialog.WarnDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.SignupResponse;
import com.benben.bb.utils.DateUtils;
import com.benben.bb.utils.PreferenceUtil;
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
 * Created by wangshengyin on 2018-05-16.
 * email:shoxgov@126.com
 */

public class MySignupActivity extends BaseActivity {
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
        mainlayout.setBackgroundResource(R.color.white);
        TitleBar titleLayout = (TitleBar) findViewById(R.id.titlelayout);
        titleLayout.setTitleName("我的报名");
        titleLayout.setTitleBarListener(new TitleBarListener() {

            @Override
            public void rightClick() {
            }

            @Override
            public void leftClick() {
                finish();
            }
        });
        recyclerSwipeLayout.createAdapter(R.layout.list_enterprise_mysignup);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
    }

    private void init() {
        PreferenceUtil.init(this);
        boolean isFirst = PreferenceUtil.getBoolean("IsFirstSignup", true);
        if (isFirst) {
            PreferenceUtil.commitBoolean("IsFirstSignup", false);
            FirstInMySignupDialog dialog = new FirstInMySignupDialog(this);
            dialog.show();
        }
        requestSignup();
    }

    private void requestSignup() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        OkHttpUtils.getAsyn(NetWorkConfig.USER_MY_SIGNUP, params, SignupResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    SignupResponse crr = (SignupResponse) resultDesc;
                    if (crr.getCode() == 1) {
                        pageNo = crr.getData().getPageNum();
                        totalPage = crr.getData().getPages();
                        List<SignupResponse.SignupInfo> temp = crr.getData().getList();
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
                requestSignup();
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };


    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final SignupResponse.SignupInfo si = (SignupResponse.SignupInfo) itemModel;
            baseViewHolder.setText(R.id.mysignup_positionname, si.getPositionName());
            baseViewHolder.setText(R.id.mysignup_job_ltd, si.getCompanyName());
            baseViewHolder.setText(R.id.mysignup_price, si.getFocusSalary());
            String region = si.getRegion();
            if (region.contains(".")) {
                String[] regions = region.split("\\.");
                if (regions.length == 3) {
                    baseViewHolder.setText(R.id.mysignup_addr, regions[1] + "." + regions[2]);
                } else {
                    baseViewHolder.setText(R.id.mysignup_addr, regions[0] + "." + regions[1]);
                }
            } else {
                baseViewHolder.setText(R.id.mysignup_addr, si.getRegion());
            }
            switch (si.getAdvSort()) {//advSort (1限时招聘2优质企业3犇犇推荐4可实习生5小时兼职)
                case 1:
                    baseViewHolder.setText(R.id.mysignup_job_tag, "限时招聘");
                    baseViewHolder.setBackgroundColor(R.id.mysignup_job_tag, Color.parseColor("#fd7979"));
                    break;
                case 2:
                    baseViewHolder.setText(R.id.mysignup_job_tag, "优质企业");
                    baseViewHolder.setBackgroundColor(R.id.mysignup_job_tag, Color.parseColor("#94db46"));
                    break;
                case 3:
                default:
                    baseViewHolder.setText(R.id.mysignup_job_tag, "犇犇推荐");
                    baseViewHolder.setBackgroundColor(R.id.mysignup_job_tag, R.color.bluetheme);
                    break;
                case 4:
                    baseViewHolder.setText(R.id.mysignup_job_tag, "可实习生");
                    baseViewHolder.setBackgroundColor(R.id.mysignup_job_tag, Color.parseColor("#ffa23c"));
                    break;
                case 5:
                    baseViewHolder.setText(R.id.mysignup_job_tag, "小时兼职");
                    baseViewHolder.setBackgroundColor(R.id.mysignup_job_tag, Color.parseColor("#50e3c2"));
                    break;
            }
            TextView submit = (TextView) baseViewHolder.getView(R.id.mysignup_submit);
            if (si.getEntryStatus() == 77) {
                submit.setText("同意应聘该职位");
                submit.setTextColor(getResources().getColor(R.color.white));
                submit.setBackgroundResource(R.drawable.drawable_buttonbg);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Utils.isFastDoubleClick()) {
                            return;
                        }
                        WarnDialog warnDialog = new WarnDialog(MySignupActivity.this, "确定同意应聘？", new DialogCallBack() {
                            @Override
                            public void OkDown(Object obj) {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("positionUserId", si.getId() + "");//职位ID
                                OkHttpUtils.getAsyn(NetWorkConfig.USER_AGREEN_SIGNUP_EVENT, params, BaseResponse.class, new HttpCallback() {
                                    @Override
                                    public void onSuccess(BaseResponse br) {
                                        super.onSuccess(br);
                                        if (br.getCode() == 1) {
                                            pageNo = 1;
                                            totalPage = -1;
                                            recyclerSwipeLayout.setNewData(new ArrayList<SignupResponse.SignupInfo>());
                                            requestSignup();
                                        } else {
                                            ToastUtil.showText(br.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(int code, String message) {
                                        super.onFailure(code, message);
                                        ToastUtil.showText(message);
                                    }
                                });
                            }

                            @Override
                            public void CancleDown() {

                            }
                        });
                        warnDialog.show();

                    }
                });
            } else {
                switch (si.getEntryStatus()) {
                    case 91:
                        submit.setText("辞退");
                        submit.setBackground(null);
                        submit.setTextColor(getResources().getColor(R.color.gray_dark));
                        break;
                    case 99:
                        submit.setText("已入职");
                        submit.setBackground(null);
                        submit.setTextColor(getResources().getColor(R.color.gray_dark));
                        break;
                    case 88:
                        submit.setText("等待系统审核");
                        submit.setBackground(null);
                        submit.setTextColor(Color.parseColor("#FFA23C"));
                        break;
                    case 89:
                        submit.setText("等待企业审核");
                        submit.setBackground(null);
                        submit.setTextColor(getResources().getColor(R.color.gray_dark));
                        break;
                    default:
                        submit.setText("申核中");
                        submit.setBackground(null);
                        submit.setTextColor(getResources().getColor(R.color.gray_dark));
                        break;
                }
                submit.setOnClickListener(null);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
