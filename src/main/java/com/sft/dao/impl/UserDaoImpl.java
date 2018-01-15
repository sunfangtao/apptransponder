package com.sft.dao.impl;

import com.sft.dao.UserDao;
import com.sft.db.SqlConnectionFactory;
import com.sft.db.TypeSql;
import com.sft.model.AppUserModel;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class UserDaoImpl implements UserDao {

    @Resource
    private TypeSql typeSql;
    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public AppUserModel getUserInfoByPhone(String serverId, String phone) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = typeSql.getSqlByType(serverId, "getUserInfoByPhone");

        try {
            con = sqlConnectionFactory.getConnectByServerType(serverId, "getUserInfoByPhone");
            ps = con.prepareStatement(sql);
            ps.setString(1, phone);
            rs = ps.executeQuery();
            while (rs.next()) {
                AppUserModel appUserModel = new AppUserModel();
                appUserModel.setId(rs.getString("id"));
                appUserModel.setPassword(rs.getString("password"));
                return appUserModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return null;
    }


    public AppUserModel getUserInfoByLoginId(String serverId, String login_id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = typeSql.getSqlByType(serverId, "getUserInfoByLoginId");

        try {
            con = sqlConnectionFactory.getConnectByServerType(serverId, "getUserInfoByLoginId");
            ps = con.prepareStatement(sql);
            ps.setString(1, login_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                AppUserModel appUserModel = new AppUserModel();
                appUserModel.setId(rs.getString("id"));
                return appUserModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return null;
    }

}
