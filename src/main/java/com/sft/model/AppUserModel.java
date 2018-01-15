package com.sft.model;

/**
 * 用户模块
 */
public class AppUserModel {

    private String id;
    private String password;  // 密码
    private String login_id;  // 第三方登录id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }
}
