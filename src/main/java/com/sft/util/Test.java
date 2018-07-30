package com.sft.util;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Test {

    private static final String Class_Name = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\Administrator\\Desktop\\china_cities_v2.db";

    private static final float meterPerLat = 111194.871f;
    private static final float meterPerLng = 111194.871f;

    public static void main(String args[]) {
        // load the sqlite-JDBC driver using the current class loader
        Connection connection = null;
        try {
            connection = createConnection();
            func1(connection);
            System.out.println("Success!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }

    // 创建Sqlite数据库连接
    public static Connection createConnection() throws SQLException, ClassNotFoundException {
        Class.forName(Class_Name);
        return DriverManager.getConnection(DB_URL);
    }

    public static void func1(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        Statement statement1 = connection.createStatement();
        statement.setQueryTimeout(30); // set timeout to 30 sec.
        // 执行查询语句
        ResultSet rs = statement.executeQuery("select * from cities");

        Map<String, String> map = new HashMap<String, String>();
        while (rs.next()) {
            String col1 = rs.getString("c_name");
            String col2 = rs.getString("c_pinyin");
            System.out.println("col1 = " + col1 + "  col2 = " + col2);

            map.put(col1, col2);
        }

        int i = 0;
        for (String str : map.keySet()) {
            System.out.println("i=" + (++i) + " size=" + map.size());
            String pin = new ChangeToPinYin().getStringPinYin(str);
            statement1.executeUpdate("update cities set c_pinyin='" + pin + "' where c_name='" + str + "'");
        }

    }
}
