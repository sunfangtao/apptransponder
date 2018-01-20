package com.sft.db;

import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Service
public class TypeSql {
    //
    @Resource
    PrivateSqlConnectionFactory privateSqlConnectionFactory;

    Map<String, String> sqlMap = new HashMap<String, String>();

    public void clearSqlMap() {
        synchronized (this) {
            sqlMap.clear();
        }
    }

    public String getSqlByType(String serverId, String type) {
        synchronized (this) {

            String sql = sqlMap.get(serverId + "-" + type);
            if (StringUtils.hasText(sql)) {
                return sql;
            }

            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                connection = privateSqlConnectionFactory.getConnection();
                ps = connection.prepareStatement("select t1.value from typesql t1,typetable t2 where t1.type = t2.id and t1.server_id = ? and t2.type = ?");
                ps.setString(1, serverId);
                ps.setString(2, type);
                rs = ps.executeQuery();
                while (rs.next()) {
                    sqlMap.put(serverId + "-" + type, rs.getString("value"));
                    return sqlMap.get(serverId + "-" + type);
                }
            } catch (Exception e) {

            } finally {
                privateSqlConnectionFactory.closeConnetion(connection, ps, rs);
            }
            throw new IllegalArgumentException("type 为空");
        }
    }
}
