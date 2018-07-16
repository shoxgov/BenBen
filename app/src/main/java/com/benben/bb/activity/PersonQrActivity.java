package com.benben.bb.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.bean.UserData;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.MyInviteResourceResponse;
import com.benben.bb.okhttp3.response.SearchResultResponse;
import com.benben.bb.share.AndroidShare;
import com.benben.bb.share.Defaultcontent;
import com.benben.bb.share.ShareStyle;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
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
 * Created by wangshengyin on 2018-05-17.
 * email:shoxgov@126.com
 */

public class PersonQrActivity extends BaseActivity {

    @Bind(R.id.my_person_qr_img)
    ImageView qrImg;
    @Bind(R.id.my_person_qr_benbenid)
    TextView qrId;
    @Bind(R.id.my_person_invite_hint)
    TextView hintTv;
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
        setContentView(R.layout.activity_person_qr);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        final String shareUrl = NetWorkConfig.H5HOST + "?bbNum=" + UserData.getUserData().getBenbenNum();
        TitleBar titleLayout = (TitleBar) findViewById(R.id.titlelayout);
        titleLayout.setTitleBarListener(new TitleBarListener() {

            @Override
            public void rightClick() {
                Intent share = new Intent();
                share.setClass(PersonQrActivity.this, AndroidShare.class);
                share.putExtra("text", "犇犇分享！");
                share.putExtra("type", ShareStyle.ShareType.WEB.ordinal());
                share.putExtra("url", shareUrl);
                share.putExtra("title", "一款好工作会赚钱的APP-职犇犇");
                share.putExtra("description", Defaultcontent.description);
                startActivity(share);
            }

            @Override
            public void leftClick() {
                finish();
            }
        });
        qrId.setText("我的邀请码：" + UserData.getUserData().getBenbenNum());
        LoadQrPic loadQrPic = new LoadQrPic();
        loadQrPic.execute(shareUrl);
        recyclerSwipeLayout.createAdapter(R.layout.list_invite_item);
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
                    hintTv.setText(Html.fromHtml("已成功邀请<font color=#FF4081>" + mrr.getData().getTotal() + "</font>位好友加入职犇犇"));
                    if (totalPage == 0) {
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
            MyInviteResourceResponse.MyInviteResource mir = (MyInviteResourceResponse.MyInviteResource) itemModel;
            baseViewHolder.setText(R.id.qr_invite_tel, mir.getUserName().replace(mir.getUserName().substring(3, 7), "****"));
            baseViewHolder.setText(R.id.qr_invite_date, mir.getCreateDate());
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 参数说明 依次为background progress result
     */
    private class LoadQrPic extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            DialogUtil.showDialogLoading(PersonQrActivity.this,"");
        }

        @Override
        protected Bitmap doInBackground(String[] params) {
            try {
                if (!TextUtils.isEmpty(UserData.getUserData().getAvatar())) {
                    return Utils.createQRImageByPhoto(PersonQrActivity.this, params[0], "");
                } else {
                    return Utils.createQRImage(PersonQrActivity.this, params[0], "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap o) {
            if (o != null) {
                qrImg.setImageBitmap(o);
//                qrResult.setText("我的邀请码：" + UserData.token);
            }
//            DialogUtil.hideDialogLoading();
        }
    }
}
