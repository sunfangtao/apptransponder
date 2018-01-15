package com.sft.db;

import com.sft.util.Util;
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
    PrivateSqlConnectionFactory sqlConnectionFactory;

    Map<String, String> sqlMap = new HashMap<String, String>();

    public void clearSqlMap() {
        synchronized (this) {
            sqlMap.clear();
        }
    }

    public String getSqlByType(String serverId, String type) {
        synchronized (this) {
            String key = Util.getConnectionKey(serverId, type);

            String sql = sqlMap.get(key);
            if (StringUtils.hasText(sql)) {
                return sql;
            }

            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                connection = sqlConnectionFactory.getConnection();
                ps = connection.prepareStatement("select value from typesql where type = ? and server_id = ?");
                ps.setString(1, type);
                ps.setString(2, serverId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    sqlMap.put(key, rs.getString("value"));
                    return sqlMap.get(key);
                }
            } catch (Exception e) {

            } finally {
                sqlConnectionFactory.closeConnetion(connection, ps, rs);
            }
            throw new IllegalArgumentException("type 为空");
        }
    }
}
