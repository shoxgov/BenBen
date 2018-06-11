package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {
 * "code":1,
 * "message":"入账出账明细列表",
 * "data":{
 * "income":100.40,
 * "pay":199.20,
 * "page":{
 * "pageNum":1,
 * "pageSize":10,
 * "size":4,
 * "startRow":1,
 * "endRow":4,
 * "total":4,
 * "pages":1,
 * "list":[
 * {
 * "id":1,
 * "userId":4,
 * "money":99.20,
 * "billType":0,
 * "billStatus":3,
 * "billDetail":"无",
 * "createDate":"2018-05-17",
 * "isDelete":0
 * },
 * {
 * "id":2,
 * "userId":4,
 * "money":50.40,
 * "billType":1,
 * "billStatus":1,
 * "billDetail":"更好",
 * "createDate":"2018-05-17",
 * "isDelete":0
 * },
 * {
 * "id":4,
 * "userId":4,
 * "money":100.00,
 * "billTy pe":0,
 * "billStatus":2,
 * "billDetail":"没钱",
 * "createDate":"2018-05-30",
 * "isDelete":0
 * },
 * {
 * "id":6,
 * "userId":4,
 * "money":50.00,
 * "billType":1,
 * "billStatus":2,
 * "billDetail":"",
 * "createDate":"2018-05-30",
 * "isDelete":0
 * }
 * ],
 * "prePage":0,
 * "nextPage":0,
 * "isFirstPage":true,
 * "isLastPage":true,
 * "hasPreviousPage":false,
 * "hasNextPage":false,
 * "navigatePages":8,
 * "navigatepageNums":[
 * 1
 * ],
 * "navigateFirstPage":1,
 * "navigateLastPage":1,
 * "firstPage":1,
 * "lastPage":1
 * }
 * }
 * }
 */
public class WalletTradeResponse extends BaseResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private float income;
        private float pay;
        private PageDate page;

        public float getIncome() {
            return income;
        }

        public void setIncome(float income) {
            this.income = income;
        }

        public float getPay() {
            return pay;
        }

        public void setPay(float pay) {
            this.pay = pay;
        }

        public PageDate getPage() {
            return page;
        }

        public void setPage(PageDate page) {
            this.page = page;
        }
    }

    public class PageDate {
        private int pageNum;
        private int pageSize;
        private int total;
        private int pages;
        private List<TradeInfo> list;

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

        public List<TradeInfo> getList() {
            return list;
        }

        public void setList(List<TradeInfo> list) {
            this.list = list;
        }
    }

    public class TradeInfo {
        private int id;
        private int userId;
        private int billType;
        private int billStatus;
        private float money;
        private String billDetail;//失败原因
        private String createDate;

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

        public int getBillType() {
            return billType;
        }

        public void setBillType(int billType) {
            this.billType = billType;
        }

        public int getBillStatus() {
            return billStatus;
        }

        public void setBillStatus(int billStatus) {
            this.billStatus = billStatus;
        }

        public float getMoney() {
            return money;
        }

        public void setMoney(float money) {
            this.money = money;
        }

        public String getBillDetail() {
            return billDetail;
        }

        public void setBillDetail(String billDetail) {
            this.billDetail = billDetail;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }

}
