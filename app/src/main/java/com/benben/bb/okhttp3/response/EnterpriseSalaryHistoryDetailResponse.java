package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {
 * "code": 1,
 * "message": "工资核算详情List",
 * "data": {
 * "pageNum": 1,
 * "pageSize": 5,
 * "size": 2,
 * "startRow": 1,
 * "endRow": 2,
 * "total": 2,
 * "pages": 1,
 * "list": [
 * {
 * "id": 9,
 * "userId": 5,
 * "wagesListId": 10,
 * "workHours": 1,
 * "buckleMoney": 1,
 * "rewardMoney": 1,
 * "buckleDetails": "去",
 * "factMoney": 100,
 * "payDay": "",
 * "payStatus": 0,
 * "createDate": "2018-06",
 * "wagesStatus": 0,
 * "complain": "",
 * "isDelete": 0,
 * "salary": 0,
 * "wagesTitle": null
 * },
 * {
 * "id": 10,
 * "userId": 6,
 * "wagesListId": 10,
 * "workHours": 1,
 * "buckleMoney": 1,
 * "rewardMoney": 1,
 * "buckleDetails": "我",
 * "factMoney": 100,
 * "payDay": "",
 * "payStatus": 0,
 * "createDate": "2018-06",
 * "wagesStatus": 0,
 * "complain": "",
 * "isDelete": 0,
 * "salary": 0,
 * "wagesTitle": null
 *  "userName": "13456783169",
 "trueName": "鄢立峰"
 * }
 * ],
 * "prePage": 0,
 * "nextPage": 0,
 * "isFirstPage": true,
 * "isLastPage": true,
 * "hasPreviousPage": false,
 * "hasNextPage": false,
 * "navigatePages": 8,
 * "navigatepageNums": [
 * 1
 * ],
 * "navigateFirstPage": 1,
 * "navigateLastPage": 1,
 * "firstPage": 1,
 * "lastPage": 1
 * }
 * }
 */
public class EnterpriseSalaryHistoryDetailResponse extends BaseResponse {

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
        private List<SalaryHistoryInfo> list;

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

        public List<SalaryHistoryInfo> getList() {
            return list;
        }

        public void setList(List<SalaryHistoryInfo> list) {
            this.list = list;
        }
    }

    public class SalaryHistoryInfo {
        private int id;
        private int userId;
        private int wagesListId;
        private int workHours;
        private float buckleMoney;
        private float rewardMoney;
        private float factMoney;
        private int payStatus;
        private float salary;
        private String buckleDetails;
        private String complain;
        private String wagesTitle;
        private String userName;
        private String trueName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public float getBuckleMoney() {
            return buckleMoney;
        }

        public void setBuckleMoney(float buckleMoney) {
            this.buckleMoney = buckleMoney;
        }

        public float getSalary() {
            return salary;
        }

        public void setSalary(float salary) {
            this.salary = salary;
        }

        public String getWagesTitle() {
            return wagesTitle;
        }

        public void setWagesTitle(String wagesTitle) {
            this.wagesTitle = wagesTitle;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getWagesListId() {
            return wagesListId;
        }

        public void setWagesListId(int wagesListId) {
            this.wagesListId = wagesListId;
        }

        public int getWorkHours() {
            return workHours;
        }

        public void setWorkHours(int workHours) {
            this.workHours = workHours;
        }

        public float getRewardMoney() {
            return rewardMoney;
        }

        public void setRewardMoney(float rewardMoney) {
            this.rewardMoney = rewardMoney;
        }

        public float getFactMoney() {
            return factMoney;
        }

        public void setFactMoney(float factMoney) {
            this.factMoney = factMoney;
        }

        public int getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(int payStatus) {
            this.payStatus = payStatus;
        }

        public String getBuckleDetails() {
            return buckleDetails;
        }

        public void setBuckleDetails(String buckleDetails) {
            this.buckleDetails = buckleDetails;
        }

        public String getComplain() {
            return complain;
        }

        public void setComplain(String complain) {
            this.complain = complain;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }
    }

}
