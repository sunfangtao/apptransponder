package com.sft.util;

/**
 * 平台上的错误信息
 */
public class Params {

    /**
     * 返回结果
     */
    public enum ResultEnum {

        SUCCESS("success"),
        FAIL("fail"),;

        private String value;

        ResultEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Json返回的类型
     */
    public enum ReasonEnum {
        REPEAT("RepeatException"),
        NODATA("NoDataException"),
        SQLEXCEPTION("SqlException"),
        SERVEREXCEPTION("ServerException"),
        NORMAL("Normal"),
        PERMISSION("Permission"),
        NOTLOGIN("NoLogin"),
        PASSWORDERROR("PasswordErrorException"),
        NOACCOUNT("UnkonwAccountException"),
        NOREQUIREPARAMS("NoRequireParams"),
        IOException("IOException"),
        NOMOREDATAEXCEPTION("NoMoreDataException");
        private String value;

        ReasonEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
