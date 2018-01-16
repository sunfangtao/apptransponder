package com.sft.controller;

import com.sft.dao.ServerDao;
import com.sft.model.ServerModel;
import com.sft.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@RequestMapping("server")
public class ServerController {

    private Logger logger = Logger.getLogger(ServerController.class);

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
            } else {
                // 添加失败
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
                // 更新成功
            } else {
                // 更新失败
            }
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
