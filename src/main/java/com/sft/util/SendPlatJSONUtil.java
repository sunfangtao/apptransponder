package com.sft.util;

import com.google.gson.Gson;

public class SendPlatJSONUtil {

    private static class ReturnJson {
        public int code;
        public int count;
        public String msg;
        public Object data;
    }

    /**
     * 列表查询返回数据，带有页码（如果请求中带有页码的话）

     * @param object
     * @return
     */
    public static String getPageJsonString(int code, String message, int count, Object object) {

        ReturnJson returnJson = new ReturnJson();
        returnJson.code = code;
        returnJson.msg = message;
        returnJson.count = count;
        returnJson.data = object;

        return new Gson().toJson(returnJson);
    }

}
