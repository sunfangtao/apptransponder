package com.sft.model;

public class VersionModel {

    public static boolean isUpdate(String var1) {
        String[] var3 = "1.0.0".replace(".", "@").split("@");
        String[] var4 = var1.replace(".", "@").split("@");
        int var5 = var3.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            if (Integer.valueOf(var4[var6]).intValue() > Integer.valueOf(var3[var6]).intValue()) {
                return true;
            } else if (Integer.valueOf(var4[var6]).intValue() < Integer.valueOf(var3[var6]).intValue()) {
                return false;
            }
        }

        if (var4.length > var3.length) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println(isUpdate("1.1.0"));
    }

    private int id;
    private String versionUrl;
    private String versionCode;
    private String title;
    private String content;
    private String isForce;
    private String serverName;
    private String serverId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsForce() {
        return isForce;
    }

    public void setIsForce(String isForce) {
        this.isForce = isForce;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
