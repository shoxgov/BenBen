package com.benben.bb.okhttp3.response;

/**
 * "code":1, "message":"验证码已发送", "data":"1823"
 */
public class SmsCodeResponse extends BaseResponse {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
