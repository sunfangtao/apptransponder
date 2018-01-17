package com.sft.controller;

import com.sft.dao.SqlDao;
import com.sft.db.TypeSql;
import com.sft.model.SqlModel;
import com.sft.util.PagingUtil;
import com.sft.util.Params;
import com.sft.util.SendAppJSONUtil;
import com.sft.util.SendPlatJSONUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("sql")
public class SqlController {

    private Logger logger = Logger.getLogger(SqlController.class);

    @Resource
    TypeSql typeSql;
    @Resource
    private SqlDao sqlDao;

    /**
     * 添加服务
     *
     * @param req
     * @param res
     */
    @ResponseBody
    @RequestMapping("add")
    public void addSql(SqlModel sqlModel, HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("addSql");
            String resultJson = null;

            if (sqlDao.addSql(sqlModel)) {
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

    @ResponseBody
    @RequestMapping("getType")
    public void getType(HttpServletRequest req, HttpServletResponse res) {
        try {
            res.getWriter().write(SendAppJSONUtil.getNormalString(sqlDao.getType()));
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
    @ResponseBody
    @RequestMapping("update")
    public void updateSql(SqlModel sqlModel, HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("updateSql");
            String resultJson = null;

            if (sqlDao.updateSql(sqlModel)) {
                // 更新成功
                resultJson = SendAppJSONUtil.getNormalString(null);
                // 刷新缓存
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
     * 获取系统所有模块
     *
     * @param req
     * @param res
     */
    @ResponseBody
    @RequestMapping("get")
    public String getSql(HttpServletRequest req, HttpServletResponse res) {
        int page = PagingUtil.getPage(req);
        int pageSize = PagingUtil.getPageSize(req);

        Map<String, String> whereMap = new HashMap<String, String>();
//            whereMap.put("name", req.getParameter("name"));
//            whereMap.put("del_flag", req.getParameter("del_flag"));

        List<SqlModel> sqlList = sqlDao.getSql(whereMap, page, pageSize);
        int count = sqlDao.getCount(whereMap);
        return SendPlatJSONUtil.getPageJsonString(0, "", count, sqlList);
    }

}
