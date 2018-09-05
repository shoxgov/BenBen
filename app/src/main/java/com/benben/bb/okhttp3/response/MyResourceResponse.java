package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * "code":1, "message":"验证码已发送", "data":"1823"
 */
public class MyResourceResponse extends BaseResponse {

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
        private List<EntryPositionInfo> list;

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

        public List<EntryPositionInfo> getList() {
            return list;
        }

        public void setList(List<EntryPositionInfo> list) {
            this.list = list;
        }
    }

    /**
     "userId": 116,
     "userName": "13574851212",
     "avatar": "http://www.zhibenben.cn/upload/face.jpg",
     "trueName": "",
     "age": "0",
     "nickName": "犇犇用户",
     "sex": 0,
     "education": "最高学历",
     "fromId": null,
     "positionUserId": null,
     "resourcesSum": 0,
     "benbenNum": "10116",
     "createDate": "2018-07-24"
     */
    public class EntryPositionInfo {
        private int userId;
        private int age;
        private int sex;
        private String userName;
        private String trueName;
        private String nickName;
        private String avatar;
        private String education;

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

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }
    }

}
