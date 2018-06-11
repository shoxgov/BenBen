package com.benben.bb.activity;

import android.os.Bundle;
import android.widget.ExpandableListView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.WalletTradeExpandableListAdapter;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.WalletTradeResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by wangshengyin on 2018-06-07.
 * email:shoxgov@126.com
 */

public class MyWalletExpandTradeDetailActivity extends BaseActivity {
    @Bind(R.id.wallet_trade_expandlist)
    ExpandableListView expandlist;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    private WalletTradeExpandableListAdapter adapter;
    private Map<String, List<String>> dataset = new HashMap<>();
    String[] parentList = new String[]{"000", "111", "222"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_trade_detail);
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
        requestTrade();
        initialData();
        adapter = new WalletTradeExpandableListAdapter(this);
        expandlist.setAdapter(adapter);
        adapter.setData(dataset, parentList);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            expandlist.expandGroup(i);
        }
    }

    private void requestTrade() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        OkHttpUtils.getAsyn(NetWorkConfig.USER_TRADE_DETAIL, params, WalletTradeResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText(message);
            }
        });
    }

    private void initialData() {
        List<String> childrenList1 = new ArrayList<>();
        List<String> childrenList2 = new ArrayList<>();
        List<String> childrenList3 = new ArrayList<>();
        childrenList1.add(parentList[0] + "-" + "first");
        childrenList1.add(parentList[0] + "-" + "second");
        childrenList1.add(parentList[0] + "-" + "third");
        childrenList2.add(parentList[1] + "-" + "first");
        childrenList2.add(parentList[1] + "-" + "second");
        childrenList2.add(parentList[1] + "-" + "third");
        childrenList3.add(parentList[2] + "-" + "first");
        childrenList3.add(parentList[2] + "-" + "second");
        childrenList3.add(parentList[2] + "-" + "third");
        dataset.put(parentList[0], childrenList1);
        dataset.put(parentList[1], childrenList2);
        dataset.put(parentList[2], childrenList3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
