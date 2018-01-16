package com.sft.controller;

import com.sft.dao.SqlDao;
import com.sft.model.SqlModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("sql")
public class SqlController {

    private Logger logger = Logger.getLogger(SqlController.class);

    @Resource
    private SqlDao sqlDao;

    /**
     * 添加服务
     *
     * @param req
     * @param res
     */
    @RequestMapping("add")
    public void addSql(SqlModel sqlModel, HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("addSql");
            String resultJson = null;

            if (sqlDao.addSql(sqlModel)) {
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
    public void updateSql(SqlModel sqlModel, HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("updateSql");
            String resultJson = null;

            if (sqlDao.updateSql(sqlModel)) {
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
