package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {
 * "code": 1,
 * "message": "公司规模信息",
 * "data": {
 * "scaleList": [
 * {
 * "id": 1,
 * "name": "少于50人"
 * },
 * {
 * "id": 2,
 * "name": "50-150人"
 * },
 * {
 * "id": 3,
 * "name": "150-500人"
 * },
 * {
 * "id": 4,
 * "name": "500-1000人"
 * },
 * {
 * "id": 5,
 * "name": "1000-5000人"
 * },
 * {
 * "id": 6,
 * "name": "5000-10000人"
 * },
 * {
 * "id": 7,
 * "name": "10000人以上"
 * }
 * ],
 * "natureList": [
 * {
 * "id": 1,
 * "name": "外资(欧美)"
 * },
 * {
 * "id": 2,
 * "name": "外资(非欧美)"
 * },
 * {
 * "id": 3,
 * "name": "合资"
 * },
 * {
 * "id": 4,
 * "name": "国企"
 * },
 * {
 * "id": 5,
 * "name": "民营公司"
 * },
 * {
 * "id": 6,
 * "name": "外企代表处"
 * },
 * {
 * "id": 7,
 * "name": "政府机关"
 * },
 * {
 * "id": 8,
 * "name": "事业单位"
 * },
 * {
 * "id": 9,
 * "name": "非营利组织"
 * },
 * {
 * "id": 10,
 * "name": "上市公司"
 * },
 * {
 * "id": 11,
 * "name": "创业公司"
 * }
 * ]
 * }
 * }
 */
public class CompanyNatureResponse extends BaseResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private List<ScaleInfo> scaleList;
        private List<NatureInfo> natureList;

        public List<ScaleInfo> getScaleList() {
            return scaleList;
        }

        public void setScaleList(List<ScaleInfo> scaleList) {
            this.scaleList = scaleList;
        }

        public List<NatureInfo> getNatureList() {
            return natureList;
        }

        public void setNatureList(List<NatureInfo> natureList) {
            this.natureList = natureList;
        }

    }

    public class ScaleInfo {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class NatureInfo {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
