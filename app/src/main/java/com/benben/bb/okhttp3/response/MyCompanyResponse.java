package com.benben.bb.okhttp3.response;

import com.benben.bb.bean.CompanyInfo;

/**
 * {
 * "code": 1,
 * "message": "企业信息",
 * "data": {
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
 * }
 * }
 */
public class MyCompanyResponse extends BaseResponse {

    private CompanyInfo data;

    public CompanyInfo getData() {
        return data;
    }

    public void setData(CompanyInfo data) {
        this.data = data;
    }

}
