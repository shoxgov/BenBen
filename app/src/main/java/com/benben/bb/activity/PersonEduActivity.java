package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.WheelBottomDialog;
import com.benben.bb.imp.DialogCallBack;
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

public class PersonEduActivity extends BaseActivity {
    private static final String[] HIGH_ARRAYS = new String[]{"硕士", "本科", "专科", "高中", "其它"};
    private static final String[] STATUS_ARRAYS = new String[]{"在读", "毕业"};//0是在读 1是毕业

    @Bind(R.id.my_person_edu_high)
    TextView highTv;
    @Bind(R.id.my_person_edu_schoolname)
    EditText schoolnameEdit;
    @Bind(R.id.my_person_edu_special)
    EditText specialEdit;
    @Bind(R.id.my_person_edu_status)
    TextView statusTv;

    private int high, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_edu);
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
        if (!TextUtils.isEmpty(UserData.getUserData().getFinishSchool())) {
            schoolnameEdit.setText(UserData.getUserData().getFinishSchool());
        }
        if (!TextUtils.isEmpty(UserData.getUserData().getMajor())) {
            specialEdit.setText(UserData.getUserData().getMajor());
        }
        try {
            highTv.setText(UserData.getUserData().getEducation());
            int i = 0;
            for(String h:HIGH_ARRAYS){
                if(h.equals(UserData.getUserData().getEducation())){
                    high = i;
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            status = UserData.getUserData().getStudyStatus();
            statusTv.setText(STATUS_ARRAYS[UserData.getUserData().getStudyStatus()]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.my_person_edu_high, R.id.my_person_edu_status, R.id.my_person_edu_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_person_edu_high:
                WheelBottomDialog sexDla = new WheelBottomDialog(this, HIGH_ARRAYS, new DialogCallBack() {
                    @Override
                    public void OkDown(Object score) {
                        int position = (int) score;
                        highTv.setText(HIGH_ARRAYS[position]);
                        high = position;
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                sexDla.show();
                break;
            case R.id.my_person_edu_status:
                final WheelBottomDialog statusDla = new WheelBottomDialog(this, STATUS_ARRAYS, new DialogCallBack() {
                    @Override
                    public void OkDown(Object score) {
                        int position = (int) score;
                        statusTv.setText(STATUS_ARRAYS[position]);
                        status = position;
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                statusDla.show();
                break;
            case R.id.my_person_edu_ok:
                final String school = schoolnameEdit.getText().toString();
                final String major = specialEdit.getText().toString();
                if (TextUtils.isEmpty(school) || TextUtils.isEmpty(major)) {
                    ToastUtil.showText("请填写学校信息");
                    return;
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserData.getUserData().getId() + "");
                params.put("finishSchool", school);
                params.put("education", HIGH_ARRAYS[high]);
                params.put("major", major);
                params.put("studyStatus", status + "");
                OkHttpUtils.postAsynFile(NetWorkConfig.USER_UPDATEINFO, "", null, params, BaseResponse.class, new HttpCallback() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        super.onSuccess(baseResponse);
                        if (baseResponse.getCode() == 1) {
                            UserData.getUserData().setFinishSchool(school);
                            UserData.getUserData().setEducation(HIGH_ARRAYS[high]);
                            UserData.getUserData().setMajor(major);
                            UserData.getUserData().setStudyStatus(status);
                            Intent rs = new Intent();
                            rs.putExtra("result", highTv.getText().toString());
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
