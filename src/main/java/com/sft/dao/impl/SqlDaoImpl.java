package com.sft.dao.impl;

import com.sft.dao.SqlDao;
import com.sft.db.PrivateSqlConnectionFactory;
import com.sft.model.SqlModel;
import com.sft.model.TypeModel;
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
public class SqlDaoImpl implements SqlDao {

    @Resource
    PrivateSqlConnectionFactory privateSqlConnectionFactory;

    public boolean addSql(SqlModel sqlModel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "insert into typesql (type,server_id,value) values(?,?,?)";

        try {
            con = privateSqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, sqlModel.getTypeId());
            ps.setString(2, sqlModel.getServerId());
            ps.setString(3, sqlModel.getSql());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privateSqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return false;
    }

    public boolean updateSql(SqlModel sqlModel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("update typesql set id=?");
        if (StringUtils.hasText(sqlModel.getTypeId())) {
            sb.append(",type = '").append(sqlModel.getTypeId()).append("'");
        }
        if (StringUtils.hasText(sqlModel.getServerId())) {
            sb.append(",server_id = '").append(sqlModel.getServerId()).append("'");
        }
        if (StringUtils.hasText(sqlModel.getSql())) {
            sb.append(",value = '").append(sqlModel.getSql()).append("'");
        }
        sb.append(" where id = ?");

        try {
            con = privateSqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setInt(1, sqlModel.getId());
            ps.setInt(2, sqlModel.getId());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privateSqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return false;
    }

    public List<TypeModel> getType() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "select id,type,name from typetable";

        List<TypeModel> typeList = new ArrayList<TypeModel>();
        try {
            con = privateSqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                TypeModel typeModel = new TypeModel();
                typeModel.setId(rs.getInt("id"));
                typeModel.setType(rs.getString("type"));
                typeModel.setName(rs.getString("name"));

                typeList.add(typeModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privateSqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return typeList;
    }

    public int getCount(Map<String, String> whereMap) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("select count(1) as count from typesql");
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

    public List<SqlModel> getSql(Map<String, String> whereMap, int page, int pageSize) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("select t1.*,t2.name as server_name,t3.id as type_id,t3.type as type_value,t3.name as type_name from typesql t1,server t2,typetable t3");
        sb.append(" where t1.server_id = t2.id and t1.type = t3.id");
        if (whereMap != null) {
            for (String key : whereMap.keySet()) {
                sb.append(" and ").append(key).append(" = '").append(whereMap.get(key)).append("'");
            }
        }
        sb.append(" limit ");
        sb.append((page - 1) * pageSize).append(",").append(pageSize);

        List<SqlModel> sqlList = new ArrayList<SqlModel>();
        try {
            con = privateSqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                SqlModel sqlModel = new SqlModel();
                sqlModel.setId(rs.getInt("id"));
                sqlModel.setServerId(rs.getString("server_id"));
                sqlModel.setSql(rs.getString("value"));
                sqlModel.setServerName(rs.getString("server_name"));
                sqlModel.setType(rs.getString("type_value"));
                sqlModel.setTypeName(rs.getString("type_name"));
                sqlModel.setTypeId(rs.getString("type_id"));

                sqlList.add(sqlModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privateSqlConnectionFactory.closeConnetion(con, ps, rs);
        }

        return sqlList;
    }
}
