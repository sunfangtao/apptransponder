package com.sft.util;

import org.apache.shiro.util.StringUtils;

public class Util {

    public static final String TYPE_URL = "TYPE_URL";

    public static String getConnectionKey(String serverId, String type) {
        if (StringUtils.hasText(serverId) && StringUtils.hasText(type)) {
            return serverId + "_" + type;
        }
        throw new IllegalArgumentException("serverId 或 type 错误");
    }
}
