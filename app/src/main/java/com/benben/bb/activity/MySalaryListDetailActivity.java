package com.benben.bb.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.AppealSuccessDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.TitleBar;

import java.util.HashMap;
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
    private int id;
    private float salary, factMoney, workHours, buckleMoney, rewardMoney;
    private String buckleDetails, wagesTitle, complain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysalary_list_detail);
        id = getIntent().getIntExtra("id", 0);
        salary = getIntent().getFloatExtra("salary", 0);
        factMoney = getIntent().getFloatExtra("factMoney", 0);
        workHours = getIntent().getFloatExtra("workHours", 0);
        buckleMoney = getIntent().getFloatExtra("buckleMoney", 0);
        rewardMoney = getIntent().getFloatExtra("rewardMoney", 0);
        buckleDetails = getIntent().getStringExtra("buckleDetails");
        wagesTitle = getIntent().getStringExtra("wagesTitle");
        complain = getIntent().getStringExtra("complain");
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

        positionnameTv.setText(wagesTitle);
        baseTv.setText("基本薪资：" + salary + "/小时");
        usernameTv.setText(UserData.getUserData().getTrueName() + "    " + UserData.getUserData().getUserName());
        factgiveTv.setText("实发：" + factMoney + "元");
        workhourTv.setText(workHours + "");
        deduckTv.setText(buckleMoney + "");
        awardTv.setText(rewardMoney + "");
        if (TextUtils.isEmpty(buckleDetails)) {
            sosoTv.setText("无");
        } else {
            sosoTv.setText(buckleDetails);
        }
        if (!TextUtils.isEmpty(complain)) {
            whyEdit.setText(complain);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.salary_history_detail_apply)
    public void onViewClicked() {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        String temp = whyEdit.getText().toString();
        if (TextUtils.isEmpty(temp)) {
            ToastUtil.showText("请输入申诉内容");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id + "");
        params.put("complain", temp);
        OkHttpUtils.getAsyn(NetWorkConfig.USER_SALARY_LIST_APPEAL, params, BaseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                if (br.getCode() == 1) {
                    AppealSuccessDialog dialog = new AppealSuccessDialog(MySalaryListDetailActivity.this, new DialogCallBack() {
                        @Override
                        public void OkDown(Object obj) {
                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        public void CancleDown() {

                        }
                    });
                    dialog.show();
                } else {
                    ToastUtil.showText(br.getMessage());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText(message);
            }
        });
    }
}
