package com.sft.dao.impl;

import com.sft.dao.SqlDao;
import com.sft.db.PrivateSqlConnectionFactory;
import com.sft.model.SqlModel;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class SqlDaoImpl implements SqlDao {

    @Resource
    PrivateSqlConnectionFactory sqlConnectionFactory;

    public boolean addSql(SqlModel sqlModel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "insert into typesql (type,server_id,value) values(?,?,?)";

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, sqlModel.getType());
            ps.setString(2, sqlModel.getServerId());
            ps.setString(3, sqlModel.getSql());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return false;
    }

    public boolean updateSql(SqlModel sqlModel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("update typesql set id=?");
        if (StringUtils.hasText(sqlModel.getType())) {
            sb.append(",type = ").append(sqlModel.getType());
        }
        if (StringUtils.hasText(sqlModel.getServerId())) {
            sb.append(",server_id = ").append(sqlModel.getServerId());
        }
        if (StringUtils.hasText(sqlModel.getSql())) {
            sb.append(",value = ").append(sqlModel.getSql());
        }
        sb.append("where id = ?");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setInt(1, sqlModel.getId());
            ps.setInt(1, sqlModel.getId());
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
