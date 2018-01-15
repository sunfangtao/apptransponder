package com.sft.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.shiro.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpClient {

    private Map<String, String> parameter = new HashMap<String, String>();
    private HttpServletResponse response;
    private boolean isDirect = false;

    public HttpClient(HttpServletRequest req, HttpServletResponse res, boolean isDirect) {
        this.response = res;
        this.isDirect = isDirect;
        setParameterMap(req.getParameterMap());
    }

    public void setParameter(String key, String value) {
        this.parameter.put(key, value);
    }

    private void setParameterMap(Map<String, String[]> map) {
        if (map != null) {
            for (String key : map.keySet()) {
                this.parameter.put(key, map.get(key)[0]);
            }
        }
    }

    public String sendByGet(String url, String userId) {
        if (StringUtils.hasText(userId)) {
            this.parameter.put("userId", userId);
        }
        try {
            if (isDirect) {
                return sendByGetDirect(url);
            } else {
                return sendByGetNewConnection(url);
            }
        } catch (Exception e) {

        }
        return null;
    }

    private String sendByGetDirect(String url) throws IOException {
        if (this.parameter.size() > 0) {
            StringBuffer sb = new StringBuffer();
            Iterator<String> it = this.parameter.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                sb.append(key).append("=").append(URLEncoder.encode(this.parameter.get(key), "UTF-8"));
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            response.sendRedirect(url + "?" + sb.toString());
        } else {
            response.sendRedirect(url);
        }
        return null;
    }

    private String sendByGetNewConnection(String url) throws IOException {
        StringBuffer sb = new StringBuffer();
        if (this.parameter.size() > 0) {
            sb.append("?");
            Iterator<String> it = this.parameter.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                sb.append(key).append("=").append(URLEncoder.encode(this.parameter.get(key), "UTF-8"));
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        } else {
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url + sb.toString());
        httpGet.addHeader("Content-Type", "text/html;charset=UTF-8");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        httpClient.close();
        return response.toString();
    }
}