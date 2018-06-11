package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.dialog.AppealSuccessDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.HomeBannerResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-18.
 * email:shoxgov@126.com
 */

public class MySalaryListDetailActivity extends BaseActivity {

    @Bind(R.id.salary_history_detail_positionname)
    TextView positionnameTv;
    @Bind(R.id.salary_history_detail_base)
    TextView baseTv;
    @Bind(R.id.salary_history_detail_total)
    TextView totalTv;
    @Bind(R.id.salary_history_detail_username)
    TextView usernameTv;
    @Bind(R.id.salary_history_detail_factgive)
    TextView factgiveTv;
    @Bind(R.id.salary_history_detail_workhour)
    TextView workhourTv;
    @Bind(R.id.salary_history_detail_deduck)
    TextView deduckTv;
    @Bind(R.id.salary_history_detail_award)
    TextView awardTv;
    @Bind(R.id.salary_history_detail_soso)
    TextView sosoTv;
    @Bind(R.id.salary_history_detail_why_edit)
    EditText whyEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysalary_list_detail);
        ButterKnife.bind(this);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.salary_history_detail_apply)
    public void onViewClicked() {
        Map<String, String> params = new HashMap<String, String>();
//        params.put("pageNum", pageNo + "");
//        params.put("pageSize", pageSize + "");
        OkHttpUtils.getAsyn(NetWorkConfig.USER_SALARY_LIST_APPEAL, BaseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                if (br.getCode() == 1) {
                    AppealSuccessDialog dialog = new AppealSuccessDialog(MySalaryListDetailActivity.this, new DialogCallBack() {
                        @Override
                        public void OkDown(Object obj) {

                        }

                        @Override
                        public void CancleDown() {

                        }
                    });
                    dialog.show();
                }
                ToastUtil.showText(br.getMessage());
            }
                @Override
                public void onFailure ( int code, String message){
                    super.onFailure(code, message);
                    ToastUtil.showText(message);
                }
            });
        }
    }
