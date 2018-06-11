package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {
 * }
 */
public class CompanyEmployeeEntryResponse extends BaseResponse {

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
        private List<EmployeeEntryInfo> list;

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

        public List<EmployeeEntryInfo> getList() {
            return list;
        }

        public void setList(List<EmployeeEntryInfo> list) {
            this.list = list;
        }
    }

    public class EmployeeEntryInfo {
        private int userId;
        private int positionUserId;
        private String userName;
        private String trueName;
        private String avatar;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getPositionUserId() {
            return positionUserId;
        }

        public void setPositionUserId(int positionUserId) {
            this.positionUserId = positionUserId;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }


}
