package com.benben.bb.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.share.AndroidShare;
import com.benben.bb.share.Defaultcontent;
import com.benben.bb.share.ShareStyle;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.TitleBar;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-05-17.
 * email:shoxgov@126.com
 */

public class PersonQrActivity extends BaseActivity {

    @Bind(R.id.my_person_qr_photo)
    ImageView photoImg;
    @Bind(R.id.my_person_qr_img)
    ImageView qrImg;
    @Bind(R.id.my_person_invite_name)
    TextView userNameTv;

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
        if (TextUtils.isEmpty(UserData.getUserData().getNickName())) {
            userNameTv.setText("");
        } else {
            userNameTv.setText(UserData.getUserData().getNickName());
        }
        if (!TextUtils.isEmpty(UserData.getUserData().getAvatar())) {
            Glide.with(PersonQrActivity.this)
                    .load(UserData.getUserData().getAvatar())
                    .placeholder(R.mipmap.default_image)
                    .error(R.mipmap.default_image)
                    .into(photoImg);
        }
        LoadQrPic loadQrPic = new LoadQrPic();
        loadQrPic.execute(shareUrl);
    }


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
