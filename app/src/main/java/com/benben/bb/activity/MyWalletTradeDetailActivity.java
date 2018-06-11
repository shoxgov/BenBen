package com.benben.bb.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.adapter.WalletTradeExpandableListAdapter;
import com.benben.bb.bean.UserData;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyRecruitResponse;
import com.benben.bb.okhttp3.response.WalletTradeResponse;
import com.benben.bb.utils.DateUtils;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.TitleBar;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

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

public class MyWalletTradeDetailActivity extends BaseActivity {
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    @Bind(R.id.wallet_trade_detail_title)
    TextView totalTv;
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
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        requestTrade();
    }

    private void requestTrade() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        OkHttpUtils.getAsyn(NetWorkConfig.USER_TRADE_DETAIL, params, WalletTradeResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                WalletTradeResponse wtr = (WalletTradeResponse) br;
                if (wtr.getCode() == 1) {
                    totalTv.setText("总收入：" + wtr.getData().getIncome() + "元    总支出：" + wtr.getData().getPay() + "元");
                    pageNo = wtr.getData().getPage().getPageNum();
                    totalPage = wtr.getData().getPage().getPages();
                    if (totalPage == 0) {
                        recyclerSwipeLayout.setEmpty();
                        return;
                    }
                    recyclerSwipeLayout.addData(wtr.getData().getPage().getList());
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
                requestTrade();
            }
            recyclerSwipeLayout.loadComplete();
        }
    };

    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final WalletTradeResponse.TradeInfo ti = (WalletTradeResponse.TradeInfo) itemModel;
            baseViewHolder.setText(R.id.wallet_trade_child_date, ti.getCreateDate());
            String title = "";
            switch (ti.getBillType()) {//bill_type 账单类型 : 1出账2入账
                case 2:
                    title = "分佣";
                    baseViewHolder.setText(R.id.wallet_trade_child_count, "+" + ti.getMoney() + "¥");
                    baseViewHolder.setTextColor(R.id.wallet_trade_child_count, getResources().getColor(R.color.textblack));
                    break;
                case 1:
                    title = "提现";
                    baseViewHolder.setText(R.id.wallet_trade_child_count, "-" + ti.getMoney() + "¥");
                    baseViewHolder.setTextColor(R.id.wallet_trade_child_count, Color.parseColor("#18B7A1"));
                    break;
            }
            switch (ti.getBillStatus()) {//bill_status  1成功2失败3申请中
                case 1:
//                    title += "";
                    break;
                case 2:
                    title += "(失败 " + ti.getBillDetail() + ")";
                    break;
                case 3:
                    title += "(申请中)";
                    break;
            }
            baseViewHolder.setText(R.id.wallet_trade_child_title, title);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
