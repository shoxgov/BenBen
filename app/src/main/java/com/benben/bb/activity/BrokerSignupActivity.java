package com.benben.bb.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.dialog.SignupSuccessDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.MyResourceResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.TitleBar;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class BrokerSignupActivity extends BaseActivity {
    @Bind(R.id.broker_signup_hint)
    TextView hintTv;
    @Bind(R.id.broker_signup_ok)
    Button okBtn;
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    private int positionId;
    private String positionName;
    /**
     * hiringCount-招聘人数
     * enrollNum - 已招人数
     */
    private int hiringCount, enrollNum;

    /**
     * 报名列表选择欲报名的人
     */
    private String selectedTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_signup);
        ButterKnife.bind(this);
        positionId = getIntent().getIntExtra("positionId", 0);
        hiringCount = getIntent().getIntExtra("hiringCount", 0);
        enrollNum = getIntent().getIntExtra("enrollNum", 0);
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
                Intent qr = new Intent();
                qr.setClass(BrokerSignupActivity.this, PersonQrActivity.class);
                startActivity(qr);
            }
        });
        View head = LayoutInflater.from(this).inflate(R.layout.list_broker_want_signup_head, null, false);
        TextView positionTv = (TextView) head.findViewById(R.id.item_head_name);
        positionTv.setText(positionName);
        TextView countTv = (TextView) head.findViewById(R.id.item_head_count);
        countTv.setText("可报名：" + enrollNum + "/" + hiringCount);
        head.findViewById(R.id.item_head_name);
        recyclerSwipeLayout.createAdapter(R.layout.list_broker_want_signup, false);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        recyclerSwipeLayout.addHeaderView(head);
        requestResource();
    }

    BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener = new BaseQuickAdapter.RequestLoadMoreListener() {

        @Override
        public void onLoadMoreRequested() {
            if (pageNo < totalPage) {
                pageNo++;
                requestResource();
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };
    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final MyResourceResponse.EntryPositionInfo epi = (MyResourceResponse.EntryPositionInfo) itemModel;
            if (!TextUtils.isEmpty(epi.getAvatar())) {
                Glide.with(BrokerSignupActivity.this)
                        .load(epi.getAvatar())
                        .placeholder(R.mipmap.default_image)
                        .error(R.mipmap.default_image)
                        .into((ImageView) baseViewHolder.getView(R.id.item_img));
            }
            if (TextUtils.isEmpty(epi.getTrueName())) {
                baseViewHolder.setText(R.id.item_name, "未实名");
            } else {
                baseViewHolder.setText(R.id.item_name, epi.getTrueName());
            }
            baseViewHolder.setText(R.id.item_year, epi.getAge() + "");
            baseViewHolder.setText(R.id.item_edu, epi.getEducation());
            if (epi.getSex() == 1) {
                baseViewHolder.setText(R.id.item_sex, "女");
            } else {
                baseViewHolder.setText(R.id.item_sex, "男");
            }
            CheckBox sbBtn = baseViewHolder.getView(R.id.item_cb);
            if (selectedTag.contains("#" + epi.getUserId() + "@")) {
                sbBtn.setChecked(true);
            } else {
                sbBtn.setChecked(false);
            }
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedTag.contains("#" + epi.getUserId() + "@")) {
                        selectedTag = selectedTag.replace("#" + epi.getUserId() + "@", "");
                        recyclerSwipeLayout.notifyDataSetChanged();
                    } else {
                        if (justCanAdd()) {
                            selectedTag = selectedTag.concat("#" + epi.getUserId() + "@");
                            recyclerSwipeLayout.notifyDataSetChanged();
                        } else {
                            ToastUtil.showText("已达报名人数上限");
                        }
                    }
                }
            });
        }
    };

    private void requestResource() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("positionId", positionId + "");
        params.put("pageSize", pageSize + "");
        params.put("userStatus", "0");//默认0未入职1已入职
        OkHttpUtils.getAsyn(NetWorkConfig.BROKER_RESOURCE_ISCAN_ENTRY, params, MyResourceResponse.class, new HttpCallback() {
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
                            recyclerSwipeLayout.setVisibility(View.GONE);
                            okBtn.setText("邀请");
                            okBtn.setTag("YAO");
                            return;
                        }
                        okBtn.setTag("BAO");
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

    private boolean justCanAdd() {
        if (enrollNum >= hiringCount || hiringCount == 0) {
            return false;
        }
        String userIds = selectedTag.replace("@#", ",").replace("@", "").replace("#", "");
        if (TextUtils.isEmpty(userIds)) {
            return true;
        }
        int p = hiringCount - enrollNum;
        int count = 1;
        if (userIds.contains(",")) {
            count = userIds.split(",").length;
        }
        if (count < p) {
            return true;
        }
        return false;
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
        String userIds = selectedTag.replace("@#", ",").replace("@", "").replace("#", "");
        if (TextUtils.isEmpty(userIds)) {
            ToastUtil.showText("请选择报名人");
            return;
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
                                finish();
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