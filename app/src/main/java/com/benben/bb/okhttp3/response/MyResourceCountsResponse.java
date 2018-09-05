package com.benben.bb.okhttp3.response;

/**
 *   "data": {
 "entryNum": 0,
 "signNum": 0,
 "resourcesNum": 1,
 "enrollNum": 0
 }
 报名中 signNum,已入职entryNum,我的资源resourcesNum,可报名enrollNum
 */
public class MyResourceCountsResponse extends BaseResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private int signNum;
        private int entryNum;
        private int resourcesNum;
        private int enrollNum;

        public int getSignNum() {
            return signNum;
        }

        public void setSignNum(int signNum) {
            this.signNum = signNum;
        }

        public int getEntryNum() {
            return entryNum;
        }

        public void setEntryNum(int entryNum) {
            this.entryNum = entryNum;
        }

        public int getResourcesNum() {
            return resourcesNum;
        }

        public void setResourcesNum(int resourcesNum) {
            this.resourcesNum = resourcesNum;
        }

        public int getEnrollNum() {
            return enrollNum;
        }

        public void setEnrollNum(int enrollNum) {
            this.enrollNum = enrollNum;
        }
    }

}
