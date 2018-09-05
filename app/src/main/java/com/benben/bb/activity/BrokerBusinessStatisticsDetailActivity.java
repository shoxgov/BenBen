package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.bb.MyApplication;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.response.BrokerEnrollSignupPositionResponse;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.TitleBar;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-05-28.
 * email:shoxgov@126.com
 */

public class BrokerBusinessStatisticsDetailActivity extends BaseActivity {

    @Bind(R.id.broker_statistics_detail_name)
    TextView nameTv;
    @Bind(R.id.broker_statistics_detail_hint)
    TextView hintTv;
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
//    @Bind(R.id.gridview)
//    GridView gridview;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    //    private BrokerPositionUserGridViewAdapter adapter;
    private int positionId;
    private float commision;
    private String positionName;
    private String focusSalary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_statistic_detail);
        ButterKnife.bind(this);
        positionId = getIntent().getIntExtra("positionId", 0);
        focusSalary = getIntent().getStringExtra("focusSalary");
        commision = getIntent().getFloatExtra("commision", 0);
        positionName = getIntent().getStringExtra("positionName");
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
        nameTv.setText(positionName);
        hintTv.setText("综合薪资：" + focusSalary + "元    就业顾问佣金：" + commision + "元/小时");
        recyclerSwipeLayout.createAdapter(R.layout.list_broker_myres_total_item);
        recyclerSwipeLayout.setXCallBack(callBack);
        recyclerSwipeLayout.addData(MyApplication.brokerBusinessStatisticsList);
        recyclerSwipeLayout.loadComplete();

//        adapter = new BrokerPositionUserGridViewAdapter(this);
//        gridview.setAdapter(adapter);
//        AutoLoadListener autoLoadListener = new AutoLoadListener(callBack);
//        gridview.setOnScrollListener(autoLoadListener);
//        requestPositionUserList();
    }

    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final BrokerEnrollSignupPositionResponse.SignupPositionInfo mir = (BrokerEnrollSignupPositionResponse.SignupPositionInfo) itemModel;
            if (!TextUtils.isEmpty(mir.getAvatar())) {
                Glide.with(BrokerBusinessStatisticsDetailActivity.this)
                        .load(mir.getAvatar())
                        .placeholder(R.mipmap.default_image)
                        .error(R.mipmap.default_image)
                        .into((ImageView) baseViewHolder.getView(R.id.item_img));
            }
            if (TextUtils.isEmpty(mir.getTrueName())) {
                baseViewHolder.setText(R.id.item_name, "未实名");
            } else {
                baseViewHolder.setText(R.id.item_name, mir.getTrueName());
            }
            baseViewHolder.setText(R.id.item_year, mir.getAge() + "");
            baseViewHolder.setText(R.id.item_edu, mir.getEducation());
            if (mir.getSex() == 1) {
                baseViewHolder.setText(R.id.item_sex, "女");
            } else {
                baseViewHolder.setText(R.id.item_sex, "男");
            }
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent person = new Intent();
                    person.setClass(BrokerBusinessStatisticsDetailActivity.this, UserInfoActivity.class);
                    person.putExtra("userId", mir.getUserId() + "");
                    startActivity(person);
                }
            });
        }
    };
//    AutoLoadListener.AutoLoadCallBack callBack = new AutoLoadListener.AutoLoadCallBack() {
//
//        public void execute() {
//            if (pageNo < totalPage) {
//                pageNo++;
//                requestPositionUserList();
//            } else {
//
//            }//这段代码是用来请求下一页数据的
//        }
//
//    };

    private void requestPositionUserList() {
//        final Map<String, String> params = new HashMap<String, String>();
//        params.put("pageNum", pageNo + "");
//        params.put("pageSize", pageSize + "");
//        params.put("positionId", positionId + "");//职位ID
//        params.put("code", "2");//状态：1已报名2已入职
//        OkHttpUtils.getAsyn(NetWorkConfig.BROKER_GET_POSITION_USERLIST, params, MyResSignupPositionUserResponse.class, new HttpCallback() {
//            @Override
//            public void onSuccess(BaseResponse resultDesc) {
//                super.onSuccess(resultDesc);
//                try {
//                    MyResSignupPositionUserResponse mar = (MyResSignupPositionUserResponse) resultDesc;
//                    if (mar.getCode() == 1) {
//                        pageNo = mar.getData().getPageNum();
//                        totalPage = mar.getData().getPages();
//                        if (totalPage == 0) {
//                            ToastUtil.showText("当前没有数据");
//                            return;
//                        }
//                        adapter.setData(mar.getData().getList());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//                super.onFailure(code, message);
//                ToastUtil.showText(message);
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        MyApplication.brokerBusinessStatisticsList = null;
    }
}
