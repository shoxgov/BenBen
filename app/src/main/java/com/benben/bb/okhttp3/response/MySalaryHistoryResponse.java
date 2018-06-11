package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {
 * "code": 1,
 * "message": "工资条列表",
 * "data": {
 * "pageNum": 1,
 * "pageSize": 5,
 * "size": 1,
 * "startRow": 1,
 * "endRow": 1,
 * "total": 1,
 * "pages": 1,
 * "list": [
 * {
 * "id": 1,
 * "userId": null,
 * "wagesListId": null,
 * "workHours": 100,
 * "buckleMoney": 100,
 * "rewardMoney": 50,
 * "buckleDetails": null,
 * "factMoney": 0,
 * "payDay": "",
 * "payStatus": null,
 * "createDate": "2018-05-25 10:23:36.0",
 * "wagesStatus": null,
 * "complain": "",
 * "isDelete": null,
 * "salary": 0,
 * "wagesTitle": "工资单"
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
public class MySalaryHistoryResponse extends BaseResponse {

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
        private List<SalaryInfo> list;

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

        public List<SalaryInfo> getList() {
            return list;
        }

        public void setList(List<SalaryInfo> list) {
            this.list = list;
        }
    }

    public class SalaryInfo {
        private int id;
        private int workHours;
        private float buckleMoney;
        private float rewardMoney;
        private float factMoney;
        private float salary;
        private String payDay;
        private String buckleDetails;
        private String createDate;
        private String payStatus;
        private String wagesTitle;
        private String wagesStatus;
        private String complain;
        private String isDelete;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getWorkHours() {
            return workHours;
        }

        public void setWorkHours(int workHours) {
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

        public float getFactMoney() {
            return factMoney;
        }

        public void setFactMoney(float factMoney) {
            this.factMoney = factMoney;
        }

        public float getSalary() {
            return salary;
        }

        public void setSalary(float salary) {
            this.salary = salary;
        }

        public String getPayDay() {
            return payDay;
        }

        public void setPayDay(String payDay) {
            this.payDay = payDay;
        }

        public String getBuckleDetails() {
            return buckleDetails;
        }

        public void setBuckleDetails(String buckleDetails) {
            this.buckleDetails = buckleDetails;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        public String getWagesStatus() {
            return wagesStatus;
        }

        public void setWagesStatus(String wagesStatus) {
            this.wagesStatus = wagesStatus;
        }

        public String getComplain() {
            return complain;
        }

        public void setComplain(String complain) {
            this.complain = complain;
        }

        public String getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
        }

        public String getWagesTitle() {
            return wagesTitle;
        }

        public void setWagesTitle(String wagesTitle) {
            this.wagesTitle = wagesTitle;
        }
    }

}
