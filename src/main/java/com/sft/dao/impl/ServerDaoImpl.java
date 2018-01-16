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

@Repository
public class ServerDaoImpl implements ServerDao {

    @Resource
    PrivateSqlConnectionFactory sqlConnectionFactory;

    public boolean addServer(ServerModel serverModel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "insert into server (id,ip,port,username,password,dbname,address,name,create_time) values(?,?,?,?,?,?,?,?,?)";

        try {
            con = sqlConnectionFactory.getConnection();
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
            sqlConnectionFactory.closeConnetion(con, ps, rs);
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
            sb.append("id = ").append(serverModel.getId());
        }
        if (StringUtils.hasText(serverModel.getIp())) {
            sb.append(",ip = ").append(serverModel.getIp());
        }
        if (StringUtils.hasText(serverModel.getUserName())) {
            sb.append(",username = ").append(serverModel.getUserName());
        }
        if (StringUtils.hasText(serverModel.getPassword())) {
            sb.append(",password = ").append(serverModel.getPassword());
        }
        if (StringUtils.hasText(serverModel.getDbName())) {
            sb.append(",dbname = ").append(serverModel.getDbName());
        }
        if (StringUtils.hasText(serverModel.getAddress())) {
            sb.append(",address = ").append(serverModel.getAddress());
        }
        if (StringUtils.hasText(serverModel.getName())) {
            sb.append(",name = ").append(serverModel.getName());
        }
        sb.append("where id = ?");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, serverModel.getId());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return false;
    }
}
