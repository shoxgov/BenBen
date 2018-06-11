package com.benben.bb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.benben.bb.MyApplication;
import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.activity.MySignupActivity;
import com.benben.bb.activity.MyWalletActivity;
import com.benben.bb.activity.PersonInfoActivity;
import com.benben.bb.activity.PersonQrActivity;
import com.benben.bb.activity.SettingActivity;
import com.benben.bb.adapter.SettingAdapter;
import com.benben.bb.base.BaseFragment;
import com.benben.bb.bean.SettingItem;
import com.benben.bb.bean.UserData;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.LoginResponse;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.PreferenceUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.RoundImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class MyFragment extends BaseFragment {
    private static final String ARG = "arg";
    @Bind(R.id.my_setting_list)
    ListView settingList;
    @Bind(R.id.my_person_photo)
    RoundImageView personPhoto;
    @Bind(R.id.my_person_truename)
    TextView personTruename;
    @Bind(R.id.my_person_signature)
    TextView personSignature;
    private SettingAdapter adapter;

    public MyFragment() {
        LogUtil.d("MyFragment non-parameter constructor");
    }

    public static MyFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        MyFragment fragment = new MyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new SettingAdapter(getActivity());
        settingList.setAdapter(adapter);
        List<SettingItem> data = new ArrayList<>();
        data.add(new SettingItem(R.mipmap.my_wallet, "钱包", ""));
        data.add(new SettingItem(R.mipmap.my_signup, "我的报名", ""));
        data.add(new SettingItem(R.mipmap.my_setting, "设置", ""));
        adapter.setData(data);
        settingList.setOnItemClickListener(onItemClickListener);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    Intent wallet = new Intent();
                    wallet.setClass(getActivity(), MyWalletActivity.class);
                    startActivity(wallet);
                    break;
                case 1:
                    Intent mysignup = new Intent();
                    mysignup.setClass(getActivity(), MySignupActivity.class);
                    startActivity(mysignup);
                    break;
                case 2:
                    Intent setting = new Intent();
                    setting.setClass(getActivity(), SettingActivity.class);
                    startActivityForResult(setting, 11);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            getActivity().finish();
        } else if (requestCode == 12 && resultCode == RESULT_OK) {
            updateUserInfo();
        }
    }

    @Override
    protected int provieResourceID() {
        return R.layout.fragment_my;
    }

    @Override
    protected void init() {
        if (!TextUtils.isEmpty(UserData.getUserData().getAvatar())) {
            Glide.with(getActivity())
                    .load(UserData.getUserData().getAvatar())
                    .placeholder(R.mipmap.default_image)
                    .error(R.mipmap.default_image)
                    .into(personPhoto);
        }
        if (!TextUtils.isEmpty(UserData.getUserData().getNickName())) {
            personTruename.setText(MyApplication.userData.getNickName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.my_person_layout, R.id.my_person_qr})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.my_person_layout:
                Intent person = new Intent();
                person.setClass(getActivity(), PersonInfoActivity.class);
                startActivityForResult(person, 12);
                break;
            case R.id.my_person_qr:
                Intent qr = new Intent();
                qr.setClass(getActivity(), PersonQrActivity.class);
                startActivity(qr);
                break;
        }
    }

    private void updateUserInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", PreferenceUtil.getString("LoginTel", ""));
        params.put("logCode", PreferenceUtil.getString("LoginCode", ""));
        OkHttpUtils.getAsyn(NetWorkConfig.LOGIN, params, LoginResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    LoginResponse lr = (LoginResponse) resultDesc;
                    UserData.updateAccount(lr);
                    if (!TextUtils.isEmpty(UserData.getUserData().getAvatar())) {
                        Glide.with(getActivity())
                                .load(UserData.getUserData().getAvatar())
                                .into(personPhoto);
                    }
                    if (!TextUtils.isEmpty(UserData.getUserData().getNickName())) {
                        personTruename.setText(MyApplication.userData.getNickName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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