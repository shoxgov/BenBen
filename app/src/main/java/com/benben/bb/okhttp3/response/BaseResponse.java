package com.benben.bb.okhttp3.response;

import java.io.Serializable;

/**
 * "code": 1,
 * "message": "注册成功",
 */
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;//code值含义:* 1表示正常* 0表示错误
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
