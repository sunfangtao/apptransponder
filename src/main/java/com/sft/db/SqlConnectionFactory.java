package com.sft.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SqlConnectionFactory {

    @Resource
    PrivateSqlConnectionFactory sqlConnectionFactory;

    public static Map<String, DruidDataSource> druidDataSourceMap = new HashMap<String, DruidDataSource>();

    public Connection getConnectByServerType(String serverId) {
        try {
            if (!druidDataSourceMap.containsKey(serverId)) {
                DruidDataSource druidDataSource = createNewDataSource(serverId);
                if (druidDataSource != null) {
                    druidDataSourceMap.put(serverId, druidDataSource);
                } else {
                    return null;
                }
            }
            return druidDataSourceMap.get(serverId).getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnetion(Connection connection, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (Exception e) {

        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {

        }
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {

        }
    }

    private DruidDataSource createNewDataSource(String serverId) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = sqlConnectionFactory.getConnection();
            ps = connection.prepareStatement("select ip,port,dbname,username,password from server where id = ?");
            ps.setString(1, serverId);
            rs = ps.executeQuery();
            while (rs.next()) {
                StringBuffer sb = new StringBuffer();
                sb.append("jdbc:mysql://");
                sb.append(rs.getString("ip"));
                sb.append(":").append(rs.getString("port"));
                sb.append("/").append(rs.getString("dbname"));
                sb.append("?useUnicode=true&characterEncoding=utf-8");

                DruidDataSource dataSource = new DruidDataSource();
                dataSource.setDriverClassName("com.mysql.jdbc.Driver");
                dataSource.setUrl(sb.toString());
                dataSource.setUsername(rs.getString("username"));
                dataSource.setPassword(rs.getString("password"));
                dataSource.setInitialSize(5);
                dataSource.setMinIdle(1);
                dataSource.setMaxActive(10);
                dataSource.setPoolPreparedStatements(false);

                return dataSource;
            }
        } catch (Exception e) {

        } finally {
            sqlConnectionFactory.closeConnetion(connection, ps, rs);
        }
        return null;
    }

}

