package com.sft.controller;

import com.sft.model.AppUserModel;
import com.sft.service.AppUserService;
import com.sft.shiro.UserNamePasswordToken;
import com.sft.util.Params;
import com.sft.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("loginController")
public class LoginController {

    private Logger logger = Logger.getLogger(LoginController.class);

    @Resource
    private AppUserService userService;

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
                    String serverId = req.getParameter("serverId");
                    AppUserModel user = userService.getUserInfoByPhone(serverId, phone);
                    resultJson = "";
                    logger.info("用户开始登录 ：" + resultJson);
                }
            } else {
                // 失败
                logger.info("用户登录失败 " + errorClassName);
                if (errorClassName.contains("IncorrectCredentialsException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PASSWORDERROR.getValue(), "验证码错误！");
                } else if (errorClassName.contains("UnknownAccountException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NOACCOUNT.getValue(), "用户不存在！");
                } else if (errorClassName.contains("NoCodeException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PERMISSION.getValue(), "请获取验证码！");
                } else if (errorClassName.contains("CodeErrorException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PERMISSION.getValue(), "验证码错误！");
                } else if (errorClassName.contains("CodeInvalicException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PERMISSION.getValue(), "验证码失效！");
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
            String phone = req.getParameter("phone");
            String code = req.getParameter("code");
            String serverId = req.getParameter("serverId");
            // 模拟登录
            UserNamePasswordToken token = new UserNamePasswordToken(phone, code, false, serverId);
            try {
                SecurityUtils.getSubject().login(token);
                logger.info("用户开始登录(测试接口) ：phone=" + phone + " code=" + code);
                res.getWriter().write("用户测试登录成功 phone=" + phone);
            } catch (Exception e) {
                e.printStackTrace();
                res.getWriter().write("用户测试登录失败 phone=" + phone);
                logger.info("用户开始登录(测试接口) ：phone=" + phone + " code=" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
