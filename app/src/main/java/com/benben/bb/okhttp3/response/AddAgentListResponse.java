package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {
 * "code": 1,
 * "message": "获取新增经纪人列表",
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
 * "userId": 3,
 * "userName": "18807411763",
 * "avatar": "http://120.79.155.10/upload/img/e9a258320c134505a03bd99fa5a263c3.png",
 * "trueName": "邓云熹",
 * "positionUserId": null,
 * "resourcesSum": 0,
 * "benbenNum": "10003"
 * },
 * {
 * "userId": 8,
 * "userName": "13998776655",
 * "avatar": "http://120.79.155.10/upload/img/e9a258320c134505a03bd99fa5a263c3.png",
 * "trueName": " 大灰狼",
 * "positionUserId": null,
 * "resourcesSum": 0,
 * "benbenNum": "10008"
 * },
 * {
 * "userId": 10,
 * "userName": "18173177170",
 * "avatar": "http://120.79.155.10/upload/img/e9a258320c134505a03bd99fa5a263c3.png",
 * "trueName": "小白兔",
 * "positionUserId": null,
 * "resourcesSum": 0,
 * "benbenNum": "10010"
 * },
 * {
 * "userId": 11,
 * "userName": "15874203322",
 * "avatar": "http://120.79.155.10/upload/img/e9a258320c134505a03bd99fa5a263c3.png",
 * "trueName": "光头强",
 * "positionUserId": null,
 * "resourcesSum": 0,
 * "benbenNum": "10011"
 * },
 * {
 * "userId": 12,
 * "userName": "15874203325",
 * "avatar": "http://120.79.155.10/upload/img/e9a258320c134505a03bd99fa5a263c3.png",
 * "trueName": "大灰熊",
 * "positionUserId": null,
 * "resourcesSum": 0,
 * "benbenNum": "10012"
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
public class AddAgentListResponse extends BaseResponse {

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
        private List<AgentInfo> list;

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

        public List<AgentInfo> getList() {
            return list;
        }

        public void setList(List<AgentInfo> list) {
            this.list = list;
        }
    }

    /**
     * "userId": 12,
     * "userName": "15874203325",
     * "avatar": "http://120.79.155.10/upload/img/e9a258320c134505a03bd99fa5a263c3.png",
     * "trueName": "大灰熊",
     * "positionUserId": null,
     * "resourcesSum": 0,
     * "benbenNum": "10012"
     */
    public class AgentInfo {
        private int userId;
        private int resourcesSum;
        private String userName;
        private String trueName;
        private String avatar;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getResourcesSum() {
            return resourcesSum;
        }

        public void setResourcesSum(int resourcesSum) {
            this.resourcesSum = resourcesSum;
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
