package com.benben.bb.okhttp3.response;

import com.benben.bb.bean.RecruitDetailInfo;

/**
 "code": 1,
 "message": "职位信息",
 "data": {
 "phone": "17670751181",
 "positionInfo": {
 "id": 1,
 "userId": null,
 "companyId": 4,
 "positionName": "操作工",
 "region": "湖南省.长沙市.长沙县",
 "salary": 17.5,
 "commision": 2.5,
 "advSort": 1,
 "houseImg": "",
 "welfare": "餐补,五险一金,年终奖,年中奖,不定期奖金,高温费,免费班车,车补,免费体检",
 "hiringCount": 50,
 "endTime": "2018-08-31 00:00:00.0",
 "payTime": "按企业实际发薪日",
 "workTime": "正常工作5天8小时，两班倒",
 "focusSalary": "3800-4200",
 "createDate": "2018-07-17 11:52:10.0",
 "supplement": "",
 "positionStatus": 0,
 "isDelete": null,
 "jobDemand": "1、初中以上学历，敬业爱岗、吃苦耐劳，适应晚班，有团队合作精神，1年以上仓库工作经验者优先。2、有效身份证原件及正反面复印件4张，有效叉车证原件及复印件2张，一寸蓝底照片3张，黑色水性笔一支及合格的体检报告，入职当天下午安排宿舍。所有岗位入职在公司住宿的员工，非休息日或未经批准不得擅自离宿，经宿管查房超过三次以上不在宿将直接清退宿舍。",
 "dayworkHour": 8,
 "monthworkDay": 22,
 "enrollNum": 0,
 "settlement": 0,
 "commisionNum": 0,
 "commisionDetails": "结3个月",
 "entrySex": "男",
 "entryAge": "18-35",
 "staffCanteen": "餐补312元/月、全套床上用品",
 "staffHouse": "免费宿舍四人间，带空调、热水器、洗衣机和无线网（水电自理）",
 "positionType": null,
 "companyMien": "http://www.zhibenben.cn/upload/img/180a6862fd9e42bdadb5f9de939657b4.jpeg",
 "introduction": "一、企业简介：安吉物流是上汽集团旗下全资子公司。安吉智行物流有限公司长沙分公司位于长沙经济技术开发区，主要从事上海大众汽车物流配套服务。上海安吉汽车零部件物流有限公司是国内汽车物流业首家经国家交通部、外经贸部正式批准的汽车物流中外合资企业。公司注册资本为3000万美元，中外双方各占50%股份。公司主要从事与汽车零部件相关的物流和汽车相关的国内货运代理服务、整车仓储、物流技术咨询、规划、管理、培训等服务以及国际货运代理；汽车零部件批发、进出口及相关配套服务。是一家专业化运作，能为客户提供一体化、技术化、网络化、可靠的、独特解决方案的第三方物流供应商。全国范围有6家合资公司、16家分公司。\n二、工资待遇：正常工作5天8小时，两班倒，底薪2300元/月 + 5天8小时外加班费1.5倍/3倍工资（综合月薪可达3800-4200以上)，餐补312元/月 + 全套床上用品+转正购买五险一金 + 免费宿舍（高档4人间、空调、热水器、洗衣机、无线网络等）水电费自理+ 年中奖、年终奖、不定期奖金 + 工会福利+高温费、团队建设费等 + 年度体检 + 免费班车及车补等。每月25日发放上一月工资。",
 "companyRegion": "湖南省.长沙市.其他",
 "companyAddr": "上汽大众产业园区",
 "po": true
 },
 "companyName": "安吉智行物流有限公司"
 }
 */
public class RecruitDetailResponse extends BaseResponse {

    private DataMap data;

    public DataMap getData() {
        return data;
    }

    public void setData(DataMap data) {
        this.data = data;
    }

    public class DataMap {
        private String phone;
        private String companyName;
        private RecruitDetailInfo positionInfo;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public RecruitDetailInfo getPositionInfo() {
            return positionInfo;
        }

        public void setPositionInfo(RecruitDetailInfo positionInfo) {
            this.positionInfo = positionInfo;
        }
    }
}
