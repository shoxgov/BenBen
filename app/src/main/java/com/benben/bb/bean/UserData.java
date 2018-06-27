package com.benben.bb.bean;

import android.text.TextUtils;

import com.benben.bb.MyApplication;
import com.benben.bb.okhttp3.response.LoginResponse;
import com.benben.bb.okhttp3.response.UserInfomation;

/**
 * Created by wangshengyin on 2017-03-15.
 * email:shoxgov@126.com
 */

public class UserData {
    /**
     * 用户个人信息的数据
     */

    public void clear() {
        MyApplication.userData = new AccountInfoData();
    }

    public static AccountInfoData getUserData() {
        if (MyApplication.userData == null) {
            return new AccountInfoData();
        }
        return MyApplication.userData;
    }

    public static void updateAccount(LoginResponse lr) {
        ////////////////////////////////
        if (lr == null) {
            return;
        }
        if (!TextUtils.isEmpty(lr.getData().getToken())) {
            getUserData().setToken(lr.getData().getToken());
        }
        updateUserInfo(lr.getData().getUser());
        ///////////////////////////////////
        //save to local
        //////////////////////////////////

    }

    public static void updateUserInfo(UserInfomation ui) {
        if (ui == null) {
            return;
        }
        if (!TextUtils.isEmpty(ui.getAvatar())) {
            getUserData().setAvatar(ui.getAvatar());
        }
        if (ui.getId() > 0) {
            getUserData().setId(ui.getId());
        }
        getUserData().setStudyStatus(ui.getStudyStatus());
        getUserData().setSex(ui.getSex());
        getUserData().setBalance(ui.getBalance());
        getUserData().setIsAgent(ui.getIsAgent());
        getUserData().setIsCompany(ui.getIsCompany());
        getUserData().setValidateStatus(ui.getValidateStatus());
        getUserData().setWalletStatus(ui.getWalletStatus());
        if (!TextUtils.isEmpty(ui.getPassWord())) {
            getUserData().setPassWord(ui.getPassWord());
        }
        if (!TextUtils.isEmpty(ui.getBenbenNum())) {
            getUserData().setBenbenNum(ui.getBenbenNum());
        }
        if (!TextUtils.isEmpty(ui.getSignature())) {
            getUserData().setSignature(ui.getSignature());
        }
        if (!TextUtils.isEmpty(ui.getNation())) {
            getUserData().setNation(ui.getNation());
        }
        if (!TextUtils.isEmpty(ui.getMajor())) {
            getUserData().setMajor(ui.getMajor());
        }
        if (!TextUtils.isEmpty(ui.getEducation())) {
            getUserData().setEducation(ui.getEducation());
        }
        if (!TextUtils.isEmpty(ui.getAge())) {
            getUserData().setAge(ui.getAge());
        }
        if (!TextUtils.isEmpty(ui.getRegion())) {
            getUserData().setRegion(ui.getRegion());
        }
        if (!TextUtils.isEmpty(ui.getUserName())) {
            getUserData().setUserName(ui.getUserName());
        }
        if (!TextUtils.isEmpty(ui.getNickName())) {
            getUserData().setNickName(ui.getNickName());
        }
        if (!TextUtils.isEmpty(ui.getTrueName())) {
            getUserData().setTrueName(ui.getTrueName());
        }
        if (!TextUtils.isEmpty(ui.getFinishSchool())) {
            getUserData().setFinishSchool(ui.getFinishSchool());
        }
        if (!TextUtils.isEmpty(ui.getIdcardZheng())) {
            getUserData().setIdcardZheng(ui.getIdcardZheng());
        }
        if (!TextUtils.isEmpty(ui.getIdcardFan())) {
            getUserData().setIdcardFan(ui.getIdcardFan());
        }
        if (!TextUtils.isEmpty(ui.getIdentityCard())) {
            getUserData().setIdentityCard(ui.getIdentityCard());
        }
        if (!TextUtils.isEmpty(ui.getBindingPaytype())) {
            getUserData().setBindingPaytype(ui.getBindingPaytype());
        }
        if (!TextUtils.isEmpty(ui.getBindingPayAccount())) {
            getUserData().setBindingPayAccount(ui.getBindingPayAccount());
        }
    }

}
