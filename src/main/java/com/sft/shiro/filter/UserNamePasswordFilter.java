package com.sft.shiro.filter;

import com.sft.shiro.UserNamePasswordToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class UserNamePasswordFilter extends FormAuthenticationFilter {
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        return new UserNamePasswordToken(username, password, false, request.getParameter("serverId"));
    }
}
