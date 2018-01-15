/**
 * @Project:crm
 * @Title:PasswordShiroRealm.java
 * @Author:Riozenc
 * @Datetime:2016年10月16日 下午8:04:07
 */
package com.sft.shiro.realm;

import com.sft.model.AppUserModel;
import com.sft.model.Permission;
import com.sft.service.AppUserService;
import com.sft.service.PermissionService;
import com.sft.shiro.UserNamePasswordToken;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PasswordShiroRealm extends AuthorizingRealm {

    private Logger logger = Logger.getLogger(PasswordShiroRealm.class);

    @Autowired
    private SessionManager sessionManager;
    @Resource
    private AppUserService userService;
    @Resource
    private PermissionService permissionService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String serverId = (String) SecurityUtils.getSubject().getSession().getAttribute("serverId");
        String account = (String) principalCollection.getPrimaryPrincipal();
        String userId = userService.getUserInfoByPhone(serverId, account).getId();
        SimpleAuthorizationInfo authorizationInfo = null;
        if (authorizationInfo == null) {
            authorizationInfo = new SimpleAuthorizationInfo();
            List<Permission> permissions = permissionService.getPermissions(serverId, userId);
            if (permissions != null) {
                List<String> permissionList = new ArrayList<String>();
                for (Permission permission : permissions) {
                    permissionList.add(permission.getPermission());
                }
                authorizationInfo.addStringPermissions(permissionList);
            }
            List<String> roleNameList = permissionService.getRoles(serverId, userId);
            if (roleNameList != null) {
                authorizationInfo.addRoles(roleNameList);
            }
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {

        UserNamePasswordToken token = (UserNamePasswordToken) authenticationToken;
        String username = token.getUsername();

        if (sessionManager instanceof DefaultWebSessionManager) {
            DefaultWebSessionManager defaultWebSessionManager = (DefaultWebSessionManager) sessionManager;

            SecurityUtils.getSubject().logout();
            SessionDAO sessionDAO = defaultWebSessionManager.getSessionDAO();
            Collection<Session> sessions = sessionDAO.getActiveSessions();

            for (Session session : sessions) {
                // 清除该用户以前登录时保存的session
                if (token.getPrincipal().equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
                    sessionDAO.delete(session);
                    logger.info("已在线的用户：" + token.getPrincipal() + "被踢出");
                }
            }
        }

        if (!StringUtils.hasText(username)) {
            throw new UnknownAccountException("");
        }

        // TODO 获取用户信息
        AppUserModel userModel = userService.getUserInfoByPhone(token.getServerId(), username);
        String password = userModel.getPassword();

        SecurityUtils.getSubject().getSession().setAttribute("userId", userModel.getId());
        SecurityUtils.getSubject().getSession().setAttribute("serverId", token.getServerId());
        return new SimpleAuthenticationInfo(username, password, getName());
    }

}
