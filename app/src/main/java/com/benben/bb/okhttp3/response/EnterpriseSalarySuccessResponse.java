package com.benben.bb.okhttp3.response;

import java.io.Serializable;

/**
 * {"code":1,"message":"保存成功","data":{"bankCard":"6212265588554787","bankType":"长沙银行","wagesCode":"BEIZHU-9"}}
 */
public class EnterpriseSalarySuccessResponse extends BaseResponse {
    private DateMap data;

    public DateMap getData() {
        return data;
    }

    public void setData(DateMap data) {
        this.data = data;
    }

    public class DateMap {
        private String bankCard;
        private String bankType;
        private String wagesCode;

        public String getBankCard() {
            return bankCard;
        }

        public void setBankCard(String bankCard) {
            this.bankCard = bankCard;
        }

        public String getBankType() {
            return bankType;
        }

        public void setBankType(String bankType) {
            this.bankType = bankType;
        }

        public String getWagesCode() {
            return wagesCode;
        }

        public void setWagesCode(String wagesCode) {
            this.wagesCode = wagesCode;
        }
    }
}
