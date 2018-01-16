package com.sft.dao.impl;

import com.sft.dao.UserDao;
import com.sft.db.SqlConnectionFactory;
import com.sft.db.TypeSql;
import com.sft.model.AppUserModel;
import com.sft.util.Params;
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

    public AppUserModel getUserInfo(String serverId, String phone) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = typeSql.getSqlByType(serverId, Params.SqlType.USER_INFO.getValue());

        try {
            con = sqlConnectionFactory.getConnectByServerType(serverId);
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

}
