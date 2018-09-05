package com.benben.bb;

public class NetWorkConfig {
    public static String DOWNLOAD_APK = "http://www.ltx360.cn/apk/LTX.apk";
    public static String H5HOST;
    public static String HTTP;
    public static String HTTPS;
    public static String IMAGEPATH;
    public static String IMAGEUPLOAD;

    public static int networkEnv = 1;// 0 现场 1 现网测试 2 内网测试环境公网

    static {
        switch (networkEnv) {
            default:
            case 0:
                H5HOST = "http://www.zhibenben.cn/join";
                HTTP = "http://www.ltx360.cn";
                HTTPS = "https://www.ltx360.cn";
                IMAGEPATH = "http://www.ltx360.cn/";
                IMAGEUPLOAD = "http://www.ltx360.cn/classmate/interface/mobile/FileUpload";
                break;
            case 1:
                H5HOST = "http://www.zhibenben.cn/join";
                HTTP = "http://www.zhibenben.cn";
                HTTPS = "http://www.zhibenben.cn";
                IMAGEPATH = "http://www.zhibenben.cn/";
                IMAGEUPLOAD = "http://www.zhibenben.cn/classmate/interface/mobile/FileUpload";
                break;
            case 2:
                H5HOST = "http://192.168.0.111/page/appHtml/";
                HTTP = "http://192.168.0.110";
                HTTPS = "http://192.168.0.110";
                IMAGEPATH = "https://119.23.12.139/";
                IMAGEUPLOAD = "http://119.23.12.139/classmate/interface/mobile/FileUpload";
                break;
        }
    }

    /**
     * 获取验证码
     */
    public static final String OBTAIN_TEL_CODE = HTTP + "/user/account/sendCode";
    /**
     * 登录
     */
    public static final String LOGIN = HTTP + "/user/account/login";
    /**
     * 用户注册
     */
    public static final String REGISTER = HTTP + "/user/account/register";
    /**
     * 修改密码
     */
    public static final String RESET_PWD = HTTP + "/user/account/resetPassword";
    /**
     获取用户信息
     */
    public static final String GET_USERINFO = HTTP + "/api/user/getUserInfo";
    /**
     * 获取首页Banner
     */
    public static final String HOME_BANNER = HTTP + "/api/index/getAdTop";
    /**
     * 首页推荐
     * */
    public static final String INDEX_HOME_ADV = HTTP + "/api/index/advPositionList";
    /**
     * 查询或搜索职位列表
     */
    public static final String INDEX_RECRUIT_SEARCH = HTTP + "/api/index/searchPositionList";
    /**
     * GET /api/index/getPositionInfo
     获取职位详情信息
     */
    public static final String INDEX_RECRUIT_DETAIL = HTTP + "/api/index/getPositionInfo";
    /**
     * 实名认证
     */
    public static final String REALNAME_CERTIFY = HTTP + "/api/user/checkTrueName";
    /**
     * 获取已报名职位列表
     */
    public static final String BROKER_GET_POSITION = HTTP + "/api/agent/getEnrollPositionList";
    /**
     * 获取我的资源已报名 GET /api/agent/getEnrollPositionInfo
     * 获取已报名或已入职用户列表
     */
    public static final String BROKER_GET_POSITION_USERLIST = HTTP + "/api/agent/getEnrollPositionInfo";
    /**
     * 获取我的就业顾问列表
     */
    public static final String BROKER_AGENTS_LIST = HTTP + "/api/agent/getMyAgentList";
    /**
     * 获取新增就业顾问列表
     */
    public static final String BROKER_AGENTS_ADD_LIST = HTTP + "/api/agent/getNewAgentList";
    /**
     * 认证经纪人
     */
    public static final String BROKER_CHECK_AGENTS = HTTP + "/api/agent/checkAgent";
    /**
     * 添加经纪人
     */
    public static final String BROKER_ADD_AGENTS = HTTP + "/api/agent/addMyAgent";
    /**
     * 取消经纪人
     */
    public static final String BROKER_DEL_AGENTS = HTTP + "/api/agent/delMyAgent";
    /**
     * 获取资源数
     */
    public static final String BROKER_RESOURCE_COUNTS = HTTP + "/api/agent/getMatterSum";
    /**
     * 获取经纪人可报名资源列表
     */
    public static final String BROKER_RESOURCE_ISCAN_ENTRY = HTTP + "/api/agent/getResourcesList";
    /**
     * 企业认证
     */
    public static final String COMPANY_CERTIFY = HTTP + "/api/company/checkCompany";
    /**
     * 企业信息
     */
    public static final String GET_COMPANY_INFO = HTTP + "/api/company/getCompanyInfo";
    /**
     获取默认企业信息
     */
    public static final String GET_MY_COMPANY = HTTP + "/api/company/getMyCompany";
    /**
     * 获取合作企业列表
     */
    public static final String GET_COOPERATE_ENTERPRISE = HTTP + "/api/company/getHZCompanyList";
    /**
     * 搜索合作企业
     */
    public static final String SEARCH_COOPERATE_ENTERPRISE = HTTP + "/api/company/searchCompany";
    /**
     * 确定合作
     */
    public static final String ADD_COOPERATE_ENTERPRISE = HTTP + "/api/company/addHZCompany";
    /**
     新增合作企业
     */
    public static final String SAVE_COOPERATE_ENTERPRISE_INFO = HTTP + "/api/company/saveCompany";
    /**
     * 合作企业开关
     */
    public static final String COOPERATE_ENTERPRISE_OPEN = HTTP + "/api/company/upCompanyStatus";
    /**
     * 修改企业信息
     */
    public static final String GET_COMPANY_INFO_SAVE = HTTP + "/api/company/updateCompany";
    /**
     * 公司规模性质信息
     */
    public static final String GET_COMPANY_NATURE = HTTP + "/api/index/getCompanyScaleList";
    /**
     * 企业行业分类
     */
    public static final String GET_COMPANY_CATEGORY = HTTP + "/api/index/getCategoryList";
    /**
     *GET /api/user/getResourcesList
     获取邀请二维码用户列表
     */
    public static final String GET_MY_INVITE_RESOURCE = HTTP + "/api/agent/getMatterList";
    /**
     * 获取企业招聘信息列表
     */
    public static final String COMPANY_RECRUIT_LIST = HTTP + "/api/company/getMyPositionList";
    /**
     * 获取企业招聘信息详情
     */
    public static final String COMPANY_RECRUIT_DETAIL = HTTP + "/api/company/getMyPositionInfo";
    /**
     * 新增或修改招聘信息
     */
    public static final String COMPANY_RECRUIT_ADD = HTTP + "/api/company/saveOrUpPositionInfo";
    /**
     * 获取企业应聘管理列表
     */
    public static final String COMPANY_EMPLOYEE_LIST = HTTP + "/api/company/getMyRecruitList";
    /**
     * 获取企业应聘管理详情列表
     */
    public static final String COMPANY_EMPLOYEE_DETAIL_LIST = HTTP + "/api/company/getRecruitInfoList";
    /**
     * 新增修改用户职位中间表信息(同意-辞退操作)
     * //entryStatus 0 取消报名-用户;1 取消报名-系统;2 取消报名-企业;3 取消报名-经纪人;77 报名中-经纪人代报名;88 报名中-待系统审核;89 报名中-待企业审核;90 待入职;99 已入职;91辞退
     */
    public static final String COMPANY_EMPLOYEE_USER_STATUS = HTTP + "/api/company/saveOrUpPositionUser";
    /**
     * /api/company/dismissUser   辞退操作调用
     增加两个参数  userId, positionId
     报名接口   已报名返回状态码 100  ， msg 内容为当前报名状态
     */
    public static final String COMPANY_EMPLOYEE_USER_DISMISS = HTTP + "/api/company/dismissUser";
    /**
     * 批量新增工资核算
     */
    public static final String COMPANY_SAVE_SALARY = HTTP + "/api/company/saveWagesInfo";
    /**
     * 工资核算历史记录
     */
    public static final String COMPANY_SALARY_HISTORY = HTTP + "/api/company/getWagesList";
    /**
     * 查询工资核算详情List
     */
    public static final String COMPANY_SALARY_HISTORY_DETAIL = HTTP + "/api/company/getWagesInfoList";
    /**
     * 更新个人信息
     */
    public static final String USER_UPDATEINFO = HTTP + "/api/user/updateUser";
    /**
     * 用户报名
     */
    public static final String USER_SIGNUP_EVENT = HTTP + "/api/agent/userSignUp";
    /**
     * 自已应聘职位
     */
    public static final String USER_AGREEN_SIGNUP_EVENT = HTTP + "/api/user/upSignUpStatus";
    /**
     * 我的报名列表
     */
    public static final String USER_MY_SIGNUP = HTTP + "/api/user/getMySignUpList";
    /**
     * 工资条列表
     */
    public static final String USER_SALARY_LIST = HTTP + "/api/user/getMyWagesList";
    /**
     * 工资条申诉
     */
    public static final String USER_SALARY_LIST_APPEAL = HTTP + "/api/user/shengsu";
    /**
     * 入账出账明细
     */
    public static final String USER_TRADE_DETAIL = HTTP + "/api/user/getBillList";
    /**
     * 提现申请
     */
    public static final String USER_WITHDRAW = HTTP + "/api/user/tixian";
}
