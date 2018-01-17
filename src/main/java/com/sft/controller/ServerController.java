package com.sft.controller;

import com.sft.dao.ServerDao;
import com.sft.db.SqlConnectionFactory;
import com.sft.db.TypeSql;
import com.sft.model.ServerModel;
import com.sft.util.*;
import org.apache.log4j.Logger;
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
    private ServerDao serverDao;

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
}
