package com.benben.bb.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.WarnDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.SignupResponse;
import com.benben.bb.utils.DateUtils;
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
            baseViewHolder.setText(R.id.mysignup_price, si.getSalary() + "元/小时");
            baseViewHolder.setText(R.id.mysignup_welfare, si.getWelfare().replace(",", "|"));
            baseViewHolder.setText(R.id.mysignup_addr, "工作地点：" + si.getRegion());
            baseViewHolder.setText(R.id.mysignup_count, Html.fromHtml("报名：<font color=#FD7979>" + si.getEnrollNum() + "/" + si.getHiringCount() + "</font>(还剩<font color=#FD7979>" + DateUtils.dateDiffToday(si.getEndTime()) + "</font>天)"));
            TextView submit = (TextView) baseViewHolder.getView(R.id.mysignup_submit);
            if (si.getEntryStatus() == 77) {
                submit.setText("同意应聘该职位");
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
                                params.put("positionUserId", si.getPositionUserId() + "");//职位ID
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
                        break;
                    case 99:
                        submit.setText("已入职");
                        break;
                    default:
                        submit.setText("申核中");
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
