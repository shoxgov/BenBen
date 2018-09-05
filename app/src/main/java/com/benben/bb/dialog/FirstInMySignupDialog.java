package com.benben.bb.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;

import com.benben.bb.MyApplication;
import com.benben.bb.R;

public class FirstInMySignupDialog extends Dialog implements OnClickListener {

    private DisplayMetrics dm;

    public FirstInMySignupDialog(Context context) {
        this(context, R.style.CustomDialog_discovery);
    }

    public FirstInMySignupDialog(Context context, int themeId) {
        super(context, themeId);
        dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_first_mysignup);
        findViewById(R.id.close).setOnClickListener(this);
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
            case R.id.close:
                break;
        }
        dismiss();
    }
}
