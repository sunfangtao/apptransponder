package com.sft.util;

/**
 * 平台上的错误信息
 */
public class Params {

    public static void main(String[] args) {
        // 贷款利率
        float x = 0.0068f;
        // 贷款年限
        float year = 3f;
        // 首付比率
        float rate = 0.3f;
        // 总价格
        float total = 2.4f;

        float first = (int) (total * rate * 100)/100f;
        int month = (int) Math.ceil(total * (1 - rate) * (1 / (12 * year) + x) * 10000);
        System.out.println("first=" + first + " month=" + month);

        String fileName = "/data/APK/apache-tomcat-8.5.11/webapps/apptransponder/金融.png";
        String downLoadUrl = "http://221.0.91.34:3082" + fileName.substring(fileName.lastIndexOf("webapps") + "webapps".length());
        System.out.println(downLoadUrl);
    }

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
