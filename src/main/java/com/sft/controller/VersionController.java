package com.sft.controller;

import com.aioute.util.SendAppJSONUtil;
import com.aioute.util.SendPlatJSONUtil;
import com.sft.dao.VersionDao;
import com.sft.model.VersionModel;
import com.sft.util.PagingUtil;
import com.sft.util.Params;
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
@RequestMapping("version")
public class VersionController {

    private Logger logger = Logger.getLogger(VersionController.class);

    @Resource
    private VersionDao versionDao;

    /**
     * 添加版本
     *
     * @param req
     * @param res
     */
    @ResponseBody
    @RequestMapping("add")
    public void addVersion(VersionModel versionModel, HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("addVersion");
            String resultJson = null;

            if (versionDao.addVersion(versionModel)) {
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
    @ResponseBody
    @RequestMapping("update")
    public void updateVersion(VersionModel versionModel, HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("updateVersion");
            String resultJson = null;

            if (versionDao.updateVersion(versionModel)) {
                // 更新成功
                resultJson = SendAppJSONUtil.getNormalString(null);
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
     * 获取系统所有版本
     *
     * @param req
     * @param res
     */
    @ResponseBody
    @RequestMapping("get")
    public String getVersion(HttpServletRequest req, HttpServletResponse res) {
        int page = PagingUtil.getPage(req);
        int pageSize = PagingUtil.getPageSize(req);

        Map<String, String> whereMap = new HashMap<String, String>();
//            whereMap.put("name", req.getParameter("name"));
//            whereMap.put("del_flag", req.getParameter("del_flag"));

        List<VersionModel> versionList = versionDao.getVersion(whereMap, page, pageSize);
        int count = versionDao.getCount(whereMap);
        return SendPlatJSONUtil.getPageJsonString(0, "", count, versionList);
    }


    /**
     * 获取系统所有模块
     *
     * @param req
     * @param res
     */
    @ResponseBody
    @RequestMapping("getNewVersion")
    public String getVersionByServerId(HttpServletRequest req, HttpServletResponse res) {

        VersionModel versionModel = versionDao.getVersionByServerId(req.getParameter("serverId"));
        if (versionModel == null) {
            return SendAppJSONUtil.getNullResultObject();
        }
        return SendAppJSONUtil.getNormalString(versionModel);
    }
}
