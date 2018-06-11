package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {
 * "code": 1,
 * "message": "工资条列表",
 * "data": {
 * "pageNum": 1,
 * "pageSize": 5,
 * "size": 1,
 * "startRow": 1,
 * "endRow": 1,
 * "total": 1,
 * "pages": 1,
 * "list": [
 * {
 * "id": 10,
 * "userId": 9,
 * "companyId": 6,
 * "positionId": 12,
 * "wagesTitle": "SMT制程工程师工时统计表",
 * "wagesMonth": "2018-06",
 * "wagesCode": "1111",
 * "wagesStatus": 0,
 * "wagesSum": 200,
 * "commissionSum": 600,
 * "payonCount": 0,
 * "payoffCount": 0,
 * "createDate": null,
 * "isDelete": null,
 * "salary": 100,
 * "commision": 50,
 * "wagesInfos": null
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
public class EnterpriseSalaryHistoryResponse extends BaseResponse {

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
        private List<SalaryInfo> list;

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

        public List<SalaryInfo> getList() {
            return list;
        }

        public void setList(List<SalaryInfo> list) {
            this.list = list;
        }
    }

    public class SalaryInfo {
        private int id;
        private int userId;
        private int companyId;
        private int positionId;
        private float salary;
        private float wagesSum;
        private float commision;
        private String createDate;
        private String wagesTitle;
        private String wagesMonth;
        private String wagesStatus;
        private String wagesInfos;
        private String wagesCode;
        private String isDelete;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        public float getSalary() {
            return salary;
        }

        public void setSalary(float salary) {
            this.salary = salary;
        }

        public float getWagesSum() {
            return wagesSum;
        }

        public void setWagesSum(float wagesSum) {
            this.wagesSum = wagesSum;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }


        public String getWagesStatus() {
            return wagesStatus;
        }

        public void setWagesStatus(String wagesStatus) {
            this.wagesStatus = wagesStatus;
        }


        public String getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
        }

        public String getWagesTitle() {
            return wagesTitle;
        }

        public void setWagesTitle(String wagesTitle) {
            this.wagesTitle = wagesTitle;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public int getPositionId() {
            return positionId;
        }

        public void setPositionId(int positionId) {
            this.positionId = positionId;
        }

        public float getCommision() {
            return commision;
        }

        public void setCommision(float commision) {
            this.commision = commision;
        }

        public String getWagesMonth() {
            return wagesMonth;
        }

        public void setWagesMonth(String wagesMonth) {
            this.wagesMonth = wagesMonth;
        }

        public String getWagesInfos() {
            return wagesInfos;
        }

        public void setWagesInfos(String wagesInfos) {
            this.wagesInfos = wagesInfos;
        }

        public String getWagesCode() {
            return wagesCode;
        }

        public void setWagesCode(String wagesCode) {
            this.wagesCode = wagesCode;
        }
    }

}
