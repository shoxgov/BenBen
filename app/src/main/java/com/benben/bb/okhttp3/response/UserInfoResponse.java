package com.benben.bb.okhttp3.response;

/**
 * "code": 1,
 * "message": "用户信息",
 * "data": {
 * "id": 1,
 * "userName": "18666637414",
 * "passWord": null,
 * "benbenNum": "zbb10001",
 * "nickName": "系统客服",
 * "avatar": "http://www.zhibenben.cn/upload/img/favicon.ico",
 * "region": "湖南省长沙市岳麓区",
 * "sex": 1,
 * "age": "",
 * "nation": "",
 * "education": "0",
 * "major": "神经科",
 * "signature": "",
 * "trueName": "刘江波",
 * "identityCard": "430523199011037611",
 * "birthDate": "2018-05-22 18:36:32",
 * "finishSchool": "祁东大学",
 * "studyStatus": 0,
 * "balance": 200.4,
 * "idcardFan": "http://www.zhibenben.cnhttp://www.zhibenben.cn/upload/img/0b36cc8e3d8d4470a949a263cbbb8e49.jpeg",
 * "idcardZheng": "http://www.zhibenben.cnhttp://www.zhibenben.cn/upload/img/72ee75a51ca44e90b4e605e02ecd12b9.jpeg",
 * "bindingPaytype": "",
 * "bindingPayAccount": "",
 * "validateStatus": 3,
 * "djBalance": 0,
 * "isAgent": 0,
 * "isCompany": 0,
 * "userStatus": 99,
 * "walletStatus": 0,
 * "userCode": "",
 * "createDate": 1525338532000,
 * "createUser": "13456783169",
 * "po": true
 * }
 */
public class UserInfoResponse extends BaseResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String token;
        private UserInfomation user;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public UserInfomation getUser() {
            return user;
        }

        public void setUser(UserInfomation user) {
            this.user = user;
        }
    }
}
