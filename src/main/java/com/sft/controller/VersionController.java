package com.sft.controller;

import com.aioute.util.SendAppJSONUtil;
import com.aioute.util.SendPlatJSONUtil;
import com.sft.dao.VersionDao;
import com.sft.model.VersionModel;
import com.sft.util.CRCode;
import com.sft.util.PagingUtil;
import com.sft.util.Params;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
     * 下载最新版的APP
     *
     * @param req
     * @param res
     */
    @ResponseBody
    @RequestMapping("getNewVersionByServerId")
    public void getNewVersionByServerId(HttpServletRequest req, HttpServletResponse res) {
        try {
            VersionModel versionModel = versionDao.getVersionByServerId(req.getParameter("serverId"));
            if (versionModel == null) {
                res.sendError(404, "File not found!");
                return;
            }
            res.sendRedirect(versionModel.getVersionUrl());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 下载最新版的APP的二维码
     *
     * @param req
     * @param res
     */
    @ResponseBody
    @RequestMapping("getDownload")
    public void getDownload(HttpServletRequest req, HttpServletResponse res) {
        try {
            VersionModel versionModel = versionDao.getVersionByServerId(req.getParameter("serverId"));
            if (versionModel == null) {
                res.sendError(404, "File not found!");
                return;
            }
            String crCodeUrl = "http://221.0.91.34:3080/apptransponder/version/getNewVersionByServerId?serverId=" + versionModel.getServerId();

            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                String fileName = CRCode.getQRCode(crCodeUrl, versionModel.getServerName(), req);
                logger.info("fileName=" + fileName);
                String downLoadUrl = "";
                if (fileName.contains("webapps")) {
                    downLoadUrl = "http://221.0.91.34:3080" + fileName.substring(fileName.lastIndexOf("webapps") + "webapps".length());
                } else {
                    downLoadUrl = "http://221.0.91.34:29249/" + fileName.substring(fileName.lastIndexOf("apptransponder") + "apptransponder".length() + 1);
                }
                logger.info("downLoadUrl=" + downLoadUrl);

                res.getWriter().write(downLoadUrl);
//                String resultFileName = URLEncoder.encode("123", "UTF-8");
                //设置响应
//                res.setContentType("application/force-download");
//                res.setCharacterEncoding("UTF-8");
//                res.setHeader("Content-disposition", "attachment; filename=" + resultFileName);// 设定输出文件头

//                inputStream = new FileInputStream(new File(fileName));
//                outputStream = res.getOutputStream();
//
//                IOUtils.copy(inputStream, outputStream);
//                res.flushBuffer();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

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
