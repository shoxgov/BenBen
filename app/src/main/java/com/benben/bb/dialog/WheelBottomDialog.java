package com.benben.bb.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.benben.bb.MyApplication;
import com.benben.bb.R;
import com.benben.bb.adapter.BottomSelectAdapter;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.utils.Utils;

public class WheelBottomDialog extends Dialog {

    private String[] data;
    private Context context;
    private DialogCallBack dialogcallback;
    private String cancle;

    public WheelBottomDialog(Context context, String[] data, DialogCallBack dialogcallback) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
        this.data = data;
        this.dialogcallback = dialogcallback;
    }

    public WheelBottomDialog(Context context, String[] data, String cancle, DialogCallBack dialogcallback) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
        this.cancle = cancle;
        this.data = data;
        this.dialogcallback = dialogcallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_bottom_wheel);
        setCanceledOnTouchOutside(false);
        ListView list = (ListView) findViewById(R.id.dialog_wheel_list);
        Button cancleBtn = (Button) findViewById(R.id.dialog_wheel_cancle);
        BottomSelectAdapter adapter = new BottomSelectAdapter(context);
        list.setAdapter(adapter);
        adapter.setData(data);
        if (!TextUtils.isEmpty(cancle)) {
            cancleBtn.setText(cancle);
        }
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        //设置Dialog从窗体底部弹出
        window.setLayout(MyApplication.screenWidthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                if (dialogcallback != null) {
                    dialogcallback.OkDown(position);
                }
                dismiss();
            }
        });
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

}
