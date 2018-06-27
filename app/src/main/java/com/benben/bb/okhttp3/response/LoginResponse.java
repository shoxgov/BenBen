package com.benben.bb.okhttp3.response;

import java.io.Serializable;

/**
 * {
 * "code": 1,
 * "id": 4,
 * "userName": "17773119860",
 * "passWord": "7403",
 * "benbenNum": "10004",
 * "nickName": "王生银",
 * "avatar": "120.79.155.104",
 * "region": "",
 * "sex": 0,
 * "age": "",
 * "nation": "",
 * "education": "",
 * "major": "",
 * "signature": "",
 * "trueName": "银华",
 * "identityCard": "1234569852",
 * "birthDate": "2018-05-22 18:36:44",
 * "finishSchool": "",
 * "studyStatus": 0,
 * "balance": 0,
 * "idcardFan": "/upload/img/e89e47ca0fd24a1d95ee10dc2cd8bea1.jpeg",
 * "idcardZheng": "/upload/img/7a4832fbfc5741ffbda4bfc30b64b09c.jpeg",
 * "bindingPaytype": "",
 * "bindingPayAccount": "",
 * "validateStatus": 1,
 * "djBalance": 0,
 * "isAgent": 0,
 * "isCompany": 0,
 * "userStatus": 0,
 * "walletStatus": 0,
 * "userCode": "10001",
 * "createDate": 1525338532000,
 * "createUser": ""
 * },
 * "token": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0MzE2OTBlNGMxMzQ0N2Q4OTIyZmZlMjczMzg1ZWNlYiIsImlzcyI6Imp3dC1iZW5iZW4iLCJzdWIiOiJ7XCJ1c2VySWRcIjpcIjRcIixcInVzZXJOYW1lXCI6XCIxNzc3MzExOTg2MFwifSIsImlhdCI6MTUyNjk3NjgyNCwiZXhwIjoxNTI3MDIwMDI0fQ.VaUQWxgGR_FvQa21GFb01HvkcveopGyl22o-fdBqyXE"
 * }
 * }
 */
public class LoginResponse extends BaseResponse {

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
