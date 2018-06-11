package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {
 * "code": 1,
 * "message": "企业应聘管理列表",
 * "data": {
 * "pageNum": 1,
 * "pageSize": 5,
 * "size": 5,
 * "startRow": 0,
 * "endRow": 4,
 * "total": 5,
 * "pages": 1,
 * "list": [
 * {
 * "positionId": 5,
 * "positionName": "生产管理储备干部",
 * "commision": 10,
 * "region": null,
 * "salary": 100,
 * "workHours": null,
 * "hiringCount": 20,
 * "enrollNum": 0,
 * "agreeNum": 0,
 * "applyNum": 0
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
public class CompanyEmployeeResponse extends BaseResponse {

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
        private List<EmployeeInfo> list;

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

        public List<EmployeeInfo> getList() {
            return list;
        }

        public void setList(List<EmployeeInfo> list) {
            this.list = list;
        }
    }

    public class EmployeeInfo {
        private int positionId;
        private float commision;
        private float salary;
        private int hiringCount;
        private int enrollNum;
        private int agreeNum;
        private int applyNum;
        private String positionName;
        private String region;
        private String workHours;


        public float getSalary() {
            return salary;
        }

        public void setSalary(float salary) {
            this.salary = salary;
        }

        public float getCommision() {
            return commision;
        }

        public void setCommision(float commision) {
            this.commision = commision;
        }


        public int getEnrollNum() {
            return enrollNum;
        }

        public void setEnrollNum(int enrollNum) {
            this.enrollNum = enrollNum;
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



        public int getHiringCount() {
            return hiringCount;
        }

        public void setHiringCount(int hiringCount) {
            this.hiringCount = hiringCount;
        }

        public int getPositionId() {
            return positionId;
        }

        public void setPositionId(int positionId) {
            this.positionId = positionId;
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

        public String getWorkHours() {
            return workHours;
        }

        public void setWorkHours(String workHours) {
            this.workHours = workHours;
        }
    }
}
