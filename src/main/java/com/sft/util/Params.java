package com.sft.util;

/**
 * 平台上的错误信息
 */
public class Params {

    public static final String SERVER_ID = "serverId";
    public static final String USER_ID = "userId";

    /**
     * 返回结果
     */
    public enum SqlType {

        USER_INFO("userInfo"),
        TYPE_URL("typeUrl"),
        PERMISSION("permission"),
        ROLE("role"),;

        private String value;

        SqlType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

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
