package com.sft.dao.impl;

import com.sft.dao.PermissionDao;
import com.sft.db.SqlConnectionFactory;
import com.sft.db.TypeSql;
import com.sft.model.Permission;
import com.sft.util.Params;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PermissionDaoImpl implements PermissionDao {

    @Resource
    TypeSql typeSql;
    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public Permission getUrlByType(String serverId, String type) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Permission permission = new Permission();

        String sql = typeSql.getSqlByType(serverId, Params.SqlType.TYPE_URL.getValue());

        try {
            con = sqlConnectionFactory.getConnectByServerType(serverId);
            ps = con.prepareStatement(sql);
            ps.setString(1, type);
            rs = ps.executeQuery();
            while (rs.next()) {
                permission.setType(rs.getString("type"));
                permission.setIs_user(rs.getBoolean("is_user"));
                permission.setAddress(rs.getString("address"));
                return permission;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return permission;
    }

    public List<Permission> getPermissions(String serverId, String userId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Permission> permissionList = new ArrayList<Permission>();
        String sql = typeSql.getSqlByType(serverId, Params.SqlType.PERMISSION.getValue());

        try {
            con = sqlConnectionFactory.getConnectByServerType(serverId);
            ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Permission permission = new Permission();
                permission.setType(rs.getString("type"));
                permission.setIs_user(rs.getBoolean("is_user"));
                permission.setAddress(rs.getString("address"));
                permissionList.add(permission);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return permissionList;
    }

    public List<String> getRoles(String serverId, String userId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<String> roleNameList = new ArrayList<String>();
        String sql = typeSql.getSqlByType(serverId, Params.SqlType.ROLE.getValue());

        try {
            con = sqlConnectionFactory.getConnectByServerType(serverId);
            ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                roleNameList.add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return roleNameList;
    }
}
