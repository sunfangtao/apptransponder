package com.sft.util;

import java.util.HashMap;

public class GeoHash {

    public static final double MIN_LAT = -90;
    public static final double MAX_LAT = 90;
    public static final double MIN_LNG = -180;
    public static final double MAX_LNG = 180;

    // 32位编码对应字符
    public static final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static final float[] latError = {45f, 22.5f, 11.25f, 5.625f, 2.813f, 1.406f,
            0.703f, 0.352f, 0.176f, 0.088f, 0.044f, 0.022f};
    public static final float[] lngError = {90f, 45f, 22.5f, 11.25f, 5.625f,
            2.813f, 1.406f, 0.703f, 0.352f, 0.176f, 0.088f};

    // 编码长度
    private int geoHashLength = 20;

    private static HashMap<Character, Integer> CHARS_MAP;

    static {
        CHARS_MAP = new HashMap<Character, Integer>();
        for (int i = 0; i < digits.length; i++) {
            CHARS_MAP.put(digits[i], i);
        }
    }

    public void setGeoHashLength(int length) {
        geoHashLength = length;
    }

    /**
     * 获取指定经纬度的GeoHash编码
     *
     * @param lng 经度
     * @param lat 纬度
     * @return GeoHash编码
     */
    public String getGeoHashStr(double lng, double lat) {
        return getGeoHashByBinary(getLatLngGeoStr(getLatLngGeoBinaryStr(lng, MAX_LNG, MIN_LNG), getLatLngGeoBinaryStr(lat, MAX_LAT, MIN_LAT)));
    }

    /**
     * 获取指定经度或纬度二进制字符串
     *
     * @param latLng 经度或纬度的值
     * @param max    经度或纬度的最大值
     * @param min    经度或纬度的最小值
     * @return 二进制字符串
     */
    private String getLatLngGeoBinaryStr(double latLng, double max, double min) {
        double minValue = min;
        double maxValue = max;

        StringBuffer latLngGeoString = new StringBuffer();
        for (int i = 0; i < geoHashLength; i++) {
            double middleValue = (maxValue + minValue) / 2;
            if (latLng < middleValue) {
                latLngGeoString.append("0");
                maxValue = middleValue;
            } else {
                latLngGeoString.append("1");
                minValue = middleValue;
            }
        }
        System.out.println("ln1=" + latLngGeoString);
        return latLngGeoString.toString();
    }

    /**
     * 将经度和纬度的二进制字符串进行拆分组合
     *
     * @param lngGeoBinaryStr 经度的二进制字符串
     * @param latGeoBinaryStr 纬度的二进制字符串
     * @return 拆分组合的二进制字符
     */
    private String getLatLngGeoStr(String lngGeoBinaryStr, String latGeoBinaryStr) {
        StringBuffer binStr = new StringBuffer();
        // 得到二进制字符串
        for (int i = 0; i < geoHashLength; i++) {
            binStr.append(lngGeoBinaryStr.charAt(i));
            binStr.append(latGeoBinaryStr.charAt(i));
        }
        return binStr.toString();
    }

    /**
     * 获取二进制字符串的GeoHash编码
     *
     * @param binaryStr 指定经纬度转化的二进制字符串
     * @return GeoHash编码
     */
    private String getGeoHashByBinary(String binaryStr) {
        int length = (int) Math.ceil(2 * geoHashLength / 5f);

        StringBuffer result = new StringBuffer();
        for (int i = 0; i < length; i++) {
            if (i < length - 1) {
                result.append(digits[Integer.parseInt(binaryStr.substring(5 * i, 5 * i + 5), 2)]);
            } else {
                result.append(digits[Integer.parseInt(binaryStr.substring(5 * i), 2)]);
            }
        }

        return result.toString();
    }

    /**
     * 求与当前GeoHash编码值相邻的8个格子的GeoHash编码值。
     *
     * @param geoHash
     * @return string数组，周围格子的GeoHash编码值
     */
    public String[] expand(String geoHash) {
        String bCoordinate = getGeoHashBinaryString(geoHash);//当前geoHash值对应的二进制串

        String[] lngLat = getLngLatBinaryStr(bCoordinate);

        System.out.println("lng=" + lngLat[0] + " lat=" + lngLat[1]);
        String downLat = calculate(lngLat[1], -1);
        String upLat = calculate(lngLat[1], 1);
        String leftLon = calculate(lngLat[0], -1);
        String rightLon = calculate(lngLat[0], 1);

        return new String[]{getGeoHashByBinary(getLatLngGeoStr(leftLon, upLat)), getGeoHashByBinary(getLatLngGeoStr(lngLat[0], upLat)), getGeoHashByBinary(getLatLngGeoStr(rightLon, upLat)),
                getGeoHashByBinary(getLatLngGeoStr(leftLon, lngLat[1])), getGeoHashByBinary(getLatLngGeoStr(rightLon, lngLat[1])),
                getGeoHashByBinary(getLatLngGeoStr(leftLon, downLat)), getGeoHashByBinary(getLatLngGeoStr(lngLat[0], downLat)), getGeoHashByBinary(getLatLngGeoStr(rightLon, downLat))};
    }

    /**
     * 获取GeoHash编码的二进制字符串
     *
     * @param geoHash GeoHash编码
     * @return
     */
    private String getGeoHashBinaryString(String geoHash) {
        if (geoHash == null || "".equals(geoHash)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        int length = geoHash.length();
        for (int i = 0; i < geoHash.length(); i++) {
            char c = geoHash.charAt(i);
            if (CHARS_MAP.containsKey(c)) {
                String cStr = getBase32BinaryString(CHARS_MAP.get(c));
                if (cStr != null) {
                    if (i == length - 1) {
                        int realLength = 2 * geoHashLength - i * 5;
                        if (cStr.length() >= realLength) {
                            sb.append(cStr.substring(cStr.length() - realLength));
                        }
                    } else {
                        sb.append(cStr);
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取GeoHash编码的经纬度二进制字符串
     *
     * @param geoHashBinaryStr GeoHash
     * @return
     */
    private String[] getLngLatBinaryStr(String geoHashBinaryStr) {
        StringBuilder bLat = new StringBuilder();
        StringBuilder bLng = new StringBuilder();

        for (int i = 0; i < geoHashBinaryStr.length(); i++) {
            if ((i % 2) == 0) {
                bLng.append(geoHashBinaryStr.charAt(i));
            } else {
                bLat.append(geoHashBinaryStr.charAt(i));
            }
        }

        return new String[]{bLng.toString(), bLat.toString()};
    }

    private String getBase32BinaryString(int i) {
        if (i < 0 || i > 31) {
            return null;
        }
        String str = Integer.toBinaryString(i + 32);
        return str.substring(1);
    }

    /**
     * 计算当前格子左右（上下）格子的经（纬）度值二进制串
     *
     * @param coordinate 当前格子的经/纬度值
     * @param i          偏移量
     * @return
     */
    private String calculate(String coordinate, int i) {
        int length = coordinate.length();
        String result = Integer.toBinaryString((Integer.valueOf(coordinate, 2) + i) + (1 << length)).substring(1);
        if (result.length() != length) {
            return null;
        } else {
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        String geo = new GeoHash().getGeoHashStr(170, 2);
        System.out.println(geo);
        String[] arr = new GeoHash().expand(geo);

        for (int i = 0; i < arr.length; i++) {
            System.out.println("i=" + i + " arr=" + arr[i]);
        }
        System.out.println(System.currentTimeMillis());
    }
}
