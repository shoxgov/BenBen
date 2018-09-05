package com.benben.bb.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.UserInformationResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.RoundImageView;
import com.benben.bb.view.TitleBar;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-05-16.
 * email:shoxgov@126.com
 */

public class UserInfoActivity extends BaseActivity {

    @Bind(R.id.my_person_photo)
    RoundImageView photoImg;
    @Bind(R.id.my_person_nickname)
    TextView nicknameTv;
    @Bind(R.id.my_person_id)
    TextView idTv;
    @Bind(R.id.my_person_sex)
    TextView sexTv;
    @Bind(R.id.my_person_age)
    TextView ageTv;
    @Bind(R.id.my_person_tel)
    TextView telTv;
    @Bind(R.id.my_person_edu)
    TextView eduTv;
    @Bind(R.id.my_person_addr)
    TextView addrTv;
    @Bind(R.id.my_person_nation)
    TextView nationTv;
    @Bind(R.id.my_person_signature)
    TextView signatureTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        init();
        updateUserInfo(getIntent().getStringExtra("userId"));
    }

    private void init() {
        TitleBar titleLayout = (TitleBar) findViewById(R.id.titlelayout);
        titleLayout.setTitleBarListener(new TitleBarListener() {

            @Override
            public void rightClick() {
            }

            @Override
            public void leftClick() {
                finish();
            }
        });
        findViewById(R.id.my_person_qr_layout).setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void updateUserInfo(String userId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", userId);//id	用户ID
        params.put("code", "1");//获取非token
        OkHttpUtils.getAsyn(NetWorkConfig.GET_USERINFO, params, UserInformationResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                UserInformationResponse uir = (UserInformationResponse) br;
                if(uir.getCode() == 0){
                    ToastUtil.showText(uir.getMessage());
                    return;
                }
                if (uir.getData().getSex() == 1) {
                    sexTv.setText("女");
                } else {
                    sexTv.setText("男");
                }
                nicknameTv.setText(uir.getData().getNickName());
                idTv.setText(uir.getData().getBenbenNum());
                ageTv.setText(uir.getData().getAge());
                telTv.setText(uir.getData().getUserName());
                nationTv.setText(uir.getData().getNation() + "");
                eduTv.setText(uir.getData().getEducation() + "");
                addrTv.setText(uir.getData().getRegion() + "");
                signatureTv.setText(uir.getData().getSignature() + "");
                if (!TextUtils.isEmpty(uir.getData().getAvatar())) {
                    Glide.with(UserInfoActivity.this)
                            .load(uir.getData().getAvatar())
                            .placeholder(R.mipmap.default_image)
                            .error(R.mipmap.default_image)
                            .into(photoImg);
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
