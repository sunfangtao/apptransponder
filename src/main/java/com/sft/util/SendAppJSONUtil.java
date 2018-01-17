package com.sft.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

public class SendAppJSONUtil {

    private static class Json {
        public Object array;
        public Object info;
    }

    private static class PageJson {
        public int page;
        public int pageSize;
        public int count;
        public Object array;
        public Object info;
    }

    private static class ReturnJson {
        public String result;
        public String type;
        public String message;
        public Object data;
    }

    private static class StringJson {
        public String res;
    }

    /**
     * 列表查询返回数据，带有页码（如果请求中带有页码的话）
     *
     * @param page
     * @param pageSize
     * @param object
     * @return
     */
    public static String getPageJsonString(int page, int pageSize, int count, Object object) {

        PageJson data = new PageJson();
        if (page >= 0 && pageSize > 0) {
            data.page = page;
            data.pageSize = pageSize;
        }
        if (count > 0) {
            data.count = count;
        }
        data.array = object;

        ReturnJson returnJson = new ReturnJson();
        returnJson.result = Params.ResultEnum.SUCCESS.getValue();
        returnJson.type = Params.ReasonEnum.NORMAL.getValue();
        returnJson.message = "";
        returnJson.data = data;

        return new Gson().toJson(returnJson);
    }

    public static String getNoDataString(int page, int pageSize, int count, String... message) {
        PageJson data = new PageJson();
        if (page >= 0 && pageSize > 0) {
            data.page = page;
            data.pageSize = pageSize;
        }
        if (count > 0) {
            data.count = count;
        }
        data.array = "[]";

        ReturnJson returnJson = new ReturnJson();
        returnJson.result = Params.ResultEnum.FAIL.getValue();
        returnJson.type = Params.ReasonEnum.NODATA.getValue();
        returnJson.message = message.length > 0 ? message[0] : "没有数据";
        returnJson.data = data;

        return new Gson().toJson(returnJson);
    }

    public static String getNoMoreDataString(int page, int pageSize, int count, String... message) {
        PageJson data = new PageJson();
        if (page >= 0 && pageSize > 0) {
            data.page = page;
            data.pageSize = pageSize;
        }
        if (count > 0) {
            data.count = count;
        }
        data.array = "[]";

        ReturnJson returnJson = new ReturnJson();
        returnJson.result = Params.ResultEnum.FAIL.getValue();
        returnJson.type = Params.ReasonEnum.NOMOREDATAEXCEPTION.getValue();
        returnJson.message = message.length > 0 ? message[0] : "没有更多数据";
        returnJson.data = data;

        return new Gson().toJson(returnJson);
    }

    /**
     * 正常返回的Json
     *
     * @param object 用户查询的数据
     * @return
     */
    public static String getNormalString(Object object) {
        ReturnJson returnJson = new ReturnJson();
        returnJson.result = Params.ResultEnum.SUCCESS.getValue();
        returnJson.type = Params.ReasonEnum.NORMAL.getValue();
        returnJson.message = "";
        returnJson.data = new JsonObject();

        if (object != null) {
            Json json = new Json();
            if (object instanceof Map || object instanceof List) {
                json.array = object;
            } else if (object instanceof String) {
                returnJson.message = (String) object;
            } else {
                json.info = object;
            }
            returnJson.data = json;
        }
        return new Gson().toJson(returnJson);
    }

    /**
     * 正常返回的Json
     *
     * @param info 用户查询的数据
     * @return
     */
    public static String getNormalStringInfo(String info) {
        ReturnJson returnJson = new ReturnJson();
        returnJson.result = Params.ResultEnum.SUCCESS.getValue();
        returnJson.type = Params.ReasonEnum.NORMAL.getValue();
        returnJson.message = "";

        if (info != null) {
            StringJson stringJson = new StringJson();
            stringJson.res = info;
            Json json = new Json();
            json.info = stringJson;
            returnJson.data = json;
        }
        return new Gson().toJson(returnJson);
    }

    /**
     * 必要参数缺失返回的json
     *
     * @param message 提示用户的内容
     * @return
     */
    public static String getRequireParamsMissingObject(String message) {
        ReturnJson returnJson = new ReturnJson();
        returnJson.result = Params.ResultEnum.FAIL.getValue();
        returnJson.type = Params.ReasonEnum.NOREQUIREPARAMS.getValue();
        returnJson.message = (message == null ? "缺少参数" : message);
        returnJson.data = "{}";

        return new Gson().toJson(returnJson);
    }

    /**
     * 查询结果为空返回的json
     *
     * @return
     */
    public static String getNullResultObject() {
        ReturnJson returnJson = new ReturnJson();
        returnJson.result = Params.ResultEnum.FAIL.getValue();
        returnJson.type = Params.ReasonEnum.NODATA.getValue();
        returnJson.message = "没有查询到结果";
        returnJson.data = "{}";

        return new Gson().toJson(returnJson);
    }

    /**
     * 一般失败返回的json
     *
     * @param type
     * @param message
     * @return
     */
    public static String getFailResultObject(String type, String message) {
        ReturnJson returnJson = new ReturnJson();
        returnJson.result = Params.ResultEnum.FAIL.getValue();
        returnJson.type = type;
        returnJson.message = (message == null ? "没有错误提示" : message);
        returnJson.data = "{}";

        return new Gson().toJson(returnJson);
    }

    /**
     * 服务器异常返回的json
     *
     * @return
     */
    public static String getServerExceptionResultObject() {
        ReturnJson returnJson = new ReturnJson();
        returnJson.result = Params.ResultEnum.FAIL.getValue();
        returnJson.type = Params.ReasonEnum.SERVEREXCEPTION.getValue();
        returnJson.message = "服务器异常";
        returnJson.data = "{}";

        return new Gson().toJson(returnJson);
    }
}
