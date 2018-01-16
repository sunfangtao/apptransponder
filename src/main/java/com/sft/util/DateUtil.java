package com.sft.util;

import org.apache.shiro.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getCurDate() {
        return format.format(new Date());
    }

    public static long getTime(String date) {
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("日期格式错误");
    }

    public static String getDeletePointDate(String date) {
        if (StringUtils.hasText(date) && date.contains(".")) {
            return date.substring(0, date.indexOf("."));
        } else {
            return date;
        }
    }

}