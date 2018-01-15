package com.sft.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UserNamePasswordToken extends UsernamePasswordToken {

    private boolean isThirdLogin = false;
    private String serverId;

    public UserNamePasswordToken(String username, String password, boolean isThirdLogin, String serverId) {
        super(username, password);
        this.isThirdLogin = isThirdLogin;
        this.serverId = serverId;
    }

    public boolean isThirdLogin() {
        return isThirdLogin;
    }

    public String getServerId() {
        return serverId;
    }
}
