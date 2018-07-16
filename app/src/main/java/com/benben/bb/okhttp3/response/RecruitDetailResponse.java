package com.benben.bb.okhttp3.response;

/**
 * {
 * "code": 1,
 * "message": "职位信息",
 * "data": {
 * "id": 21,
 * "companyId": 15,
 * "positionName": "搓澡工",
 * "region": "全国",
 * "salary": 15,
 * "commision": 5,
 * "advSort": 1,
 * "houseImg": "",
 * "welfare": "五险一金",
 * "hiringCount": 10,
 * "endTime": "2018-07-27 00:00:00.0",
 * "payTime": "每月15日",
 * "workTime": "每天工作8小时",
 * "focusSalary": "3000-6000",
 * "createDate": "2018-07-05 15:49:22.0",
 * "supplement": "",
 * "positionStatus": 0,
 * "isDelete": null,
 * "jobDemand": "know图我发具体咯五
 * <p>
 * lost我",
 * "dayworkHour": 8,
 * "monthworkDay": 22,
 * "enrollNum": 0,
 * "settlement": 2,
 * "commisionNum": 300,
 * "commisionDetails": "科目汇总表",
 * "entrySex": "不限",
 * "entryAge": "18-45",
 * "staffCanteen": "餐费自理",
 * "staffHouse": "住宿自理",
 * "po": true
 * "introduction":"华通电脑（长沙）有限公司为台商独资企业，母公
 * ,"companyMien":"/upload/img/ht1.jpg",
 *  companyAddr//地址  companyRegion//地区
 * }
 * }
 */
public class RecruitDetailResponse extends BaseResponse {

    public RecruitDetailInfo getData() {
        return data;
    }

    public void setData(RecruitDetailInfo data) {
        this.data = data;
    }

    private RecruitDetailInfo data;

    public class RecruitDetailInfo {
        private int id;
        private int companyId;
        private int hiringCount;
        private int positionStatus;
        private int dayworkHour;
        private int monthworkDay;
        private int enrollNum;
        private int settlement;
        private float salary;
        private float commisionNum;
        private float commision;
        private String createDate;

        private String positionName;
        private String region;
        private String houseImg;
        private String welfare;
        private String endTime;
        private String payTime;
        private String workTime;
        private String jobDemand;
        private String supplement;
        private String focusSalary;
        private String entrySex;
        private String entryAge;
        private String staffCanteen;
        private String staffHouse;
        private String commisionDetails;
        private String introduction;
        private String companyMien;
        private String companyAddr;
        private String companyRegion;

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

        public int getHiringCount() {
            return hiringCount;
        }

        public void setHiringCount(int hiringCount) {
            this.hiringCount = hiringCount;
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

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getFocusSalary() {
            return focusSalary;
        }

        public void setFocusSalary(String focusSalary) {
            this.focusSalary = focusSalary;
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

        public String getHouseImg() {
            return houseImg;
        }

        public void setHouseImg(String houseImg) {
            this.houseImg = houseImg;
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

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getWorkTime() {
            return workTime;
        }

        public void setWorkTime(String workTime) {
            this.workTime = workTime;
        }

        public String getJobDemand() {
            return jobDemand;
        }

        public void setJobDemand(String jobDemand) {
            this.jobDemand = jobDemand;
        }

        public String getSupplement() {
            return supplement;
        }

        public void setSupplement(String supplement) {
            this.supplement = supplement;
        }

        public int getSettlement() {
            return settlement;
        }

        public void setSettlement(int settlement) {
            this.settlement = settlement;
        }

        public float getCommisionNum() {
            return commisionNum;
        }

        public void setCommisionNum(float commisionNum) {
            this.commisionNum = commisionNum;
        }

        public String getEntrySex() {
            return entrySex;
        }

        public void setEntrySex(String entrySex) {
            this.entrySex = entrySex;
        }

        public String getEntryAge() {
            return entryAge;
        }

        public void setEntryAge(String entryAge) {
            this.entryAge = entryAge;
        }

        public String getStaffCanteen() {
            return staffCanteen;
        }

        public void setStaffCanteen(String staffCanteen) {
            this.staffCanteen = staffCanteen;
        }

        public String getStaffHouse() {
            return staffHouse;
        }

        public void setStaffHouse(String staffHouse) {
            this.staffHouse = staffHouse;
        }

        public String getCommisionDetails() {
            return commisionDetails;
        }

        public void setCommisionDetails(String commisionDetails) {
            this.commisionDetails = commisionDetails;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
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
    }
}
