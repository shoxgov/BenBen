package com.benben.bb.okhttp3.response;

import java.io.Serializable;
import java.util.List;

/**
 {
 "code": 1,
 "message": "获取邀请二维码用户列表",
 "data": {
 "pageNum": 1,
 "pageSize": 10,
 "size": 1,
 "startRow": 1,
 "endRow": 1,
 "total": 1,
 "pages": 1,
 "list": [
 {
 "userId": 33,
 "userName": "13973139715",
 "avatar": "http://www.zhibenben.cn/upload/face.jpg",
 "trueName": "",
 "fromId": null,
 "positionUserId": null,
 "resourcesSum": 0,
 "benbenNum": "10033",
 "createDate": "2018-06-28 09:35:11.0"
 }
 ],
 "prePage": 0,
 "nextPage": 0,
 "isFirstPage": true,
 "isLastPage": true,
 "hasPreviousPage": false,
 "hasNextPage": false,
 "navigatePages": 8,
 "navigatepageNums": [
 1
 ],
 "navigateFirstPage": 1,
 "navigateLastPage": 1,
 "firstPage": 1,
 "lastPage": 1
 }
 }
 */
public class MyInviteResourceResponse extends BaseResponse {

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
        private List<MyInviteResource> list;

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

        public List<MyInviteResource> getList() {
            return list;
        }

        public void setList(List<MyInviteResource> list) {
            this.list = list;
        }
    }

   public class MyInviteResource{
       private int userId;
       private int resourcesSum;
       private int benbenNum;
       private String userName;
       private String trueName;
       private String avatar;
       private String createDate;

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

       public int getBenbenNum() {
           return benbenNum;
       }

       public void setBenbenNum(int benbenNum) {
           this.benbenNum = benbenNum;
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

       public String getCreateDate() {
           return createDate;
       }

       public void setCreateDate(String createDate) {
           this.createDate = createDate;
       }
   }
}
