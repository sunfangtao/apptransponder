package com.sft.dao.impl;

import com.sft.dao.VersionDao;
import com.sft.db.PrivateSqlConnectionFactory;
import com.sft.model.VersionModel;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class VersionDaoImpl implements VersionDao {

    @Resource
    PrivateSqlConnectionFactory privateSqlConnectionFactory;

    public boolean addVersion(VersionModel versionModel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "insert into version_info (version_url,version_code,title,content,is_force,server_id) values(?,?,?,?,?,?)";

        try {
            con = privateSqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, versionModel.getVersionUrl());
            ps.setString(2, versionModel.getVersionCode());
            ps.setString(3, versionModel.getTitle());
            ps.setString(4, versionModel.getContent());
            ps.setString(5, versionModel.getIsForce());
            ps.setString(6, versionModel.getServerId());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privateSqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return false;
    }

    public boolean updateVersion(VersionModel versionModel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("update version_info set id = ?");
        if (StringUtils.hasText(versionModel.getVersionUrl())) {
            sb.append(",version_url = '").append(versionModel.getVersionUrl()).append("'");
        }
        if (StringUtils.hasText(versionModel.getVersionCode())) {
            sb.append(",version_code = '").append(versionModel.getVersionCode()).append("'");
        }
        if (StringUtils.hasText(versionModel.getTitle())) {
            sb.append(",title = '").append(versionModel.getTitle()).append("'");
        }
        if (StringUtils.hasText(versionModel.getContent())) {
            sb.append(",content = '").append(versionModel.getContent()).append("'");
        }
        if (StringUtils.hasText(versionModel.getIsForce())) {
            sb.append(",is_force = '").append(versionModel.getIsForce()).append("'");
        }
        if (StringUtils.hasText(versionModel.getServerId())) {
            sb.append(",server_id = '").append(versionModel.getServerId()).append("'");
        }
        sb.append(" where id = ?");

        try {
            con = privateSqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setInt(1, versionModel.getId());
            ps.setInt(2, versionModel.getId());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privateSqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return false;
    }

    public int getCount(Map<String, String> whereMap) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("select count(1) as count from version_info");
        if (whereMap != null && whereMap.size() > 0) {
            sb.append(" where");
            for (String key : whereMap.keySet()) {
                sb.append(" ").append(key).append(" = '").append(whereMap.get(key)).append("'");
            }
        }

        try {
            con = privateSqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privateSqlConnectionFactory.closeConnetion(con, ps, rs);
        }

        return 0;
    }

    public List<VersionModel> getVersion(Map<String, String> whereMap, int page, int pageSize) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("select t1.*,t2.name as server_name from version_info t1,server t2");
        sb.append(" where t1.server_id = t2.id");
        if (whereMap != null) {
            for (String key : whereMap.keySet()) {
                sb.append(" and ").append(key).append(" = '").append(whereMap.get(key)).append("'");
            }
        }
        sb.append(" limit ");
        sb.append((page - 1) * pageSize).append(",").append(pageSize);

        List<VersionModel> sqlList = new ArrayList<VersionModel>();
        try {
            con = privateSqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                VersionModel versionModel = new VersionModel();
                versionModel.setId(rs.getInt("id"));
                versionModel.setVersionUrl(rs.getString("version_url"));
                versionModel.setVersionCode(rs.getString("version_code"));
                versionModel.setServerName(rs.getString("server_name"));
                versionModel.setTitle(rs.getString("title"));
                versionModel.setContent(rs.getString("content"));
                versionModel.setIsForce(rs.getString("is_force"));
                versionModel.setServerId(rs.getString("server_id"));

                sqlList.add(versionModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privateSqlConnectionFactory.closeConnetion(con, ps, rs);
        }

        return sqlList;
    }

    public VersionModel getVersionByServerId(String serverId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("select v.*,s.name as name from version_info v,server s where v.server_id = ?");
        sb.append(" and s.id = v.server_id");

        try {
            con = privateSqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, StringUtils.hasText(serverId) ? serverId : "");
            rs = ps.executeQuery();
            while (rs.next()) {
                VersionModel versionModel = new VersionModel();
                versionModel.setVersionUrl(rs.getString("version_url"));
                versionModel.setVersionCode(rs.getString("version_code"));
                versionModel.setTitle(rs.getString("title"));
                versionModel.setContent(rs.getString("content"));
                versionModel.setIsForce(rs.getString("is_force"));
                versionModel.setServerId(serverId);
                versionModel.setServerName(rs.getString("name"));

                return versionModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privateSqlConnectionFactory.closeConnetion(con, ps, rs);
        }

        return null;
    }
}
