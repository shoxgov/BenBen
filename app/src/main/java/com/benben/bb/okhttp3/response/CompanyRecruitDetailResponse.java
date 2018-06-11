package com.benben.bb.okhttp3.response;

/**
 * {
 * "code": 1,
 * "message": "企业招聘信息列表",
 * "data": {
 * "id": 5,
 * "companyId": 4,
 * "positionName": "好兔兔",
 * "region": "青海省果洛藏族自治州枞阳县",
 * "salary": 1000,
 * "commision": 500,
 * "houseDetails": "包住",
 * "houseImg": "http://120.79.155.10/upload/img/fbc37ee7258a4c26a8d95a77487b9a2f.jpeg,http://120.79.155.10/upload/img/8e71eafa808a4d82bdade7174a38a815.jpeg",
 * "welfare": "加班费,保险,餐补,双休,十三薪,包吃",
 * "hiringCount": 20,
 * "endTime": "2018-05-08",
 * "payTime": "每月15日",
 * "workTime": "8",
 * "focusSalary": "1200",
 * "createDate": 1526615039000,
 * "supplement": "好咯图咯KKK图谋傻笨做PPT我",
 * "positionStatus": 0,
 * "isDelete": null,
 * "jobDemand": "狗啃涂卡凑图魔图web几楼JJ",
 * "dayworkHour": 8,
 * "monthworkDay": 20,
 * "enrollNum": 2,
 * "po": true
 * }
 * }
 */
public class CompanyRecruitDetailResponse extends BaseResponse {

    private RecruitDetail data;

    public RecruitDetail getData() {
        return data;
    }

    public void setData(RecruitDetail data) {
        this.data = data;
    }

    public class RecruitDetail {
        private int id;
        private int companyId;
        private float salary;
        private float commision;
        private int hiringCount;
        private int positionStatus;
        private int dayworkHour;
        private int monthworkDay;
        private int enrollNum;
        private String positionName;
        private String region;
        private String houseDetails;
        private String welfare;
        private String houseImg;
        private String endTime;
        private String jobDemand;
        private String focusSalary;
        private String supplement;
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

        public String getSupplement() {
            return supplement;
        }

        public void setSupplement(String supplement) {
            this.supplement = supplement;
        }

        public String getFocusSalary() {
            return focusSalary;
        }

        public void setFocusSalary(String focusSalary) {
            this.focusSalary = focusSalary;
        }
    }
}
