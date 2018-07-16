package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;

import com.benben.bb.MyApplication;
import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-17.
 * email:shoxgov@126.com
 */

public class PersonSexCheckActivity extends BaseActivity {


    @Bind(R.id.my_person_sex_group_man)
    RadioButton sexMan;
    @Bind(R.id.my_person_sex_group_woman)
    RadioButton sexWoman;
    private String sex = "";//0 男 1 女

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_sex);
        ButterKnife.bind(this);
        sex = getIntent().getStringExtra("sex");
        init();
    }

    private void init() {
        TitleBar titleBar = (TitleBar) findViewById(R.id.titlelayout);
        titleBar.setTitleBarListener(new TitleBarListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        if (sex.equals("男")) {
            sexMan.setChecked(true);
            sexWoman.setChecked(false);
        } else if (sex.equals("女")) {
            sexMan.setChecked(false);
            sexWoman.setChecked(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.my_person_sex_group_man_layout, R.id.my_person_sex_group_woman_layout, R.id.my_person_sex_ok})
    public void onViewClicked(View v) {

        switch (v.getId()) {
            case R.id.my_person_sex_group_man_layout:
                sexMan.setChecked(true);
                sexWoman.setChecked(false);
                break;
            case R.id.my_person_sex_group_woman_layout:
                sexMan.setChecked(false);
                sexWoman.setChecked(true);
                break;

            case R.id.my_person_sex_ok:
                int result = 0;//0保密1男2女
                if (sexWoman.isChecked()) {
                    sex = "女";
                    result = 1;
                }
                if (sexMan.isChecked()) {
                    sex = "男";
                    result = 0;
                }
                if (TextUtils.isEmpty(sex)) {
                    return;
                }

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserData.getUserData().getId() + "");
                params.put("sex", "" + result);
                OkHttpUtils.postAsynFile(NetWorkConfig.USER_UPDATEINFO, "", null, params, BaseResponse.class, new HttpCallback() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        super.onSuccess(baseResponse);
                        if (baseResponse.getCode() == 1) {
                            if (sex.equals("男")) {
                                MyApplication.userData.setSex(0);
                            } else {
                                MyApplication.userData.setSex(1);
                            }
                            Intent rs = new Intent();
                            rs.putExtra("result", sex);
                            setResult(RESULT_OK, rs);
                            finish();
                        }
                        ToastUtil.showText(baseResponse.getMessage());

                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        ToastUtil.showText(message);
                    }
                });

                break;
        }
    }

}
