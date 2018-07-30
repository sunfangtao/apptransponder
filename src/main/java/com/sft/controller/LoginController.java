package com.sft.controller;

import com.aioute.util.SendAppJSONUtil;
import com.sft.shiro.UserNamePasswordToken;
import com.sft.util.Params;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("loginController")
public class LoginController {

    private Logger logger = Logger.getLogger(LoginController.class);

    /**
     * 用户登录
     *
     * @param req
     * @param res
     */
    @RequestMapping("login")
    public void login(HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("login");
            String resultJson = null;
            String errorClassName = (String) req.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
            if (errorClassName == null) {
                // 成功
                Subject subject = SecurityUtils.getSubject();
                String phone = (String) subject.getPrincipal();

                if (phone == null || phone.length() == 0) {
                    // 非法请求
                    logger.info("用户请求未认证");
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NOTLOGIN.getValue(), "请先登录！");
                } else {
                    resultJson = SendAppJSONUtil.getNormalString(null);
                    logger.info("用户开始登录 ：" + resultJson);
                }
            } else {
                // 失败
                logger.info("用户登录失败 " + errorClassName);
                if (errorClassName.contains("IncorrectCredentialsException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PASSWORDERROR.getValue(), "密码错误！");
                } else if (errorClassName.contains("UnknownAccountException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NOACCOUNT.getValue(), "用户不存在！");
                } else {
                    resultJson = SendAppJSONUtil.getFailResultObject("", "登录失败！");
                }
            }
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("test")
    public void testLogin(HttpServletRequest req, HttpServletResponse res) {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String serverId = req.getParameter("serverId");
            // 模拟登录
            UserNamePasswordToken token = new UserNamePasswordToken(username, password, false, serverId);
            try {
                SecurityUtils.getSubject().login(token);
                logger.info("用户开始登录(测试接口) ：username=" + username + " password=" + password);
                res.getWriter().write("用户测试登录成功 username=" + username);
            } catch (Exception e) {
                e.printStackTrace();
                res.getWriter().write("用户测试登录失败 username=" + username);
                logger.info("用户开始登录(测试接口) ：username=" + username + " password=" + password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
