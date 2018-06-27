package com.benben.bb.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.bb.R;
import com.benben.bb.utils.Utils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.shareboard.SnsPlatform;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AndroidShare extends Activity implements
        AdapterView.OnItemClickListener {
    @Bind(R.id.share_gridview)
    GridView shareGridview;
    private ArrayList<SnsPlatform> platforms = new ArrayList<SnsPlatform>();
    /**
     *
     */
    private ShareStyle.ShareType shareType = ShareStyle.ShareType.TEXT;
    private UMImage imageurl, imagelocal;
    private UMVideo video;
    private UMusic music;
    private UMEmoji emoji;
    private UMWeb web;
    private File file;
//    private ProgressDialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
//        if (Build.VERSION.SDK_INT >= 23) {
//            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW};
//            ActivityCompat.requestPermissions(this, mPermissionList, 123);
//        }
        shareType = ShareStyle.ShareType.values()[getIntent().getIntExtra("type", ShareStyle.ShareType.TEXT.ordinal())];
        ButterKnife.bind(this);
        initShare();
        initPlatforms();
        init();
    }

    void init() {
//        dialog = new ProgressDialog(this);
        getWindow().setGravity(80);
        this.shareGridview.setAdapter(new MyAdapter(platforms));
        this.shareGridview.setOnItemClickListener(this);
    }

    private void initShare() {
        switch (shareType) {
            case TEXT:
                Defaultcontent.text = getIntent().getStringExtra("text");
                break;
            case IMAGELOCAL:
                Defaultcontent.imageurl = getIntent().getStringExtra("imagelocal");
                break;
            case IMAGEURL:
                Defaultcontent.imageurl = getIntent().getStringExtra("imageurl");
                break;
            case WEB:
                Defaultcontent.url = getIntent().getStringExtra("url");
                Defaultcontent.text = getIntent().getStringExtra("text");
                Defaultcontent.title = getIntent().getStringExtra("title");
                Defaultcontent.description = getIntent().getStringExtra("description");
                break;
        }

    }

    private void initPlatforms() {
        platforms.clear();
        platforms.add(SHARE_MEDIA.WEIXIN.toSnsPlatform());//微信
        platforms.add(SHARE_MEDIA.QQ.toSnsPlatform());//
        platforms.add(SHARE_MEDIA.SINA.toSnsPlatform());//
        platforms.add(SHARE_MEDIA.WEIXIN_CIRCLE.toSnsPlatform());//微信朋友圈
//        platforms.add(SHARE_MEDIA.WEIXIN_FAVORITE.toSnsPlatform());//微信收蒧
        platforms.add(SHARE_MEDIA.QZONE.toSnsPlatform());//QQ空间
//        platforms.add(SHARE_MEDIA.SMS.toSnsPlatform());//短信
//        platforms.add(SHARE_MEDIA.EMAIL.toSnsPlatform());//邮件
//        platforms.add(SHARE_MEDIA.TENCENT.toSnsPlatform());//腾讯微博
//        platforms.add(SHARE_MEDIA.MORE.toSnsPlatform());//更多

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        SHARE_MEDIA share_media = platforms.get(position).mPlatform;
        switch (shareType) {
            case IMAGELOCAL:
                imagelocal = new UMImage(this, new File(Defaultcontent.imageurl));
                imagelocal.setThumb(new UMImage(this, new File(Defaultcontent.imageurl)));
                new ShareAction(this).withMedia(imagelocal)
                        .setPlatform(share_media)
                        .setCallback(shareListener).share();
                break;
            case IMAGEURL:
                imageurl = new UMImage(this, Defaultcontent.imageurl);
                imageurl.setThumb(new UMImage(this, Defaultcontent.imageurl));
                new ShareAction(this).withMedia(imageurl)
                        .setPlatform(share_media)
                        .setCallback(shareListener).share();
                break;
            case TEXT:
//                if(share_media == SHARE_MEDIA.QQ){
//                    web = new UMWeb(Defaultcontent.url);
//                    web.setTitle(Defaultcontent.title);
//                    web.setDescription(Defaultcontent.text);
//                    web.setThumb(new UMImage(this, R.mipmap.ic_launcher));
//                    new ShareAction(this).withText(Defaultcontent.text)
//                            .withMedia(web)
//                            .setPlatform(share_media)
//                            .setCallback(shareListener).share();
//                    break;
//                }
                new ShareAction(this).withText(Defaultcontent.text)
                        .setPlatform(share_media)
                        .setCallback(shareListener).share();
                break;
            case WEB:
                web = new UMWeb(Defaultcontent.url);
                web.setTitle(Defaultcontent.title);
                web.setThumb(new UMImage(this, R.mipmap.ic_launcher));
                web.setDescription(Defaultcontent.description);
                new ShareAction(this).withText(Defaultcontent.text)
                        .withMedia(web)
                        .setPlatform(share_media)
                        .setCallback(shareListener).share();
                break;
            case TEXTANDIMAGE:
            case MUSIC00:
                new ShareAction(this)
                        .withMedia(music)
                        .setPlatform(share_media)
                        .setCallback(shareListener).share();
                break;
            case VIDEO00:
                new ShareAction(this)
                        .withMedia(video)
                        .setPlatform(share_media)
                        .setCallback(shareListener).share();
                break;
            case EMOJI:
                new ShareAction(this)
                        .withMedia(emoji)
                        .setPlatform(share_media)
                        .setCallback(shareListener).share();
                break;
            case FILE:
                new ShareAction(this)
                        .withFile(file)
                        .withText(Defaultcontent.text)
                        .withSubject(Defaultcontent.title)
                        .setPlatform(share_media)
                        .setCallback(shareListener).share();
                break;
            case MINAPP:
                UMMin umMin = new UMMin(Defaultcontent.url);
                umMin.setThumb(imagelocal);
                umMin.setTitle(Defaultcontent.title);
                umMin.setDescription(Defaultcontent.text);
                umMin.setPath("pages/page10007/page10007");
                umMin.setUserName("gh_3ac2059ac66f");
                new ShareAction(this)
                        .withMedia(umMin)
                        .setPlatform(share_media)
                        .setCallback(shareListener).share();
                break;
        }

    }


    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
//            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(AndroidShare.this, "成功了", Toast.LENGTH_LONG).show();
            finish();
//            SocializeUtils.safeCloseDialog(dialog);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(AndroidShare.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            SocializeUtils.safeCloseDialog(dialog);
//            Toast.makeText(AndroidShare.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };

    @OnClick(R.id.share_cancel)
    public void onViewClicked() {
        finish();
    }

    private final class MyAdapter extends BaseAdapter {

        ArrayList<SnsPlatform> platforms;

        public MyAdapter(ArrayList<SnsPlatform> platforms) {
            this.platforms = platforms;
        }

        public int getCount() {
            return platforms.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0L;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(AndroidShare.this).inflate(R.layout.dialog_share_item, null, false);
            ImageView img = (ImageView) convertView.findViewById(R.id.share_item_logo);
            TextView tv = (TextView) convertView.findViewById(R.id.share_item_title);
            SnsPlatform sp = platforms.get(position);
            img.setImageResource(ResContainer.getResourceId(AndroidShare.this, "mipmap", sp.mIcon));
            tv.setText(ResContainer.getResourceId(AndroidShare.this, "string", sp.mShowWord));
            return convertView;
        }
    }

}
