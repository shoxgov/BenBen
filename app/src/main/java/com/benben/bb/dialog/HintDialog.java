package com.benben.bb.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.benben.bb.MyApplication;
import com.benben.bb.R;

public class HintDialog extends Dialog {

    private String hintContent;
    private int imgId;
    private DisplayMetrics dm;
    private ImageView hintImg;
    private TextView hintTxt;


    public HintDialog(Context context, int imgId, String hintContent) {
        this(context, R.style.CustomDialog_discovery);
        this.imgId = imgId;
        this.hintContent = hintContent;
    }

    public HintDialog(Context context, int themeId) {
        super(context, themeId);
        dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_hint);
        hintImg = (ImageView) findViewById(R.id.dialog_hint_img);
        hintTxt = (TextView) findViewById(R.id.dialog_hint_txt);
        hintImg.setImageResource(imgId);
        hintTxt.setText(hintContent);
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

}
