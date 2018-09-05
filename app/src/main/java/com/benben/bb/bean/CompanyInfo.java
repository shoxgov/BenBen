package com.benben.bb.bean;

/**
 * Created by wangshengyin on 2018-08-28.
 * email:shoxgov@126.com
 */

public class CompanyInfo {
    private int id;
    private int companyId;
    private int userId;
    private int categoriesId;
    private int companyNature;
    private int companySize;
    private int auditStatus;

    private String companyName;
    private String representative;
    private String businessImg;
    private String companyMien;
    private String companyAddr;
    private String companyRegion;
    private String introduction;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(int categoriesId) {
        this.categoriesId = categoriesId;
    }

    public int getCompanyNature() {
        return companyNature;
    }

    public void setCompanyNature(int companyNature) {
        this.companyNature = companyNature;
    }

    public int getCompanySize() {
        return companySize;
    }

    public void setCompanySize(int companySize) {
        this.companySize = companySize;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getBusinessImg() {
        return businessImg;
    }

    public void setBusinessImg(String businessImg) {
        this.businessImg = businessImg;
    }

    public String getCompanyMien() {
        return companyMien;
    }

    public void setCompanyMien(String companyMien) {
        this.companyMien = companyMien;
    }

    public String getCompanyAddr() {
        return companyAddr;
    }

    public void setCompanyAddr(String companyAddr) {
        this.companyAddr = companyAddr;
    }

    public String getCompanyRegion() {
        return companyRegion;
    }

    public void setCompanyRegion(String companyRegion) {
        this.companyRegion = companyRegion;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}