package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {
 * "code": 1,
 * "message": "我的报名列表",
 * "data": {
 * "pageNum": 1,
 * "pageSize": 5,
 * "size": 0,
 * "startRow": 0,
 * "endRow": 0,
 * "total": 0,
 * "pages": 0,
 * "list": [
 * "id": null,
 * "positionName": "线路板设计储备干部",
 * "commision": null,
 * "region": "岳麓区",
 * "salary": 100,
 * "hiringCount": 10,
 * "enrollNum": 0,
 * "welfare": "五险一金,包食宿,班车接送,生日福利",
 * "endTime": null,
 * "houseImg": null,
 * "positionUserId": 11],
 * "prePage": 0,
 * "nextPage": 0,
 * "isFirstPage": true,
 * "isLastPage": true,
 * "hasPreviousPage": false,
 * "hasNextPage": false,
 * "navigatePages": 8,
 * "navigatepageNums": [],
 * "navigateFirstPage": 0,
 * "navigateLastPage": 0,
 * "firstPage": 0,
 * "lastPage": 0
 * }
 * }
 */
public class SignupResponse extends BaseResponse {

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
        private List<SignupInfo> list;

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

        public List<SignupInfo> getList() {
            return list;
        }

        public void setList(List<SignupInfo> list) {
            this.list = list;
        }
    }

    public class SignupInfo {
        private int id;
        private int positionId;
        private int settlement;
        private int advSort;
        private int hiringCount;
        private int enrollNum;
        private int entryStatus;
        private String positionName;
        private String companyName;
        private String houseImg;
        private String focusSalary;
        private String region;
        private String welfare;
        private String endTime;

        public int getEntryStatus() {
            return entryStatus;
        }

        public void setEntryStatus(int entryStatus) {
            this.entryStatus = entryStatus;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPositionId() {
            return positionId;
        }

        public void setPositionId(int positionId) {
            this.positionId = positionId;
        }

        public int getSettlement() {
            return settlement;
        }

        public void setSettlement(int settlement) {
            this.settlement = settlement;
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

        public String getHouseImg() {
            return houseImg;
        }

        public void setHouseImg(String houseImg) {
            this.houseImg = houseImg;
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
    }

}
