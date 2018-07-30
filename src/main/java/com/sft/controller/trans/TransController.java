package com.sft.controller.trans;

import com.aioute.util.SendAppJSONUtil;
import com.aioute.util.SingleLock;
import com.sft.model.Permission;
import com.sft.service.PermissionService;
import com.sft.util.FTPPicUtil;
import com.sft.util.HttpClient;
import com.sft.util.Params;
import com.sft.util.SecurityUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

@Controller
@RequestMapping("trans")
public class TransController {

    private static Logger logger = Logger.getLogger(TransController.class);

    @Resource
    private PermissionService permissionService;

    /**
     * app接口转发
     *
     * @param req
     * @param res
     */
    @RequestMapping("app")
    public void transPlat(HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnJson = null;
            String type = req.getParameter("type");
            String serverId = req.getParameter("serverId");
            StringBuffer sb = new StringBuffer();
            try {
                if (StringUtils.hasText(type)) {
                    Permission permission = permissionService.getUrlByType(serverId, type);
                    if (permission == null || !StringUtils.hasText(permission.getType())) {
                        returnJson = SendAppJSONUtil.getRequireParamsMissingObject("type错误!");
                    } else {
                        sb.append("ip=" + SecurityUtil.getRemoteIP(req) + " serverId=" + SecurityUtil.getServerId(req) + " user=" + SecurityUtils.getSubject().getPrincipal() + " type=" + type);
                        Map<String, String[]> paramsMap = req.getParameterMap();
                        if (paramsMap != null) {
                            sb.append("\nURL: ").append(permission.getAddress()).append("?");
                            StringBuffer sb2 = new StringBuffer();
                            for (String key : paramsMap.keySet()) {
                                sb2.append("&").append(key).append("=").append(paramsMap.get(key)[0]);
                            }
                            sb.append(sb2.substring(1));
                        }
                        logger.info(sb.toString());
                        if (permission.isIs_user()) {
                            if (SecurityUtils.getSubject().getPrincipal() == null) {
                                // 用户没有登录
                                returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NOTLOGIN.getValue(), "请先登录!");
                            } else {
                                returnJson = new HttpClient(req, res, false).sendByGet(permission.getAddress(), SecurityUtil.getUserId(req));
                            }
                        } else {
                            returnJson = new HttpClient(req, res, false).sendByGet(permission.getAddress(), null);
                        }
                    }
                } else {
                    returnJson = SendAppJSONUtil.getRequireParamsMissingObject("没有type!");
                }

                if (returnJson == null) {
                    returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.SERVEREXCEPTION.getValue(), "请稍后再试！");
                }
                logger.info(returnJson);
                res.getWriter().write(returnJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {

        } finally {

        }
    }

    /**
     * app接口转发
     *
     * @param res
     */
    @RequestMapping("pic")
    public void transPict(HttpServletRequest req, HttpServletResponse res, @RequestParam("files") MultipartFile[] files) {
        String returnJson = null;
        try {
            if (files != null && files.length > 0) {
                List<String> picUrl = null;
                if ((picUrl = FTPPicUtil.uploadPics(files)) != null) {
                    returnJson = SendAppJSONUtil.getNormalString(picUrl);
                } else {
                    returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.SQLEXCEPTION.getValue(), "上传失败!");
                }
            } else {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请添加文件!");
            }
            logger.info(returnJson);
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}