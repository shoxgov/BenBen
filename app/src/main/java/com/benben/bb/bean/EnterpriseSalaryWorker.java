package com.benben.bb.bean;

/**
 * Created by wangshengyin on 2018-06-01.
 * email:shoxgov@126.com
 */
//  "id":0,
//          "userId":0,
//          "wagesListId":0,
//          "workHours":0,
//          "buckleMoney":0,
//          "rewardMoney":0,
//          "buckleDetails":"string",
//          "factMoney":0,
//          "payDay":"string",
//          "payStatus":0,
//          "createDate":"string",
//          "wagesStatus":0,
//          "complain":"string",
//          "isDelete":0,
//          "salary":0,
//          "wagesTitle":"string"

public class EnterpriseSalaryWorker {
    private int userId;
    private int fromId;
    private float workHours;
    private float buckleMoney;
    private float rewardMoney;
    private float factMoney;
    private String buckleDetails;

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getWorkHours() {
        return workHours;
    }

    public void setWorkHours(float workHours) {
        this.workHours = workHours;
    }

    public float getBuckleMoney() {
        return buckleMoney;
    }

    public void setBuckleMoney(float buckleMoney) {
        this.buckleMoney = buckleMoney;
    }

    public float getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(float rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public String getBuckleDetails() {
        return buckleDetails;
    }

    public void setBuckleDetails(String buckleDetails) {
        this.buckleDetails = buckleDetails;
    }

    public float getFactMoney() {
        return factMoney;
    }

    public void setFactMoney(float factMoney) {
        this.factMoney = factMoney;
    }
}
