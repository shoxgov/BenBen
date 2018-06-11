package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {
 * "code": 1,
 * "message": "企业招聘信息列表",
 * "data": {
 * "pageNum": 1,
 * "pageSize": 5,
 * "size": 5,
 * "startRow": 1,
 * "endRow": 5,
 * "total": 5,
 * "pages": 1,
 * "list": [
 * {
 * "id": 6,
 * "companyId": 4,
 * "positionName": "线路板设计储备干部",
 * "region": "",
 * "salary": 100,
 * "commision": 0,
 * "houseDetails": "",
 * "houseImg": "",
 * "welfare": null,
 * "hiringCount": 1,
 * "endTime": null,
 * "payTime": "",
 * "workTime": "",
 * "focusSalary": "",
 * "createDate": 1526615059000,
 * "supplement": "",
 * "positionStatus": 0,
 * "isDelete": null,
 * "jobDemand": "印刷电路板生产工艺设计 \r\n制程设计：排版、孔径、叠板等设计 读带：档案转化为能识别资料",
 * "dayworkHour": 0,
 * "monthworkDay": 0,
 * "enrollNum": 0
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
public class CompanyRecruitResponse extends BaseResponse {

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
        private int companyId;
        private float salary;
        private float commision;
        private int positionStatus;
        private int dayworkHour;
        private int monthworkDay;
        private int hiringCount;
        private int enrollNum;
        private String positionName;
        private String region;
        private String houseDetails;
        private String welfare;
        private String houseImg;
        private String endTime;
        private String jobDemand;
        private long createDate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

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

        public int getPositionStatus() {
            return positionStatus;
        }

        public void setPositionStatus(int positionStatus) {
            this.positionStatus = positionStatus;
        }

        public int getDayworkHour() {
            return dayworkHour;
        }

        public void setDayworkHour(int dayworkHour) {
            this.dayworkHour = dayworkHour;
        }

        public int getMonthworkDay() {
            return monthworkDay;
        }

        public void setMonthworkDay(int monthworkDay) {
            this.monthworkDay = monthworkDay;
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

        public String getHouseDetails() {
            return houseDetails;
        }

        public void setHouseDetails(String houseDetails) {
            this.houseDetails = houseDetails;
        }

        public String getHouseImg() {
            return houseImg;
        }

        public void setHouseImg(String houseImg) {
            this.houseImg = houseImg;
        }

        public String getJobDemand() {
            return jobDemand;
        }

        public void setJobDemand(String jobDemand) {
            this.jobDemand = jobDemand;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getWelfare() {
            return welfare;
        }

        public void setWelfare(String welfare) {
            this.welfare = welfare;
        }

        public int getHiringCount() {
            return hiringCount;
        }

        public void setHiringCount(int hiringCount) {
            this.hiringCount = hiringCount;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
