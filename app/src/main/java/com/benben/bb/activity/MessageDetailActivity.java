package com.benben.bb.activity;

import android.os.Bundle;

import com.benben.bb.R;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.view.TitleBar;

import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-05-17.
 * email:shoxgov@126.com
 */

public class MessageDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_detail);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        TitleBar titleBar = (TitleBar) findViewById(R.id.titlelayout);
        titleBar.setTitleName("黄三");
        titleBar.setTitleBarListener(new TitleBarListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
