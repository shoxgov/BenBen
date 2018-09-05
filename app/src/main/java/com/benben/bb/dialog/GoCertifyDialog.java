package com.benben.bb.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.benben.bb.MyApplication;
import com.benben.bb.R;
import com.benben.bb.imp.DialogCallBack;

public class GoCertifyDialog extends Dialog implements OnClickListener {

    private String title, info, cancelString, okString;
    private boolean cancelVisible, okVisible;
    private DialogCallBack dialogcallback;
    private DisplayMetrics dm;


    public GoCertifyDialog(Context context, DialogCallBack dialogcallback) {
        this(context, R.style.CustomDialog_discovery);
        this.dialogcallback = dialogcallback;
        cancelVisible = true;
        okVisible = true;
    }

    public GoCertifyDialog(Context context, String title, String info, String cancelString, boolean cancelVisible, String okString, boolean okVisible, DialogCallBack dialogcallback) {
        this(context, R.style.CustomDialog_discovery);
        this.dialogcallback = dialogcallback;
        this.title = title;
        this.info = info;
        this.cancelString = cancelString;
        this.cancelVisible = cancelVisible;
        this.okString = okString;
        this.okVisible = okVisible;
    }

    public GoCertifyDialog(Context context, int themeId) {
        super(context, themeId);
        dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_go_certify);
        Button cancelBtn = (Button) findViewById(R.id.seeBtn);
        cancelBtn.setOnClickListener(this);
        if (!TextUtils.isEmpty(cancelString)) {
            cancelBtn.setText(cancelString);
        }
        if (!cancelVisible) {
            cancelBtn.setVisibility(View.INVISIBLE);
        }
        Button okBtn = (Button) findViewById(R.id.okBtn);
        okBtn.setOnClickListener(this);
        if (!TextUtils.isEmpty(okString)) {
            okBtn.setText(okString);
        }
        if (!okVisible) {
            okBtn.setVisibility(View.INVISIBLE);
        }
        TextView titleTv = (TextView) findViewById(R.id.dialog_title);
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
        TextView infoTv = (TextView) findViewById(R.id.showInfo);
        if (TextUtils.isEmpty(info)) {
            infoTv.setText(Html.fromHtml("您还未完成<font color=red>[实名认证]</font>，不能使用报名职位功能，请先完成<font color=red>[实名认证]</font>。"));
        } else {
            infoTv.setText(Html.fromHtml(info));
        }
        int width = (int) (MyApplication.screenWidthPixels * 0.8);
        if (width <= 0) {
            width = (int) (dm.widthPixels * 0.8);
        }
        getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onBackPressed() {
        dismiss();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seeBtn:
                if (dialogcallback != null) {
                    dialogcallback.CancleDown();
                }
                break;
            case R.id.okBtn:
                if (dialogcallback != null) {
                    dialogcallback.OkDown(null);
                }
                break;
        }
        dismiss();
    }
}
