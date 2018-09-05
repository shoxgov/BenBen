package com.benben.bb.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.BrokerAddGridViewAdapter;
import com.benben.bb.bean.UserData;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.AddAgentListResponse;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.AutoLoadListener;
import com.benben.bb.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-25.
 * email:shoxgov@126.com
 */

public class BrokerMyteamAddActivity extends BaseActivity {
    @Bind(R.id.broker_myteam_add_list)
    ListView listView;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    private BrokerAddGridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_myteam_add);
        ButterKnife.bind(this);
        init();
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
        adapter = new BrokerAddGridViewAdapter(this, true);
        listView.setAdapter(adapter);
        AutoLoadListener autoLoadListener = new AutoLoadListener(callBack);
        listView.setOnScrollListener(autoLoadListener);
        requestAddAgents();
    }

    AutoLoadListener.AutoLoadCallBack callBack = new AutoLoadListener.AutoLoadCallBack() {

        public void execute() {
            if (pageNo < totalPage) {
                pageNo++;
                requestAddAgents();
            } else {

            }//这段代码是用来请求下一页数据的
        }

    };

    private void requestAddAgents() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        OkHttpUtils.getAsyn(NetWorkConfig.BROKER_AGENTS_ADD_LIST, params, AddAgentListResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    AddAgentListResponse mar = (AddAgentListResponse) resultDesc;
                    if (mar.getCode() == 1) {
                        pageNo = mar.getData().getPageNum();
                        totalPage = mar.getData().getPages();
                        if (totalPage == 0) {
                            ToastUtil.showText("当前没有可增加的就业顾问");
                            return;
                        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.broker_myteam_add_ok)
    public void onViewClicked() {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        List<AddAgentListResponse.AgentInfo> tempData = adapter.getData();
        if(tempData == null){
            ToastUtil.showText("当前无可添加人员");
            return;
        }
        String userIds = "";
        String selectedTag = adapter.getSelectedTag();
        int count = 0;
        for (AddAgentListResponse.AgentInfo epi : tempData) {
            if (selectedTag.contains("#" + epi.getUserId() + "@")) {
                count++;
                if (TextUtils.isEmpty(userIds)) {
                    userIds = epi.getUserId() + "";
                } else {
                    userIds += "," + epi.getUserId();
                }
            }
        }
        if ((UserData.getUserData().getIsAgent() - 1) < count) {
            ToastUtil.showText("你只能添加" + (UserData.getUserData().getIsAgent() - 1) + "个就业顾问");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("userIds", userIds);//用户ID逗号分隔
        OkHttpUtils.getAsyn(NetWorkConfig.BROKER_ADD_AGENTS, params, BaseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                ToastUtil.showText(br.getMessage());
                if (br.getCode() == 1) {
                    setResult(RESULT_OK);
                    finish();
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
