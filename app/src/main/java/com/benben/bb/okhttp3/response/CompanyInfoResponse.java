package com.benben.bb.okhttp3.response;

import com.benben.bb.bean.CompanyInfo;
import com.benben.bb.bean.RecruitDetailInfo;

import java.util.List;

/**
 * {
 * "code": 1,
 * "message": "企业信息",
 * "data": {
 * "companyInfo": {
 * "id": 7,
 * "userId": 8,
 * "categoriesId": 71,
 * "companyName": "博世汽车部件（苏州）有限公司",
 * "representative": "法人",
 * "businessImg": "http://www.zhibenben.cn/upload/img/725686dc8d2e4d719d88feed27f71e88.jpeg,http://www.zhibenben.cn/upload/img/09d9218f35894785a495bd18be238d4e.jpeg",
 * "companyMien": "http://www.zhibenben.cn/upload/img/7a736adbc118495baf1b52952b742264.jpeg",
 * "companyAddr": "中新苏州工业园区苏虹西路126号博世汽车部件 (苏州)有限公司人力资源部",
 * "companyNature": 1,
 * "companySize": 1,
 * "companyRegion": "江苏省.苏州市.其他",
 * "auditStatus": 1,
 * "createDate": null,
 * "isDelete": null,
 * "introduction": "成立于1999年，总占地面积8万平方米，总投资3亿美元。汽车电子事业部的电子产品向客户提供了更好的乘员安全保证、驾驶辅助功能，提升驾乘舒适性。底盘控制系统为中国市场提供创新的主动安全系统，包括ABS防抱死制动系统，TCS牵引力控制系统和ESP电子稳定程序。底盘制动系统为中国市场提供先进的制动产品，包括前后单双缸盘式制动器， 真空助力器和制动主缸。汽车多媒体事业部负责生产蓝宝车载收音机。ATMO事业部作为博世内部供应商，提供生产装配系统及专用设备，设计和组装完整的机器及生产线。随着公司规模，业务的不断扩大，我们着力建立一支高技术，高素质的团队以适应新形势。我们欢迎各地英才的加盟，与博世一起，与中国汽车工业一起发展和进步。博世将为员工提供更好的职业前景和更优良的培训机会，我们在此诚邀您的加盟。",
 * "creditCode": "",
 * "po": true
 * },
 * "otherList": [
 * {
 * "id": 9,
 * "userId": 1,
 * "companyId": 7,
 * "positionName": "机械工程师",
 * "region": "全国",
 * "salary": 30,
 * "commision": 4,
 * "advSort": 1,
 * "houseImg": "",
 * "welfare": "公司福利",
 * "hiringCount": 3,
 * "endTime": "2018-08-31 00:00:00.0",
 * "payTime": "按企业实际发薪日",
 * "workTime": "每天工作8小时",
 * "focusSalary": "7000-10000",
 * "createDate": "2018-07-18 10:42:19.0",
 * "supplement": "",
 * "positionStatus": 0,
 * "isDelete": null,
 * "jobDemand": "学历：本科及以上，从事非标设备电气编程2年及以上，汽车行业优先。熟悉CAD, Solid Works，Proe，UG等机械制图软件。有单独做项目能力，适应出差，抗压能力强。（特别优秀者学历可放宽至大专）",
 * "dayworkHour": 8,
 * "monthworkDay": 22,
 * "enrollNum": 0,
 * "settlement": 0,
 * "commisionNum": 0,
 * "commisionDetails": "无",
 * "entrySex": "不限",
 * "entryAge": "24-35",
 * "staffCanteen": "餐费自理",
 * "staffHouse": "住宿自理",
 * "positionType": 1,
 * "po": true
 * },
 * {
 * "id": 10,
 * "userId": 1,
 * "companyId": 7,
 * "positionName": "机械调试",
 * "region": "全国",
 * "salary": 22,
 * "commision": 3,
 * "advSort": 1,
 * "houseImg": "",
 * "welfare": "公司福利",
 * "hiringCount": 2,
 * "endTime": "2018-08-31 00:00:00.0",
 * "payTime": "按企业实际发薪日",
 * "workTime": "每天工作8小时",
 * "focusSalary": "5500-7000",
 * "createDate": "2018-07-18 10:44:59.0",
 * "supplement": "",
 * "positionStatus": 0,
 * "isDelete": null,
 * "jobDemand": "学历：中专及以上，有2年以上机械装配，机械调试，现场生产线的改造维护经验，能适应出差。",
 * "dayworkHour": 8,
 * "monthworkDay": 22,
 * "enrollNum": 0,
 * "settlement": 0,
 * "commisionNum": 0,
 * "commisionDetails": "无",
 * "entrySex": "男",
 * "entryAge": "22-38",
 * "staffCanteen": "餐费自理",
 * "staffHouse": "住宿自理",
 * "positionType": 1,
 * "po": true
 * }
 * ],
 * "defaultList": [
 * {
 * "id": 7,
 * "userId": 1,
 * "companyId": 7,
 * "positionName": "电气工程师",
 * "region": "全国",
 * "salary": 33,
 * "commision": 4,
 * "advSort": 2,
 * "houseImg": "",
 * "welfare": "公司福利",
 * "hiringCount": 2,
 * "endTime": "2018-08-31 00:00:00.0",
 * "payTime": "按企业实际发薪日",
 * "workTime": "8",
 * "focusSalary": "8000-13000",
 * "createDate": "2018-07-18 10:38:55.0",
 * "supplement": "",
 * "positionStatus": 0,
 * "isDelete": null,
 * "jobDemand": "学历本科及以上，从事非标设备电气编程2年及以上，熟悉西门子PLC300，倍福等编程软件。有单独做项目能力，适应出差，抗压能力强。（特别优秀者学历可放宽至大专）",
 * "dayworkHour": 8,
 * "monthworkDay": 22,
 * "enrollNum": 0,
 * "settlement": 0,
 * "commisionNum": 0,
 * "commisionDetails": "无",
 * "entrySex": "不限",
 * "entryAge": "24-35",
 * "staffCanteen": "餐费自理",
 * "staffHouse": "住宿自理",
 * "positionType": 0,
 * "po": true
 * }
 * ]
 * }
 * }
 */
public class CompanyInfoResponse extends BaseResponse {

    private DataMap data;

    public DataMap getData() {
        return data;
    }

    public void setData(DataMap data) {
        this.data = data;
    }

    public class DataMap {
        private CompanyInfo companyInfo;
        private List<RecruitDetailInfo> defaultList;
        private List<RecruitDetailInfo> otherList;

        public CompanyInfo getCompanyInfo() {
            return companyInfo;
        }

        public void setCompanyInfo(CompanyInfo companyInfo) {
            this.companyInfo = companyInfo;
        }

        public List<RecruitDetailInfo> getDefaultList() {
            return defaultList;
        }

        public void setDefaultList(List<RecruitDetailInfo> defaultList) {
            this.defaultList = defaultList;
        }

        public List<RecruitDetailInfo> getOtherList() {
            return otherList;
        }

        public void setOtherList(List<RecruitDetailInfo> otherList) {
            this.otherList = otherList;
        }
    }


}
