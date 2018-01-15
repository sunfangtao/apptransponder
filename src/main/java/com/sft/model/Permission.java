package com.sft.model;

/**
 * 用户权限
 */
public class Permission {

    private String id;         // 编号
    private boolean is_user; // 是否需要登录
    private String url;
    private String address;       // 链接地址
    private String type;      // 链接映射标识
    private String permission;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Permission) {
            Permission permission = (Permission) obj;
            if (permission.id.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIs_user() {
        return is_user;
    }

    public void setIs_user(boolean is_user) {
        this.is_user = is_user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}