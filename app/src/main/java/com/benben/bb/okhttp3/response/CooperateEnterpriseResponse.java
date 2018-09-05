package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {
 * "code": 1,
 * "message": "查询或搜索职位列表",
 * "data": {
 * "pageNum": 1,
 * "pageSize": 5,
 * "size": 3,
 * "startRow": 1,
 * "endRow": 3,
 * "total": 3,
 * "pages": 1,
 * "list": [
 * {
 * "id": 2,
 * "uid": 33,
 * "eid": 8,
 * "companyType": 1,
 * "status": 0,
 * "createDate": null,
 * "companyId": 8,
 * "companyName": "美团点评",
 * "companyRegion": "江苏省.扬州市.广陵区"
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
public class CooperateEnterpriseResponse extends BaseResponse {

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
        private List<CooperateEnterprise> list;

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

        public List<CooperateEnterprise> getList() {
            return list;
        }

        public void setList(List<CooperateEnterprise> list) {
            this.list = list;
        }
    }

    public class CooperateEnterprise {
        private int id;
        private int uid;
        private int eid;
        private int companyType;
        private int status;
        private int companyId;
        private String companyName;
        private String companyRegion;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getEid() {
            return eid;
        }

        public void setEid(int eid) {
            this.eid = eid;
        }

        public int getCompanyType() {
            return companyType;
        }

        public void setCompanyType(int companyType) {
            this.companyType = companyType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyRegion() {
            return companyRegion;
        }

        public void setCompanyRegion(String companyRegion) {
            this.companyRegion = companyRegion;
        }
    }
}
