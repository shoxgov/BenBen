package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

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

public class PersonRefValueActivity extends BaseActivity {

    @Bind(R.id.my_person_ref_name)
    TextView nameTextView;
    @Bind(R.id.my_person_ref_value)
    EditText nameEditValue;
    private String title, nameTv, nameValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_ref);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra("title");
        nameTv = getIntent().getStringExtra("nameTv");
        nameValue = getIntent().getStringExtra("nameValue");
        init();
    }

    private void init() {
        TitleBar titleBar = (TitleBar) findViewById(R.id.titlelayout);
        titleBar.setTitleName(title);
        titleBar.setTitleBarListener(new TitleBarListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        nameTextView.setText(nameTv);
        nameEditValue.setText(nameValue);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.my_person_ref_ok)
    public void onViewClicked() {
        final String result = nameEditValue.getText().toString();
        if (TextUtils.isEmpty(result)) {
            ToastUtil.showText("请输入" + nameTv);
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", UserData.getUserData().getId()+"");
        if (title.equals("昵称")) {
            params.put("nickName", result);
        } else if (title.equals("年龄")) {
            params.put("age", result);
        } else if (title.equals("民族")) {
            params.put("nation", result);
        } else if (title.equals("个性签名")) {
            params.put("signature", result);
        }

        OkHttpUtils.postAsynFile(NetWorkConfig.USER_UPDATEINFO, "", null, params, BaseResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                super.onSuccess(baseResponse);
                if (baseResponse.getCode() == 1) {
                    if (title.equals("昵称")) {
                        UserData.getUserData().setNickName(result);
                    } else if (title.equals("年龄")) {
                        UserData.getUserData().setAge(result);
                    } else if (title.equals("民族")) {
                        UserData.getUserData().setNation(result);
                    } else if (title.equals("个性签名")) {
                        UserData.getUserData().setSignature(result);
                    }
//                    MyApplication.userData = UserData.getUserData();
                    Intent rs = new Intent();
                    rs.putExtra("result", result);

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
    }
}
