package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.MyInviteResourceResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.TitleBar;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-08-21.
 * email:shoxgov@126.com
 */

public class MyInviteActivity extends BaseActivity {

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
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void init() {
        TitleBar titleLayout = (TitleBar) findViewById(R.id.titlelayout);
        titleLayout.setTitleName("我的邀请");
        titleLayout.setTitleBarListener(new TitleBarListener() {

            @Override
            public void rightClick() {
            }

            @Override
            public void leftClick() {
                finish();
            }
        });
        recyclerSwipeLayout.createAdapter(R.layout.list_broker_myres_total_item);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        requestMyInvite();
    }

    private void requestMyInvite() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        OkHttpUtils.getAsyn(NetWorkConfig.GET_MY_INVITE_RESOURCE, params, MyInviteResourceResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                MyInviteResourceResponse mrr = (MyInviteResourceResponse) br;
                if (mrr.getCode() == 1) {
                    pageNo = mrr.getData().getPageNum();
                    totalPage = mrr.getData().getPages();
                    if (totalPage == 0) {
                        recyclerSwipeLayout.setEmpty();
                        return;
                    }
                    List<MyInviteResourceResponse.MyInviteResource> temp = mrr.getData().getList();
                    recyclerSwipeLayout.addData(temp);
                } else {
                    ToastUtil.showText(mrr.getMessage());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener = new BaseQuickAdapter.RequestLoadMoreListener() {

        @Override
        public void onLoadMoreRequested() {
            if (pageNo < totalPage) {
                pageNo++;
                requestMyInvite();
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };

    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final MyInviteResourceResponse.MyInviteResource mir = (MyInviteResourceResponse.MyInviteResource) itemModel;
            if (!TextUtils.isEmpty(mir.getAvatar())) {
                Glide.with(MyInviteActivity.this)
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
                    person.setClass(MyInviteActivity.this, UserInfoActivity.class);
                    person.putExtra("userId", mir.getUserId() + "");
                    startActivity(person);
                }
            });
        }

    };
}
