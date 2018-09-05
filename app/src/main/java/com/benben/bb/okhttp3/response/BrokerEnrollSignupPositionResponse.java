package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * "list": [
 * {
 * "positionName": "点焊工",
 * "userlist": [
 * {
 * "userId": 134,
 * "userName": "15910686219",
 * "avatar": "http://www.zhibenben.cn/upload/face.jpg",
 * "trueName": "",
 * "age": null,
 * "nickName": null,
 * "sex": 0,
 * "education": null,
 * "fromId": null,
 * "positionUserId": null,
 * "resourcesSum": 0,
 * "benbenNum": null,
 * "createDate": null
 * },
 * {
 * "userId": 135,
 * "userName": "18872078123",
 * "avatar": "http://www.zhibenben.cn/upload/face.jpg",
 * "trueName": "",
 * "age": null,
 * "nickName": null,
 * "sex": 0,
 * "education": null,
 * "fromId": null,
 * "positionUserId": null,
 * "resourcesSum": 0,
 * "benbenNum": null,
 * "createDate": null
 * }
 * ],
 * "focusSalary": "3000-6000",
 * "companyName": "长沙卡斯马汽车系统有限公司",
 * "region": "湖南省.长沙市.长沙县"
 * },
 * {
 * "positionName": "点焊工",
 * "userlist": [
 * {
 * "userId": 134,
 * "userName": "15910686219",
 * "avatar": "http://www.zhibenben.cn/upload/face.jpg",
 * "trueName": "",
 * "age": null,
 * "nickName": null,
 * "sex": 0,
 * "education": null,
 * "fromId": null,
 * "positionUserId": null,
 * "resourcesSum": 0,
 * "benbenNum": null,
 * "createDate": null
 * },
 * {
 * "userId": 135,
 * "userName": "18872078123",
 * "avatar": "http://www.zhibenben.cn/upload/face.jpg",
 * "trueName": "",
 * "age": null,
 * "nickName": null,
 * "sex": 0,
 * "education": null,
 * "fromId": null,
 * "positionUserId": null,
 * "resourcesSum": 0,
 * "benbenNum": null,
 * "createDate": null
 * }
 * ],
 * "focusSalary": "3000-6000",
 * "companyName": "长沙卡斯马汽车系统有限公司",
 * "region": "湖南省.长沙市.长沙县"
 * }
 * ],
 */
public class BrokerEnrollSignupPositionResponse extends BaseResponse {

    private DataMap data;

    public DataMap getData() {
        return data;
    }

    public void setData(DataMap data) {
        this.data = data;
    }

    public class DataMap {
        private int pageNum;
        private int total;
        private int pages;
        private List<SignData> list;

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
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

        public List<SignData> getList() {
            return list;
        }

        public void setList(List<SignData> list) {
            this.list = list;
        }
    }

    public class SignData {
        private int positionId;
        private float salary;
        private float commision;
        private String positionName;
        private String focusSalary;
        private String companyName;
        private String region;
        private List<SignupPositionInfo> userlist;

        public int getPositionId() {
            return positionId;
        }

        public void setPositionId(int positionId) {
            this.positionId = positionId;
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

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }

        public String getFocusSalary() {
            return focusSalary;
        }

        public void setFocusSalary(String focusSalary) {
            this.focusSalary = focusSalary;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public List<SignupPositionInfo> getUserlist() {
            return userlist;
        }

        public void setUserlist(List<SignupPositionInfo> userlist) {
            this.userlist = userlist;
        }
    }

    public class SignupPositionInfo {
        private int userId;
        private int age;
        private int sex;
        private String avatar;
        private String userName;
        private String trueName;
        private String nickName;
        private String education;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
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

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }
    }

}
