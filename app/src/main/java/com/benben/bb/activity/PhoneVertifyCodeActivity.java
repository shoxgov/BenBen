package com.benben.bb.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.benben.bb.R;
import com.benben.bb.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-07-30.
 * email:shoxgov@126.com
 */

public class PhoneVertifyCodeActivity extends BaseActivity {


    @Bind(R.id.findpwd_vertify_code_1)
    EditText codeEdit1;
    @Bind(R.id.findpwd_vertify_code_2)
    EditText codeEdit2;
    @Bind(R.id.findpwd_vertify_code_3)
    EditText codeEdit3;
    @Bind(R.id.findpwd_vertify_code_4)
    EditText codeEdit4;
    @Bind(R.id.findpwd_vertify_code_5)
    EditText codeEdit5;
    @Bind(R.id.findpwd_vertify_code_6)
    EditText codeEdit6;
    @Bind(R.id.findpwd_vertify_ok)
    Button okBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_code);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void init() {
        codeEdit1.addTextChangedListener(new JumpTextWatcher(codeEdit1,codeEdit2));
        codeEdit2.addTextChangedListener(new JumpTextWatcher(codeEdit2,codeEdit3));
        codeEdit3.addTextChangedListener(new JumpTextWatcher(codeEdit3,codeEdit4));
        codeEdit4.addTextChangedListener(new JumpTextWatcher(codeEdit4,codeEdit5));
        codeEdit5.addTextChangedListener(new JumpTextWatcher(codeEdit5,codeEdit6));
    }

    public class JumpTextWatcher implements TextWatcher {
        private EditText mThisView = null;
        private View mNextView = null;

        public JumpTextWatcher(EditText vThis, View vNext) {
            super();
            mThisView = vThis;
            if (vNext != null) {
                mNextView = vNext;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            if (!TextUtils.isEmpty(str) && str.length() == 1) {
                //如果发现输入回车符或换行符，替换为空字符
                if (mNextView != null) {
                    //如果跳转控件不为空，让下一个控件获得焦点，此处可以直接实现登录功能
                    mNextView.requestFocus();
                    if (mNextView instanceof EditText) {
                        EditText et = (EditText) mNextView;
                        //如果跳转控件为EditText，让光标自动移到文本框文字末尾
                        et.setSelection(et.getText().length());
                    }
                    if(mNextView.getId() == R.id.findpwd_vertify_code_6){
                        Utils.closeInputMethod(PhoneVertifyCodeActivity.this);
                        okBtn.requestFocus();
                    }
                }
            }
        }
    }
}
