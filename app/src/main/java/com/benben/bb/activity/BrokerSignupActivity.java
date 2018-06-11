package com.benben.bb.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.BrokerGridViewAdapter;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.SignupSuccessDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.MyResourceResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.AutoLoadListener;
import com.benben.bb.view.TitleBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class BrokerSignupActivity extends BaseActivity {
    @Bind(R.id.gridview)
    GridView gridview;
    @Bind(R.id.broker_signup_hint)
    TextView hintTv;
    @Bind(R.id.broker_signup_ok)
    Button okBtn;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    private BrokerGridViewAdapter adapter;
    private int positionId;
    private String positionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_signup);
        ButterKnife.bind(this);
        positionId = getIntent().getIntExtra("positionId", 0);
        positionName = getIntent().getStringExtra("positionName");
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
        adapter = new BrokerGridViewAdapter(this, true);
        gridview.setAdapter(adapter);
        AutoLoadListener autoLoadListener = new AutoLoadListener(callBack);
        gridview.setOnScrollListener(autoLoadListener);
        requestResource();
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

    private void requestResource() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("userStatus", "0");//默认0未入职1已入职
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
                            hintTv.setVisibility(View.VISIBLE);
                            gridview.setVisibility(View.GONE);
                            okBtn.setText("邀请");
                            okBtn.setTag("YAO");
                            return;
                        }
                        okBtn.setTag("BAO");
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

    @OnClick(R.id.broker_signup_ok)
    public void onViewClicked() {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        if (okBtn.getTag() == null) {
            return;
        }
        if (okBtn.getTag().equals("YAO")) {
            Intent qr = new Intent();
            qr.setClass(BrokerSignupActivity.this, PersonQrActivity.class);
            startActivity(qr);
            finish();
            return;
        }
        String userIds = "";
        String selectedTag = adapter.getSelectedTag();
        List<MyResourceResponse.EntryPositionInfo> tempData = adapter.getData();
        for (MyResourceResponse.EntryPositionInfo epi : tempData) {
            if (selectedTag.contains("#" + epi.getUserId() + "@")) {
                if (TextUtils.isEmpty(userIds)) {
                    userIds = epi.getUserId() + "";
                } else {
                    userIds += "," + epi.getUserId();
                }
            }
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("positionId", positionId + "");//职位ID
        params.put("positionName", positionName);//职位
        params.put("code", "1");//1经纪人报名2自主报名
        params.put("userIds", userIds);//用户ID逗号分隔
        OkHttpUtils.getAsyn(NetWorkConfig.USER_SIGNUP_EVENT, params, BaseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                try {
                    if (br.getCode() == 1) {
                        SignupSuccessDialog signupSucDialog = new SignupSuccessDialog(BrokerSignupActivity.this, new DialogCallBack() {
                            @Override
                            public void OkDown(Object score) {

                            }

                            @Override
                            public void CancleDown() {

                            }
                        });
                        signupSucDialog.show();
                    } else {
                        ToastUtil.showText(br.getMessage());
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