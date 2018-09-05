package com.benben.bb.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.bb.MyApplication;
import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.activity.BandCardManagerActivity;
import com.benben.bb.activity.BrokerActivity;
import com.benben.bb.activity.BrokerBusinessStatisticsActivity;
import com.benben.bb.activity.BrokerMyResourceActivity;
import com.benben.bb.activity.BrokerMyteamActivity;
import com.benben.bb.activity.EnterpriseCertifyActivity;
import com.benben.bb.activity.EnterpriseCooperationActivity;
import com.benben.bb.activity.EnterpriseEmployeeActivity;
import com.benben.bb.activity.EnterpriseRecruitFragmentActivity;
import com.benben.bb.activity.MyInviteActivity;
import com.benben.bb.activity.MySignupActivity;
import com.benben.bb.activity.MyWalletActivity;
import com.benben.bb.activity.PersonInfoActivity;
import com.benben.bb.activity.PersonQrActivity;
import com.benben.bb.activity.RealNameCertifyActivity;
import com.benben.bb.activity.SettingActivity;
import com.benben.bb.adapter.PersonGridViewAdapter;
import com.benben.bb.adapter.PrivilegeGridViewAdapter;
import com.benben.bb.base.BaseFragment;
import com.benben.bb.bean.SettingItem;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.BecomeBrokerDialog;
import com.benben.bb.dialog.BecomeBrokerRunningDialog;
import com.benben.bb.dialog.BecomeEnterpriseDialog;
import com.benben.bb.dialog.RealnameCertifyDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.UserInfoResponse;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
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
    @Bind(R.id.my_consultant_layout)
    LinearLayout consultantLayout;
    @Bind(R.id.my_person_grid)
    GridView personGrid;
    @Bind(R.id.my_privilege_grid)
    GridView privilegeGrid;
    @Bind(R.id.my_person_photo)
    RoundImageView personPhoto;
    @Bind(R.id.my_person_truename)
    TextView personTruename;
    @Bind(R.id.my_person_grade)
    TextView personGrade;
    @Bind(R.id.my_privilege_title)
    TextView privilegeTitle;
    @Bind(R.id.my_privilege_hint)
    TextView privilegeHint;
    private PersonGridViewAdapter adapter1;
    private PrivilegeGridViewAdapter adapter2;

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
        adapter1 = new PersonGridViewAdapter(getActivity());
        personGrid.setAdapter(adapter1);
        initBaseInfo();
        personGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent person = new Intent();
                        person.setClass(getActivity(), PersonInfoActivity.class);
                        startActivityForResult(person, 12);
                        break;
                    case 1:
                        Intent realName = new Intent();
                        realName.setClass(getActivity(), RealNameCertifyActivity.class);
                        realName.putExtra("status", UserData.getUserData().getValidateStatus());
                        startActivity(realName);
                        break;
                    case 2:
                        Intent bandCard = new Intent();
                        bandCard.setClass(getActivity(), BandCardManagerActivity.class);
                        startActivityForResult(bandCard, 13);
                        break;
                    case 3:
                        Intent qr = new Intent();
                        qr.setClass(getActivity(), PersonQrActivity.class);
                        startActivity(qr);
                        break;
                }
            }
        });
        ///////
        adapter2 = new PrivilegeGridViewAdapter(getActivity());
        privilegeGrid.setAdapter(adapter2);
        privilegeGrid.setOnItemClickListener(onItemClickListener);
        freshUI();
    }

    private void initBaseInfo() {
        List<SettingItem> data = new ArrayList<>();
        data.add(new SettingItem(R.mipmap.my_info, "个人信息", "去完成"));
        switch (UserData.getUserData().getValidateStatus()) {//validateStatus 0未认证3认证中2认证失败1已通过
            case 1:
                data.add(new SettingItem(R.mipmap.my_realname_certify, "实名认证", "已认证"));
                break;
            case 0:
                data.add(new SettingItem(R.mipmap.my_realname_certify, "实名认证", "去完成"));
                break;
            case 3:
                data.add(new SettingItem(R.mipmap.my_realname_certify, "实名认证", "申核中"));
                break;
            case 2:
                data.add(new SettingItem(R.mipmap.my_realname_certify, "实名认证", "认证失败"));
                break;
        }
        if (TextUtils.isEmpty(UserData.getUserData().getBindingPayAccount())) {
            data.add(new SettingItem(R.mipmap.my_bindbank, "绑定银行卡", "去完成"));
        } else {
            data.add(new SettingItem(R.mipmap.my_bindbank, "绑定银行卡", "修改"));
        }
        data.add(new SettingItem(R.mipmap.my_share, "分享", "去完成"));
        adapter1.setData(data);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (UserData.getUserData().getIsAgent() > 0 && UserData.getUserData().getIsAgent() < 88) {
                switch (position) {
                    case 0:
                        Intent res = new Intent();
                        res.setClass(getActivity(), BrokerMyResourceActivity.class);
                        startActivity(res);
                        break;
                    case 1:
                        Intent statistics = new Intent();
                        statistics.setClass(getActivity(), BrokerBusinessStatisticsActivity.class);
                        startActivity(statistics);
                        break;
                    case 2:
                        Intent team = new Intent();
                        team.setClass(getActivity(), BrokerMyteamActivity.class);
                        startActivity(team);
                        break;
                    case 3:
                        Intent wallet = new Intent();
                        wallet.setClass(getActivity(), MyWalletActivity.class);
                        startActivity(wallet);
                        break;
                    case 4:
                        Intent mysignup = new Intent();
                        mysignup.setClass(getActivity(), MySignupActivity.class);
                        startActivity(mysignup);
                        break;
                    case 5:
                        goMyInvite();
                        break;
                }
            } else if (UserData.getUserData().getIsCompany() > 0 && UserData.getUserData().getIsCompany() < 88) {
                switch (position) {
                    case 0:
                        Intent info = new Intent();
                        info.setClass(getActivity(), EnterpriseCooperationActivity.class);
                        startActivity(info);
                        break;
                    case 1:
                        Intent recruit = new Intent();
                        recruit.setClass(getActivity(), EnterpriseRecruitFragmentActivity.class);
                        startActivity(recruit);
                        break;
                    case 2:
                        Intent setting = new Intent();
                        setting.setClass(getActivity(), EnterpriseEmployeeActivity.class);
                        startActivity(setting);
                        break;
                    case 3:
                        Intent wallet = new Intent();
                        wallet.setClass(getActivity(), MyWalletActivity.class);
                        startActivity(wallet);
                        break;
                    case 4:
                        Intent mysignup = new Intent();
                        mysignup.setClass(getActivity(), MySignupActivity.class);
                        startActivity(mysignup);
                        break;
                    case 5:
                        goMyInvite();
                        break;
                }
            } else {//普通用户
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
                        goMyInvite();
                        break;
                }
            }
        }
    };

    private void goMyInvite() {
        Intent invite = new Intent();
        invite.setClass(getActivity(), MyInviteActivity.class);
        startActivity(invite);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            getActivity().finish();
        } else if (requestCode == 12 && resultCode == RESULT_OK) {
            updateUserInfo();
        } else if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            Utils.updateUserInfomation();
        } else if (requestCode == 13 && resultCode == Activity.RESULT_OK) {
            initBaseInfo();
        }
    }

    @Override
    protected int provieResourceID() {
        return R.layout.fragment_my;
    }

    @Override
    protected void init() {
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

    /**
     * 更新用户信息
     */
    private void updateUserInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", UserData.getUserData().getId() + "");//id	用户ID
        OkHttpUtils.getAsyn(NetWorkConfig.GET_USERINFO, params, UserInfoResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                try {
                    UserInfoResponse ui = (UserInfoResponse) br;
                    if (ui.getCode() == 1) {
                        UserData.updateUserInfo(ui.getData().getUser());
                        UserData.getUserData().setToken(ui.getData().getToken());
                        freshUI();
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

    public void freshUI() {
        if (!TextUtils.isEmpty(UserData.getUserData().getAvatar())) {
            Glide.with(getActivity())
                    .load(UserData.getUserData().getAvatar())
                    .placeholder(R.mipmap.default_image)
                    .error(R.mipmap.default_image)
                    .into(personPhoto);
        }
        if (!TextUtils.isEmpty(UserData.getUserData().getNickName())) {
            personTruename.setText(MyApplication.userData.getNickName());
            personGrade.setText(getVip());
        }
        if (UserData.getUserData().getIsAgent() > 0 && UserData.getUserData().getIsAgent() < 88) {
            privilegeTitle.setText("尊贵特权");
            List<SettingItem> data2 = new ArrayList<>();
            data2.add(new SettingItem(R.mipmap.my_human, "人力资源", ""));
            data2.add(new SettingItem(R.mipmap.my_statistic, "就业统计", ""));
            data2.add(new SettingItem(R.mipmap.my_secretary, "助理顾问", ""));
            data2.add(new SettingItem(R.mipmap.my_wallet, "我的钱包", ""));
            data2.add(new SettingItem(R.mipmap.my_signup, "我的报名", ""));
            data2.add(new SettingItem(R.mipmap.my_invite, "我的邀请", ""));
            adapter2.setData(data2);
            privilegeGrid.setVisibility(View.VISIBLE);
            privilegeHint.setVisibility(View.GONE);
            consultantLayout.setVisibility(View.GONE);
        } else if (UserData.getUserData().getIsCompany() > 0 && UserData.getUserData().getIsCompany() < 88) {
            privilegeTitle.setText("尊贵特权");
            List<SettingItem> data2 = new ArrayList<>();
            data2.add(new SettingItem(R.mipmap.my_enterprise_info, "企业信息", ""));
            data2.add(new SettingItem(R.mipmap.my_employ, "招聘岗位", ""));
            data2.add(new SettingItem(R.mipmap.my_employee, "应聘人员", ""));
            data2.add(new SettingItem(R.mipmap.my_wallet, "我的钱包", ""));
            data2.add(new SettingItem(R.mipmap.my_signup, "我的报名", ""));
            data2.add(new SettingItem(R.mipmap.my_invite, "我的邀请", ""));
            adapter2.setData(data2);
            privilegeGrid.setVisibility(View.VISIBLE);
            privilegeHint.setVisibility(View.GONE);
            consultantLayout.setVisibility(View.GONE);
        } else {//普通用户
            if (UserData.getUserData().getValidateStatus() == 1) {
                privilegeTitle.setText("尊贵特权");
                List<SettingItem> data2 = new ArrayList<>();
                data2.add(new SettingItem(R.mipmap.my_wallet, "我的钱包", ""));
                data2.add(new SettingItem(R.mipmap.my_signup, "我的报名", ""));
                data2.add(new SettingItem(R.mipmap.my_invite, "我的邀请", ""));
                adapter2.setData(data2);
                privilegeGrid.setVisibility(View.VISIBLE);
                privilegeHint.setVisibility(View.GONE);
            } else {
                privilegeTitle.setText("会员特权");
                privilegeGrid.setVisibility(View.GONE);
                privilegeHint.setVisibility(View.VISIBLE);
            }
            consultantLayout.setVisibility(View.VISIBLE);
        }

    }

    private String getVip() {
        if (UserData.getUserData().getIsAgent() > 0 && UserData.getUserData().getIsAgent() < 88) {
            return "就业顾问";
        } else if (UserData.getUserData().getIsCompany() > 0 && UserData.getUserData().getIsCompany() < 88) {
            return "企业人";
        } else {//普通用户
            return "普通用户";
        }
    }

    @OnClick({R.id.my_person_layout, R.id.my_person_setting, R.id.my_person_broker, R.id.my_person_enterprise})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_person_layout:
                Intent person = new Intent();
                person.setClass(getActivity(), PersonInfoActivity.class);
                startActivityForResult(person, 12);
                break;
            case R.id.my_person_setting:
                Intent setting = new Intent();
                setting.setClass(getActivity(), SettingActivity.class);
                startActivityForResult(setting, 10);
                break;
            case R.id.my_person_broker:
                if (UserData.getUserData().getValidateStatus() == 3 || UserData.getUserData().getIsAgent() == 99 || UserData.getUserData().getIsAgent() == 88) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", UserData.getUserData().getId() + "");//id	用户ID
                    OkHttpUtils.getAsyn(NetWorkConfig.GET_USERINFO, params, UserInfoResponse.class, new HttpCallback() {
                        @Override
                        public void onSuccess(BaseResponse br) {
                            super.onSuccess(br);
                            try {
                                UserInfoResponse ui = (UserInfoResponse) br;
                                UserData.updateUserInfo(ui.getData().getUser());
                                UserData.getUserData().setToken(ui.getData().getToken());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            brokerClick();
                        }

                        @Override
                        public void onFailure(int code, String message) {
                            super.onFailure(code, message);
                            brokerClick();
                        }
                    });
                } else {
                    brokerClick();
                }
                break;
            case R.id.my_person_enterprise:
                if (UserData.getUserData().getValidateStatus() == 3 || UserData.getUserData().getIsCompany() == 99 || UserData.getUserData().getIsCompany() == 88) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", UserData.getUserData().getId() + "");//id	用户ID
                    OkHttpUtils.getAsyn(NetWorkConfig.GET_USERINFO, params, UserInfoResponse.class, new HttpCallback() {
                        @Override
                        public void onSuccess(BaseResponse br) {
                            super.onSuccess(br);
                            try {
                                UserInfoResponse ui = (UserInfoResponse) br;
                                UserData.updateUserInfo(ui.getData().getUser());
                                UserData.getUserData().setToken(ui.getData().getToken());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            enterpriseClick();
                        }

                        @Override
                        public void onFailure(int code, String message) {
                            super.onFailure(code, message);
                            enterpriseClick();
                        }
                    });
                } else {
                    enterpriseClick();
                }
                break;
        }
    }

    private void enterpriseClick() {
        switch (UserData.getUserData().getValidateStatus()) {//validateStatus 0未认证1已通过2认证失败3认证中
            case 2:
                ToastUtil.showText("上次提交的实名认证申核失败，需要重新认证");
            case 0:
                RealnameCertifyDialog realnameCertifyDialog = new RealnameCertifyDialog(getActivity(), "认证为企业人需先完成实名认证", new DialogCallBack() {
                    @Override
                    public void OkDown(Object obj) {
                        Intent realname = new Intent();
                        realname.setClass(getActivity(), RealNameCertifyActivity.class);
                        realname.putExtra("status", UserData.getUserData().getValidateStatus());
                        startActivityForResult(realname, 11);
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                realnameCertifyDialog.show();
                return;
            case 3:
                BecomeBrokerRunningDialog dialog2 = new BecomeBrokerRunningDialog(getActivity(), "实名认证正在审核中", new DialogCallBack() {
                    @Override
                    public void OkDown(Object obj) {

                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                dialog2.show();
                return;
        }
        int agent2 = UserData.getUserData().getIsAgent();
        if (agent2 > 0 && agent2 < 88) {
            ToastUtil.showText("您已经认证为就业顾问将不能认证成为企业");
            return;
        }
        final int company = UserData.getUserData().getIsCompany();
        switch (company) {
            case 88://申请失败
                ToastUtil.showText("上次提交的企业人认证申核失败，是否重新认证");
            case 0://未认证
                BecomeEnterpriseDialog dialog = new BecomeEnterpriseDialog(getActivity(), "是否认证为企业人", new DialogCallBack() {
                    @Override
                    public void OkDown(Object obj) {
                        Intent enterprise = new Intent();
                        enterprise.setClass(getActivity(), EnterpriseCertifyActivity.class);
                        startActivityForResult(enterprise, 11);
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                dialog.show();
                break;
            case 99://申请中
                BecomeBrokerRunningDialog dialog2 = new BecomeBrokerRunningDialog(getActivity(), new DialogCallBack() {
                    @Override
                    public void OkDown(Object obj) {
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                dialog2.show();
                break;
            default://大于0 小于88 都是就业顾问
                if (company > 0 && company < 88) {
                    freshUI();
//                    Intent enterprise = new Intent();
//                    enterprise.setClass(getActivity(), MyEnterpriseActivity.class);
//                    enterprise.putExtra("status", company);
//                    startActivity(enterprise);
                }
                break;
        }
    }

    private void brokerClick() {
        switch (UserData.getUserData().getValidateStatus()) {//validateStatus 0未认证1已通过2认证失败3认证中
            case 2:
                ToastUtil.showText("上次提交的实名认证申核失败，需要重新认证");
            case 0:
                RealnameCertifyDialog realnameCertifyDialog = new RealnameCertifyDialog(getActivity(), "认证为就业顾问需先完成实名认证", new DialogCallBack() {
                    @Override
                    public void OkDown(Object obj) {
                        Intent realname = new Intent();
                        realname.setClass(getActivity(), RealNameCertifyActivity.class);
                        realname.putExtra("status", UserData.getUserData().getValidateStatus());
                        startActivityForResult(realname, 11);
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                realnameCertifyDialog.show();
                return;
            case 3:
                BecomeBrokerRunningDialog dialog2 = new BecomeBrokerRunningDialog(getActivity(), "实名认证正在审核中", new DialogCallBack() {
                    @Override
                    public void OkDown(Object obj) {
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                dialog2.show();
                return;
        }
        final int agent = UserData.getUserData().getIsAgent();
        switch (agent) {
            case 88://申请失败
                ToastUtil.showText("上次提交的就业顾问认证申核失败，是否重新认证");
            case 0://未认证
                String info = "1.免费认证\n" +
                        "通过【我的】-【二维码】-【分享】-成功邀请10位好友加入职犇犇";
//                        + "2.收费认证\n" + "缴纳认证费 200.00元";
                BecomeBrokerDialog dialog = new BecomeBrokerDialog(getActivity(), info, new DialogCallBack() {
                    @Override
                    public void OkDown(Object obj) {
                        OkHttpUtils.getAsyn(NetWorkConfig.BROKER_CHECK_AGENTS, BaseResponse.class, new HttpCallback() {
                            @Override
                            public void onSuccess(BaseResponse br) {
                                super.onSuccess(br);
                                if (br.getCode() == 1) {
                                    ToastUtil.showText("申请就业顾问已提交，申核需要1-2个工作日");
                                    Utils.updateUserInfomation();
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

                    @Override
                    public void CancleDown() {

                    }
                });
                dialog.show();
                break;
            case 99://申请中
                BecomeBrokerRunningDialog dialog2 = new BecomeBrokerRunningDialog(getActivity(), new DialogCallBack() {
                    @Override
                    public void OkDown(Object obj) {
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                dialog2.show();
                break;
            default://大于0 小于88 都是就业顾问
                if (agent > 0 && agent < 88) {
                    freshUI();
//                    Intent broker = new Intent();
//                    broker.setClass(getActivity(), BrokerActivity.class);
//                    startActivity(broker);
                }
                break;

        }
    }
}