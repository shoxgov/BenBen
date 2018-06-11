package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {
 * "code": 1,
 * "message": "行业分类信息",
 * "data": [
 * {
 * "name": "计算机/互联网/通信/电子",
 * "companyCategory": [
 * {
 * "id": 12,
 * "name": "计算机软件",
 * "pid": 1,
 * "paixu": null,
 * "isdel": null,
 * "crtime": null
 * }
 * ],
 * "pid": null,
 * "id": 1
 * },
 * {
 * "name": "政府/非营利组织/其他",
 * "companyCategory": [
 * {
 * "id": 66,
 * "name": "政府/公共事业",
 * "pid": 11,
 * "paixu": null,
 * "isdel": null,
 * "crtime": null
 * }
 * ],
 * "pid": null,
 * "id": 11
 * }
 * ]
 * }
 */
public class CompanyCategoryResponse extends BaseResponse {

    private List<CategoryFirst> data;

    public List<CategoryFirst> getData() {
        return data;
    }

    public void setData(List<CategoryFirst> data) {
        this.data = data;
    }

    public class CategoryFirst {
        private String name;
        private int id;
        private List<CategorySecond> companyCategory;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<CategorySecond> getCompanyCategory() {
            return companyCategory;
        }

        public void setCompanyCategory(List<CategorySecond> companyCategory) {
            this.companyCategory = companyCategory;
        }
    }

    public class CategorySecond {
        private int id;
        private int pid;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
