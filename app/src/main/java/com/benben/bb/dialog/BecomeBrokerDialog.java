package com.benben.bb.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.benben.bb.MyApplication;
import com.benben.bb.R;
import com.benben.bb.imp.DialogCallBack;

public class BecomeBrokerDialog extends Dialog implements OnClickListener {

    private DialogCallBack dialogcallback;
    private DisplayMetrics dm;
    private String info;


    public BecomeBrokerDialog(Context context, DialogCallBack dialogcallback) {
        this(context, R.style.CustomDialog_discovery);
        this.dialogcallback = dialogcallback;
    }
    public BecomeBrokerDialog(Context context, String info, DialogCallBack dialogcallback) {
        this(context, R.style.CustomDialog_discovery);
        this.dialogcallback = dialogcallback;
        this.info = info;
    }

    public BecomeBrokerDialog(Context context, int themeId) {
        super(context, themeId);
        dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_become_broker);
        setCanceledOnTouchOutside(false);
        findViewById(R.id.seeBtn).setOnClickListener(this);
        findViewById(R.id.okBtn).setOnClickListener(this);
        TextView infoTv = (TextView) findViewById(R.id.showInfo);
        if(!TextUtils.isEmpty(this.info)){
            infoTv.setText(info);
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
