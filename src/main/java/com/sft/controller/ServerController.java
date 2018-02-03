package com.sft.controller;

import com.aioute.util.DateUtil;
import com.aioute.util.SendAppJSONUtil;
import com.aioute.util.SendPlatJSONUtil;
import com.sft.dao.ServerDao;
import com.sft.db.SqlConnectionFactory;
import com.sft.db.TypeSql;
import com.sft.model.ServerModel;
import com.sft.service.PermissionService;
import com.sft.util.*;
import org.apache.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("server")
public class ServerController {

    private Logger logger = Logger.getLogger(ServerController.class);

    @Resource
    SqlConnectionFactory sqlConnectionFactory;
    @Resource
    TypeSql typeSql;
    @Resource
    PermissionService permissionService;
    @Resource
    private ServerDao serverDao;
    @Resource
    private EhCacheManager shiroCacheManager;

    /**
     * 添加服务
     *
     * @param req
     * @param res
     */
    @RequestMapping("add")
    public void addServer(ServerModel serverModel, HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("addServer");
            String resultJson = null;

            serverModel.setId(UUID.randomUUID().toString().replace("-", ""));
            serverModel.setCreateTime(DateUtil.getCurDate());

            if (serverDao.addServer(serverModel)) {
                // 添加成功
                resultJson = SendAppJSONUtil.getNormalString(null);
            } else {
                // 添加失败
                resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.SQLEXCEPTION.getValue(), "操作失败");
            }
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加服务
     *
     * @param req
     * @param res
     */
    @RequestMapping("update")
    public void updateServer(ServerModel serverModel, HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("updateServer");
            String resultJson = null;

            if (serverDao.updateServer(serverModel)) {
                resultJson = SendAppJSONUtil.getNormalString(null);
                // 更新成功
                sqlConnectionFactory.clearSourceMap(serverModel.getId());
                typeSql.clearSqlMap();
            } else {
                // 更新失败
                resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.SQLEXCEPTION.getValue(), "操作失败");
            }
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param req
     * @param res
     */
    @ResponseBody
    @RequestMapping("get")
    public String getServer(HttpServletRequest req, HttpServletResponse res) {
        int page = PagingUtil.getPage(req);
        int pageSize = PagingUtil.getPageSize(req);

        List<ServerModel> serverList = serverDao.getSever(null, page, pageSize);
        int count = serverDao.getCount(null);
        return SendPlatJSONUtil.getPageJsonString(0, "", count, serverList);
    }

    /**
     * 更新权限信息（清缓存）
     *
     * @param req
     * @param res
     */
    @RequestMapping("updatePermissionCache")
    public void permissionUpdate(HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("permissionUpdate");
            permissionService.update();
            res.getWriter().write(SendAppJSONUtil.getNormalString(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * shiro认证授权缓存
     *
     * @param req
     * @param res
     */
    @RequestMapping("updateAuthorizationCache")
    public void shiroCache(HttpServletRequest req, HttpServletResponse res) {
        try {
            String account = req.getParameter("account");
            clearAuthorizationInfo(account);
            String returnJson = SendAppJSONUtil.getNormalString("授权信息刷新成功!");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除用户的授权信息
     */
    private void clearAuthorizationInfo(String userName) {
        if (userName != null) {
            Cache<Object, Object> cache = shiroCacheManager.getCache("authorizationCache");
            cache.remove(userName);
        } else {
            Cache<Object, Object> cache = shiroCacheManager.getCache("authorizationCache");
            cache.clear();
        }
    }
}
