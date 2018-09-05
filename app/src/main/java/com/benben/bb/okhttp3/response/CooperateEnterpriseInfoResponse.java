package com.benben.bb.okhttp3.response;

/**
 "code": 1,
 "message": "搜索企业",
 "data": {
 "id": 8,
 "userId": 9,
 "categoriesId": 71,
 "companyName": "美团点评",
 "representative": "法人",
 "businessImg": "http://www.zhibenben.cn/upload/img/0824738e5b2f4073b71b4b5188642015.jpeg",
 "companyMien": "http://www.zhibenben.cn/upload/img/f5dff36e9e0546b1ada5160b44f7ed0a.jpeg,http://www.zhibenben.cn/upload/img/9f6efcaa9ff444efaaabbd3857aefae6.jpeg,http://www.zhibenben.cn/upload/img/b46d0eaf5ff341d88918e9aa42707db8.jpeg",
 "companyAddr": "扬州信息服务产业基地内7号楼",
 "companyNature": 1,
 "companySize": 1,
 "companyRegion": "江苏省.扬州市.广陵区",
 "auditStatus": 1,
 "createDate": null,
 "isDelete": null,
 "introduction": "企业简介：美团点评公司是全球领先的一站式生活服务平台，为超过6亿消费者和和超过450万的优质商户提供一个连接线上线下的电子商务网络。秉承让每个人吃得更好，活的更好的使命，美团点评的业务覆盖了超过200个丰富品类和2800个城区县网络，在餐饮、外卖、酒店旅游、丽人、家庭、休闲娱乐等领域具有领先的市场地位。美团点评不断用科技和创新赋能于传统产业，深刻地影响和改变了广大用户的生活习惯。截止2016年底，美团点评年度活跃买家超过2.4亿。\n二、补充说明：1、薪资构成=底薪2000+餐补260+全勤100+计件效绩（单价在0.7元-2元不等），综合薪资3500~5500元；2、五险一金+补充医疗保险+商业意外险、免费体检、旅游、员工文体活动、年假、结婚贺礼、生宝宝贺礼等；3、入职培训，入职即参加带薪培训，专业培训快速上岗；4、免费住宿，距离办公室5分钟路程，4~6人间，热水、空调、wifi一应俱全；5、入职满3~12个月参加内部竞聘，竞聘通过即可晋升、转岗。职业发展多通道：①、培训讲师、质检、流程管理、人事、行政等；②，优秀员工、组长、班长、主管、经理、高级经理等；③、其他跨事业群岗位；",
 "creditCode": "",
 "po": true
 }
 */
public class CooperateEnterpriseInfoResponse extends BaseResponse {

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
