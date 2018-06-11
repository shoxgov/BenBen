package com.benben.bb.okhttp3.response;

/**
 * {
 * "code": 1,
 * "message": "企业信息",
 * "data": {
 * "id": 4,
 * "userId": 4,
 * "categoriesId": 12,
 * "companyName": "华通电脑（惠州）有限公司",
 * "representative": "王生银",
 * "businessImg": "/upload/img/85f8cca6e55149978cf23412628a4daa.jpeg,/upload/img/5260945fdd6f409ba8531f288f20770e.jpeg",
 * "companyMien": "/upload/img/fbc37ee7258a4c26a8d95a77487b9a2f.jpeg,/upload/img/8e71eafa808a4d82bdade7174a38a815.jpeg",
 * "companyAddr": "湖南长沙",
 * "companyNature": 2,
 * "companySize": 0,
 * "companyRegion": "岳麓区",
 * "auditStatus": 1,
 * "createDate": 1526610974000,
 * "isDelete": 0,
 * "introduction": "华通电脑（惠州）有限公司为台商独资企业，母公司为华通电脑股份有限公司。 华通电脑股份有限公司成立于 1973 年 8 月，为台湾第一家响应政府发展高科 技策略工业政策的印刷电路板专业制造公司。从以生产单、双面印刷电路板为 主，到带领台湾印刷电路板产业朝向多层板发展，历经几十年的发展，曾一跃 成为世界第一大笔记本印刷电路板供货商。1995 年华通在惠州建立内地第一 个生产基地，占地 19.6 万平方米，目前员工达 14000 余人，整体规模在惠州排 名前三，于 2005 年成为世界最大的手机电路板供货商之一。并相继获得客户 颁发的品质金奖，和政府特别授予的 “出口大户”、 “环保先进单位”、“劳 动用工标杆企业” 称号。 \r\n\r\n华通将是您实现理想抱负的最佳选择。华通主要的业务范围包含一般多层电路板、高密度电路板(HDI)、高层次板\r\n\r\n(HLC)、软板(FPC)与软硬板(Rigid-Flex PCB)等产品，并提供零件装配服务，以 满足客户全方位的需求。"
 * }
 * }
 */
public class CompanyInfoResponse extends BaseResponse {

    private CompanyInfo data;

    public CompanyInfo getData() {
        return data;
    }

    public void setData(CompanyInfo data) {
        this.data = data;
    }

    public class CompanyInfo {
        private int id;
        private int userId;
        private int categoriesId;
        private int companyNature;
        private int companySize;
        private int auditStatus;
        private int isDelete;

        private String companyName;
        private String representative;
        private String businessImg;
        private String companyMien;
        private String companyAddr;
        private String companyRegion;
        private String introduction;

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

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
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

}
