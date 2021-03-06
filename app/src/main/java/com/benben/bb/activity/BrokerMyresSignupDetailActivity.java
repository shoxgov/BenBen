package com.benben.bb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.BrokerPositionUserGridViewAdapter;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.MyResSignupPositionUserResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.AutoLoadListener;
import com.benben.bb.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-06-01.
 * email:shoxgov@126.com
 */

public class BrokerMyresSignupDetailActivity extends BaseActivity {
    @Bind(R.id.gridview)
    GridView gridview;
    @Bind(R.id.broker_myteam_add_ok)
    Button okBtn;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    private BrokerPositionUserGridViewAdapter adapter;
    private int positionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_myteam_add);
        ButterKnife.bind(this);
        positionId = getIntent().getIntExtra("positionId", 0);
        init();
    }

    private void init() {
        okBtn.setVisibility(View.GONE);
        TitleBar titleBar = (TitleBar) findViewById(R.id.titlelayout);
        titleBar.setTitleName("报名人数");
        titleBar.setTitleBarListener(new TitleBarListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        adapter = new BrokerPositionUserGridViewAdapter(this);
        gridview.setAdapter(adapter);
        AutoLoadListener autoLoadListener = new AutoLoadListener(callBack);
        gridview.setOnScrollListener(autoLoadListener);
        requestPositionUserList();
    }

    AutoLoadListener.AutoLoadCallBack callBack = new AutoLoadListener.AutoLoadCallBack() {

        public void execute() {
            if (pageNo < totalPage) {
                pageNo++;
                requestPositionUserList();
            } else {

            }//这段代码是用来请求下一页数据的
        }

    };

    private void requestPositionUserList() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("positionId", positionId + "");//职位ID
        params.put("code", "1");//状态：1已报名2已入职
        OkHttpUtils.getAsyn(NetWorkConfig.BROKER_GET_POSITION_USERLIST, params, MyResSignupPositionUserResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    MyResSignupPositionUserResponse mar = (MyResSignupPositionUserResponse) resultDesc;
                    if (mar.getCode() == 1) {
                        pageNo = mar.getData().getPageNum();
                        totalPage = mar.getData().getPages();
                        if (totalPage == 0) {
                            ToastUtil.showText("当前没有数据");
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
}
