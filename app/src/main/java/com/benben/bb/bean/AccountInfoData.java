package com.benben.bb.bean;

import java.io.Serializable;

/**
 * "token": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0MzE2OTBlNGMxMzQ0N2Q4OTIyZmZlMjczMzg1ZWNlYiIsImlzcyI6Imp3dC1iZW5iZW4iLCJzdWIiOiJ7XCJ1c2VySWRcIjpcIjRcIixcInVzZXJOYW1lXCI6XCIxNzc3MzExOTg2MFwifSIsImlhdCI6MTUyNjk3NjgyNCwiZXhwIjoxNTI3MDIwMDI0fQ.VaUQWxgGR_FvQa21GFb01HvkcveopGyl22o-fdBqyXE"
 * "id": 4,
 * "userName": "17773119860",
 * "passWord": "7403",
 * "benbenNum": "10004",
 * "nickName": "王生银",
 * "avatar": "120.79.155.104",
 * "region": "",
 * "sex": 0,
 * "age": "",
 * "nation": "",
 * "education": "",
 * "major": "",
 * "signature": "",
 * "trueName": "银华",
 * "identityCard": "1234569852",
 * "birthDate": "2018-05-22 18:36:44",
 * "finishSchool": "",
 * "studyStatus": 0,
 * "balance": 0,
 * "idcardFan": "/upload/img/e89e47ca0fd24a1d95ee10dc2cd8bea1.jpeg",
 * "idcardZheng": "/upload/img/7a4832fbfc5741ffbda4bfc30b64b09c.jpeg",
 * "bindingPaytype": "",
 * "bindingPayAccount": "",
 * "validateStatus": 1,
 * "djBalance": 0,
 * "isAgent": 0,
 * "isCompany": 0,
 * "userStatus": 0,
 * "walletStatus": 0,
 * "userCode": "10001",
 * "createDate": 1525338532000,
 * "createUser": ""
 */
public class AccountInfoData implements Serializable {
    private int id;
    private int isAgent;
    private int isCompany;
    private int validateStatus;//validateStatus 0未认证;1认证中;2认证失败;3已通过
    private int walletStatus;
    private int sex;
    private float balance;
    private int studyStatus;
    private String token;
    private String userName;
    private String benbenNum;
    private String passWord;
    private String nickName;
    private String avatar;
    private String region;
    private String age;
    private String nation;
    private String education;
    private String major;
    private String signature;
    private String trueName;
    private String finishSchool;
    private String idcardFan;
    private String idcardZheng;
    private String identityCard;
    private String bindingPaytype;//银行名称
    private String bindingPayAccount;//银行账号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBenbenNum() {
        return benbenNum;
    }

    public void setBenbenNum(String benbenNum) {
        this.benbenNum = benbenNum;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getIsAgent() {
        return isAgent;
    }

    public void setIsAgent(int isAgent) {
        this.isAgent = isAgent;
    }

    public int getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(int isCompany) {
        this.isCompany = isCompany;
    }

    public int getValidateStatus() {
        return validateStatus;
    }

    public void setValidateStatus(int validateStatus) {
        this.validateStatus = validateStatus;
    }

    public int getWalletStatus() {
        return walletStatus;
    }

    public void setWalletStatus(int walletStatus) {
        this.walletStatus = walletStatus;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public int getStudyStatus() {
        return studyStatus;
    }

    public void setStudyStatus(int studyStatus) {
        this.studyStatus = studyStatus;
    }

    public String getFinishSchool() {
        return finishSchool;
    }

    public void setFinishSchool(String finishSchool) {
        this.finishSchool = finishSchool;
    }

    public String getIdcardFan() {
        return idcardFan;
    }

    public void setIdcardFan(String idcardFan) {
        this.idcardFan = idcardFan;
    }

    public String getIdcardZheng() {
        return idcardZheng;
    }

    public void setIdcardZheng(String idcardZheng) {
        this.idcardZheng = idcardZheng;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getBindingPaytype() {
        return bindingPaytype;
    }

    public void setBindingPaytype(String bindingPaytype) {
        this.bindingPaytype = bindingPaytype;
    }

    public String getBindingPayAccount() {
        return bindingPayAccount;
    }

    public void setBindingPayAccount(String bindingPayAccount) {
        this.bindingPayAccount = bindingPayAccount;
    }
}
