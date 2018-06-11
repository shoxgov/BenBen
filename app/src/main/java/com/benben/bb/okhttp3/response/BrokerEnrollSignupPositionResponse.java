package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * "code":1, "message":"验证码已发送", "data":"1823"
 */
public class BrokerEnrollSignupPositionResponse extends BaseResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private int pageNum;
        private int pageSize;
        private int total;
        private int pages;
        private List<SignupPositionInfo> list;

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<SignupPositionInfo> getList() {
            return list;
        }

        public void setList(List<SignupPositionInfo> list) {
            this.list = list;
        }
    }

    public class SignupPositionInfo {
        private int positionId;
        private float commision;
        private float salary;
        private int hiringCount;
        private int enrollNum;
        private int agreeNum;
        private int applyNum;
        private String avatar;
        private String positionName;
        private String region;


        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getPositionId() {
            return positionId;
        }

        public void setPositionId(int positionId) {
            this.positionId = positionId;
        }

        public float getCommision() {
            return commision;
        }

        public void setCommision(float commision) {
            this.commision = commision;
        }

        public float getSalary() {
            return salary;
        }

        public void setSalary(float salary) {
            this.salary = salary;
        }

        public int getHiringCount() {
            return hiringCount;
        }

        public void setHiringCount(int hiringCount) {
            this.hiringCount = hiringCount;
        }

        public int getEnrollNum() {
            return enrollNum;
        }

        public void setEnrollNum(int enrollNum) {
            this.enrollNum = enrollNum;
        }

        public int getAgreeNum() {
            return agreeNum;
        }

        public void setAgreeNum(int agreeNum) {
            this.agreeNum = agreeNum;
        }

        public int getApplyNum() {
            return applyNum;
        }

        public void setApplyNum(int applyNum) {
            this.applyNum = applyNum;
        }

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }

}
