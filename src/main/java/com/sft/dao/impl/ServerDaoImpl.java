package com.sft.dao.impl;

import com.sft.dao.ServerDao;
import com.sft.db.PrivateSqlConnectionFactory;
import com.sft.model.ServerModel;
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
public class ServerDaoImpl implements ServerDao {

    @Resource
    PrivateSqlConnectionFactory privateSqlConnectionFactory;

    public boolean addServer(ServerModel serverModel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "insert into server (id,ip,port,username,password,dbname,address,name,create_time) values(?,?,?,?,?,?,?,?,?)";

        try {
            con = privateSqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, serverModel.getId());
            ps.setString(2, serverModel.getIp());
            ps.setInt(3, serverModel.getPort());
            ps.setString(4, serverModel.getUserName());
            ps.setString(5, serverModel.getPassword());
            ps.setString(6, serverModel.getDbName());
            ps.setString(7, serverModel.getAddress());
            ps.setString(8, serverModel.getName());
            ps.setString(9, serverModel.getCreateTime());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privateSqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return false;
    }

    public boolean updateServer(ServerModel serverModel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("update server set ");
        if (StringUtils.hasText(serverModel.getId())) {
            sb.append("id = '").append(serverModel.getId()).append("'");
        }
        if (StringUtils.hasText(serverModel.getIp())) {
            sb.append(",ip = '").append(serverModel.getIp()).append("'");
        }
        if (StringUtils.hasText(serverModel.getUserName())) {
            sb.append(",username = '").append(serverModel.getUserName()).append("'");
        }
        if (StringUtils.hasText(serverModel.getPassword())) {
            sb.append(",password = '").append(serverModel.getPassword()).append("'");
        }
        if (StringUtils.hasText(serverModel.getDbName())) {
            sb.append(",dbname = '").append(serverModel.getDbName()).append("'");
        }
        if (StringUtils.hasText(serverModel.getAddress())) {
            sb.append(",address = '").append(serverModel.getAddress()).append("'");
        }
        if (StringUtils.hasText(serverModel.getName())) {
            sb.append(",name = '").append(serverModel.getName()).append("'");
        }
        if (serverModel.getPort()>0) {
            sb.append(",port = ").append(serverModel.getPort());
        }
        sb.append(" where id = ?");

        try {
            con = privateSqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, serverModel.getId());
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
        sb.append("select count(1) as count from server");
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

    public List<ServerModel> getSever(Map<String, String> whereMap, int page, int pageSize) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("select * from server");
        if (whereMap != null && whereMap.size() > 0) {
            sb.append(" where");
            for (String key : whereMap.keySet()) {
                sb.append(" ").append(key).append(" = '").append(whereMap.get(key)).append("'");
            }
        }
        sb.append(" limit ");
        sb.append((page - 1) * pageSize).append(",").append(pageSize);

        List<ServerModel> serverList = new ArrayList<ServerModel>();
        try {
            con = privateSqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                ServerModel serverModel = new ServerModel();
                serverModel.setCreateTime(rs.getString("create_time"));
                serverModel.setId(rs.getString("id"));
                serverModel.setName(rs.getString("name"));
                serverModel.setAddress(rs.getString("address"));
                serverModel.setDbName(rs.getString("dbname"));
                serverModel.setIp(rs.getString("ip"));
                serverModel.setPassword(rs.getString("password"));
                serverModel.setPort(rs.getInt("port"));
                serverModel.setUserName(rs.getString("username"));

                serverList.add(serverModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privateSqlConnectionFactory.closeConnetion(con, ps, rs);
        }

        return serverList;
    }
}
