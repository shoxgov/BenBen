package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {
 * "code": 1,
 * "message": "查询或搜索职位列表",
 * "data": {
 * "pageNum": 1,
 * "pageSize": 5,
 * "size": 3,
 * "startRow": 1,
 * "endRow": 3,
 * "total": 3,
 * "pages": 1,
 * "list": [
 * {
 * "id": 8,
 * "positionName": "工程储备干部",
 * "commision": 10,
 * "region": "岳麓区",
 * "salary": 100,
 * "hiringCount": 5,
 * "enrollNum": 0,
 * "welfare": "五险一金,包食宿,班车接送,生日福利",
 * "endTime": "2020-01-01",
 * "houseImg": "/upload/img/fbc37ee7258a4c26a8d95a77487b9a2f.jpeg,/upload/img/8e71eafa808a4d82bdade7174a38a815.jpeg"
 * },
 * {
 * "id": 12,
 * "positionName": "SMT制程工程师",
 * "commision": 300,
 * "region": "雨花区",
 * "salary": 100,
 * "hiringCount": 200,
 * "enrollNum": 0,
 * "welfare": "五险一金,包食宿,班车接送,生日福利",
 * "endTime": "2030-01-01",
 * "houseImg": "/upload/img/fbc37ee7258a4c26a8d95a77487b9a2f.jpeg,/upload/img/8e71eafa808a4d82bdade7174a38a815.jpeg"
 * },
 * {
 * "id": 13,
 * "positionName": "数据生产工程师",
 * "commision": 200,
 * "region": "雨花区",
 * "salary": 100,
 * "hiringCount": 100,
 * "enrollNum": 0,
 * "welfare": "五险一金,包食宿,班车接送,生日福利",
 * "endTime": "2030-07-01",
 * "houseImg": "/upload/img/fbc37ee7258a4c26a8d95a77487b9a2f.jpeg,/upload/img/8e71eafa808a4d82bdade7174a38a815.jpeg"
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
public class SearchResultResponse extends BaseResponse {

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
        private List<RecruitInfo> list;

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

        public List<RecruitInfo> getList() {
            return list;
        }

        public void setList(List<RecruitInfo> list) {
            this.list = list;
        }
    }

    public class RecruitInfo {
        private int id;
        private int advSort;
        private float commision;
        private float salary;
        private int hiringCount;
        private int enrollNum;
        private String positionName;
        private String companyName;
        private String region;
        private String welfare;
        private String endTime;
        private String companyMien;
        private String focusSalary;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getFocusSalary() {
            return focusSalary;
        }

        public void setFocusSalary(String focusSalary) {
            this.focusSalary = focusSalary;
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

        public String getWelfare() {
            return welfare;
        }

        public void setWelfare(String welfare) {
            this.welfare = welfare;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getAdvSort() {
            return advSort;
        }

        public void setAdvSort(int advSort) {
            this.advSort = advSort;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyMien() {
            return companyMien;
        }

        public void setCompanyMien(String companyMien) {
            this.companyMien = companyMien;
        }
    }
}
