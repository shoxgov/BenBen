package com.benben.bb.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.benben.bb.MyApplication;
import com.benben.bb.R;
import com.benben.bb.imp.DialogCallBack;

public class EnterpriseSalarySuccessDialog extends Dialog implements OnClickListener {

    private String title, bank;
    private DialogCallBack dialogcallback;
    private DisplayMetrics dm;


    public EnterpriseSalarySuccessDialog(Context context, String title, String bank, DialogCallBack dialogcallback) {
        this(context, R.style.CustomDialog_discovery);
        this.title = title;
        this.bank = bank;
        this.dialogcallback = dialogcallback;
    }

    public EnterpriseSalarySuccessDialog(Context context, int themeId) {
        super(context, themeId);
        dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_enterprise_salary_success);
        findViewById(R.id.okBtn).setOnClickListener(this);
        TextView titleTv = (TextView) findViewById(R.id.dialog_enterprise_salary_success_title);
        TextView bankTv = (TextView) findViewById(R.id.dialog_enterprise_salary_success_bank);
        titleTv.setText("已为您生成了XOXO的工时统计表".replace("XOXO", title));
        bankTv.setText(bank);
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
        if (dialogcallback != null) {
            dialogcallback.OkDown(null);
        }
    }

    @Override
    public void onBackPressed() {
        dismiss();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.okBtn:

                break;
        }
        dismiss();
    }
}
