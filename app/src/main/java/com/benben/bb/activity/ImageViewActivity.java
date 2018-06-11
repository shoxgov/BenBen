/**
 * @Title: ImageViewActivity.java
 * @date: 2015-12-2 上午10:41:18
 * @Copyright: (c) 2015, unibroad.com Inc. All rights reserved.
 */
package com.benben.bb.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import com.benben.bb.R;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.ImageControl;
import com.benben.bb.view.TitleBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

/**
 * @Class: ImageViewActivity
 */
public class ImageViewActivity extends Activity {
    private String url;
    ImageControl imgControl;
    private String title;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_common_image_view);
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
        imgControl = (ImageControl) findViewById(R.id.common_imageview_imageControl1);
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("filename");
        }
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(ImageViewActivity.this, "图片为空", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        titleLayout.setTitleName(title);
        init();
    }

    private void init() {
        try {
            Glide.with(this)
                    .load(url)
                    .asBitmap()
                    .placeholder(R.mipmap.default_image).dontAnimate()
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                            imgControl.setImageBitmap(bitmap);
                            int screenW = ImageViewActivity.this.getWindowManager().getDefaultDisplay().getWidth();
                            int screenH = ImageViewActivity.this.getWindowManager().getDefaultDisplay().getHeight();
                            imgControl.imageInit(bitmap, screenW, screenH);
                        }

                    });
            ToastUtil.showText("图片加载中...");
        } catch (Exception e) {
            ToastUtil.showText("加载图片失败");
            finish();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgControl == null) {
            return true;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                imgControl.mouseDown(event);
                break;
            /**
             * 非第一个点按下
             */
            case MotionEvent.ACTION_POINTER_DOWN:
                imgControl.mousePointDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                imgControl.mouseMove(event);
                break;

            case MotionEvent.ACTION_UP:
                imgControl.mouseUp();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}