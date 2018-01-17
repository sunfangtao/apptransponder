package com.sft.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SecurityUtil {

    private static int orderFix = 0;

    private static String getOrderFix() {
        int value = (orderFix++) % 1000;
        int length = (value + "").length();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4 - length; i++) {
            sb.append("0");
        }
        sb.append(value);
        return sb.toString();
    }

    public static String getLocalIp() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress(); //get the ip address
            return ip;
        } catch (UnknownHostException e) {
            System.out.println("unknown host!");
        }
        return null;

    }

    public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    public static String getUserId(HttpServletRequest req) {
        try {
            if (SecurityUtils.getSubject() != null) {
                return (String) SecurityUtils.getSubject().getSession().getAttribute(Params.USER_ID);
            } else {
                return req.getParameter(Params.USER_ID);
            }
        } catch (Exception e) {
            return req.getParameter(Params.USER_ID);
        }
    }

    public static String getServerId(HttpServletRequest req) {
        try {
            if (SecurityUtils.getSubject() != null) {
                return (String) SecurityUtils.getSubject().getSession().getAttribute(Params.SERVER_ID);
            } else {
                return req.getParameter(Params.SERVER_ID);
            }
        } catch (Exception e) {
            return req.getParameter(Params.SERVER_ID);
        }
    }

}
