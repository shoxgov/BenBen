package com.benben.bb.bean;

import android.text.TextUtils;

import com.benben.bb.MyApplication;
import com.benben.bb.okhttp3.response.LoginResponse;

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
        return MyApplication.userData;
    }

    public static void updateAccount(LoginResponse lr) {
        ////////////////////////////////
        if (lr == null) {
            return;
        }
        AccountInfoData userInfoData = new AccountInfoData();
        if (!TextUtils.isEmpty(lr.getData().getToken())) {
            userInfoData.setToken(lr.getData().getToken());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getAvatar())) {
            userInfoData.setAvatar(lr.getData().getUser().getAvatar());
        }
        if (lr.getData().getUser().getId() > 0) {
            userInfoData.setId(lr.getData().getUser().getId());
        }
        userInfoData.setStudyStatus(lr.getData().getUser().getStudyStatus());
        userInfoData.setSex(lr.getData().getUser().getSex());
        userInfoData.setBalance(lr.getData().getUser().getBalance());
        userInfoData.setIsAgent(lr.getData().getUser().getIsAgent());
        userInfoData.setIsCompany(lr.getData().getUser().getIsCompany());
        userInfoData.setValidateStatus(lr.getData().getUser().getValidateStatus());
        userInfoData.setWalletStatus(lr.getData().getUser().getWalletStatus());
        if (!TextUtils.isEmpty(lr.getData().getUser().getBenbenNum())) {
            userInfoData.setBenbenNum(lr.getData().getUser().getBenbenNum());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getSignature())) {
            userInfoData.setSignature(lr.getData().getUser().getSignature());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getNation())) {
            userInfoData.setNation(lr.getData().getUser().getNation());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getMajor())) {
            userInfoData.setMajor(lr.getData().getUser().getMajor());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getEducation())) {
            userInfoData.setEducation(lr.getData().getUser().getEducation());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getAge())) {
            userInfoData.setAge(lr.getData().getUser().getAge());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getRegion())) {
            userInfoData.setRegion(lr.getData().getUser().getRegion());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getUserName())) {
            userInfoData.setUserName(lr.getData().getUser().getUserName());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getNickName())) {
            userInfoData.setNickName(lr.getData().getUser().getNickName());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getTrueName())) {
            userInfoData.setTrueName(lr.getData().getUser().getTrueName());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getFinishSchool())) {
            userInfoData.setFinishSchool(lr.getData().getUser().getFinishSchool());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getIdcardZheng())) {
            userInfoData.setIdcardZheng(lr.getData().getUser().getIdcardZheng());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getIdcardFan())) {
            userInfoData.setIdcardFan(lr.getData().getUser().getIdcardFan());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getIdentityCard())) {
            userInfoData.setIdentityCard(lr.getData().getUser().getIdentityCard());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getBindingPaytype())) {
            userInfoData.setBindingPaytype(lr.getData().getUser().getBindingPaytype());
        }
        if (!TextUtils.isEmpty(lr.getData().getUser().getBindingPayAccount())) {
            userInfoData.setBindingPayAccount(lr.getData().getUser().getBindingPayAccount());
        }
        ///////////////////////////////////
        //save to local
        //////////////////////////////////
        MyApplication.userData = userInfoData;
    }

//    public static void restoreUserInfo() {
//    }
}
