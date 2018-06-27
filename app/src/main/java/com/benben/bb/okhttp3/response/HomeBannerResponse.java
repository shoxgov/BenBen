package com.benben.bb.okhttp3.response;

import java.util.List;

/**
 * {"code":1,"message":"顶部广告信息",
 * "data":
 * [
 * {"href":"http://120.79.155.10/adshow?id=1","pic":"http://120.79.155.10/upload/img/banner1.png","title":"职犇犇APP上线啦"},
 * {"href":"http://120.79.155.10/adshow?id=2","pic":"http://120.79.155.10/upload/img/banner2.png","title":"做个抬头族，找份好工作"},
 * {"href":"http://120.79.155.10/adshow?id=3","pic":"http://120.79.155.10/upload/img/banner3.png","title":"掌握犇犇，共赢天下"}]}
 */
public class HomeBannerResponse extends BaseResponse {

    private List<BannerInfo> data;

    public List<BannerInfo> getData() {
        return data;
    }

    public void setData(List<BannerInfo> data) {
        this.data = data;
    }

    public class BannerInfo {
        private String href;
        private String pic;
        private String title;
        private String positionName;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }
    }

}
