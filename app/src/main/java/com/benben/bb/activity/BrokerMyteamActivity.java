package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.bean.UserData;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.LoginResponse;
import com.benben.bb.okhttp3.response.MyAgentsResponse;
import com.benben.bb.utils.PreferenceUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.RotateTransformation;
import com.benben.bb.view.TitleBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-05-25.
 * email:shoxgov@126.com
 */

public class BrokerMyteamActivity extends BaseActivity implements View.OnClickListener {
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    @Bind(R.id.broker_myteam_layout)
    LinearLayout layout;
    private View emptyView, listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_myteam);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        TitleBar titleLayout = (TitleBar) findViewById(R.id.titlelayout);
        titleLayout.setTitleBarListener(new TitleBarListener() {

            @Override
            public void rightClick() {
            }

            @Override
            public void leftClick() {
                finish();
            }
        });
        requestTeams();
    }

    private void initStatus(boolean isEmpty) {
        if (isEmpty) {
            emptyView = LayoutInflater.from(this).inflate(R.layout.view_broker_myteam_inivate, null, false);
            emptyView.findViewById(R.id.view_broker_myteam_empty_add).setOnClickListener(this);
            TextView emptyHintTv = (TextView) emptyView.findViewById(R.id.view_broker_myteam_empty_hint);
            emptyHintTv.setText("您现在是就业顾问V" + UserData.getUserData().getIsAgent() + ",可以设置" + (UserData.getUserData().getIsAgent() - 1) + "个就业顾问");
            layout.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layout.addView(emptyView, params);
        } else {
            listView = LayoutInflater.from(this).inflate(R.layout.view_broker_myteam_list, null, false);
            listView.findViewById(R.id.view_broker_myteam_add).setOnClickListener(this);
            TextView hintTv = (TextView) listView.findViewById(R.id.view_broker_myteam_hint);
            hintTv.setText("您还可以设置" + (UserData.getUserData().getIsAgent() - 1) + "个就业顾问");
            recyclerSwipeLayout = listView.findViewById(R.id.recyclerRefreshLayout);
            layout.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layout.addView(listView, params);
            recyclerSwipeLayout.setDividerDashline(this, getResources().getColor(R.color.mainbg), 5);
            recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
            recyclerSwipeLayout.setXCallBack(callBack);
        }
    }

    private void requestTeams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        OkHttpUtils.getAsyn(NetWorkConfig.BROKER_AGENTS_LIST, params, MyAgentsResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    MyAgentsResponse mar = (MyAgentsResponse) resultDesc;
                    if (mar.getCode() == 1) {
                        pageNo = mar.getData().getPageNum();
                        totalPage = mar.getData().getPages();
                        if (totalPage == 0) {
                            initStatus(true);
                            return;
                        }
                        initStatus(false);
                        List<MyAgentsResponse.AgentInfo> temp = mar.getData().getList();
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
                requestTeams();
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };


    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final MyAgentsResponse.AgentInfo ai = (MyAgentsResponse.AgentInfo) itemModel;
            Glide.with(getApplicationContext())
                    .load(ai.getAvatar())
                    .error(R.mipmap.default_image)
                    .into((ImageView) baseViewHolder.getView(R.id.broker_myteam_item_photo));
            baseViewHolder.setText(R.id.broker_myteam_item_name, ai.getTrueName() + "    " + ai.getUserName());
            baseViewHolder.setText(R.id.broker_myteam_item_num, Html.fromHtml("资源数  <font color=#FD7979>" + ai.getResourcesSum() + "</font>"));
            TextView cancelBtn = (TextView) baseViewHolder.getView(R.id.broker_myteam_item_cancel);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", ai.getUserId() + "");
                    OkHttpUtils.getAsyn(NetWorkConfig.BROKER_DEL_AGENTS, params, BaseResponse.class, new HttpCallback() {
                        @Override
                        public void onSuccess(BaseResponse br) {
                            super.onSuccess(br);
                            ToastUtil.showText(br.getMessage());
                            if (br.getCode() == 1) {
                                pageNo = 1;
                                totalPage = -1;
                                recyclerSwipeLayout.setNewData(new ArrayList<MyAgentsResponse.AgentInfo>());
                                requestTeams();
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
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_broker_myteam_empty_add:
            case R.id.view_broker_myteam_add:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                if ((UserData.getUserData().getIsAgent() - 1) < 1) {
                    ToastUtil.showText("您的当前等级不能再添加就业顾问");
                    return;
                }
                Intent add = new Intent();
                add.setClass(BrokerMyteamActivity.this, BrokerMyteamAddActivity.class);
                startActivityForResult(add, 11);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            updateUserInfo();
        }
    }

    private void updateUserInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", PreferenceUtil.getString("LoginTel", ""));
        params.put("logCode", PreferenceUtil.getString("LoginCode", ""));
        OkHttpUtils.getAsyn(NetWorkConfig.LOGIN, params, LoginResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    LoginResponse lr = (LoginResponse) resultDesc;
                    UserData.updateAccount(lr);
                    requestTeams();
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
