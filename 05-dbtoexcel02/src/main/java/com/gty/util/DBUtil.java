package com.gty.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {

    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String ip = "localhost";
    private static String port = "3306";
    private static String db = "dev";
    private static String name = "root";
    private static String password = "112233";
    public static String tableName = "t_user";

    /**
     * 加载驱动
     */
    private static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(generateDBUrl(ip, port, db), name, password);
        } catch (ClassNotFoundException e) {
            System.out.println("can not load jdbc driver" + e);
        } catch (SQLException e) {
            System.out.println("get connection failure" + e);
        }
        return conn;
    }

    /**
     * 生成一个连接url
     */
    private static String generateDBUrl(String ip, String port, String dbname) {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://").append(ip);
        sb.append(":").append(port);
        sb.append("/").append(dbname);
        //sb.append("?useUnicode=true&amp;characterEncoding=utf-8&amp;autoreconnect=true&useSSL=false");
        sb.append("?useUnicode=true&characterEncoding=UTF-8&autoreconnect=true&useSSL=false&rewriteBatchedStatements=true&serverTimezone=GMT%2B8");
        return sb.toString();
    }

    /**
     * 关闭数据库连接
     */
    private static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取表中字段的所有注释
     * @return
     */
    public static List<String> getColumnComments() {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = "SELECT * FROM " + tableName;
        List<String> columnComments = new ArrayList<>();//列名注释集合
        ResultSet rs = null;
        try {
            pStemt = conn.prepareStatement(tableSql);
            rs = pStemt.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                columnComments.add(rs.getString("Comment"));
            }
            //去掉第一列的id
            columnComments.remove(0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return columnComments;
    }

    /**
     * 获取数据库表中的数据,查询出来的每一行封装成一个list
     */
    public static List<List<String>> selectRowToList(String selectSQL) {
        List<List<String>> result = new ArrayList<List<String>>();
        Connection conn = getConnection();
        PreparedStatement pst = null;

        try {
            pst = (PreparedStatement) conn.prepareStatement(selectSQL);
            ResultSet rs = pst.executeQuery();
            //获取元数据
            ResultSetMetaData rmd = rs.getMetaData();
            //获取总的列数
            int columnCount = rmd.getColumnCount();
            while (rs.next()) {
                List<String> row = new ArrayList<String>();
                //从第一列开始去掉id列
                for (int i = 1; i < columnCount; i++) {
                    row.add(rs.getObject(i+1).toString());
                }
                result.add(row);
            }
        } catch (Exception e) {
            System.out.println("execute selectSQL failure ");
        } finally {
            closeConnection(conn);
        }
        return result;
    }

    /**
     * 按字段别名拼装结果,每行数据返回一个map
     */
    public static List<Map<String, Object>> selectRowToMap(String selectSQL) {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        // 定义一个list用于接受数据库查询到的内容
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            pst = (PreparedStatement) conn.prepareStatement(selectSQL);
            ResultSet rs = pst.executeQuery();
            //获取元数据
            ResultSetMetaData rmd = rs.getMetaData();
            //获取总的列数
            int columnCount = rmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> rsMap = new LinkedHashMap<String, Object>();
                //从1列开始,去掉id列
                for (int i = 1; i < columnCount; i++) {
                    //循环每一列，并取得该列的值，以别名方式
                    rsMap.put(rmd.getColumnLabel(i+1), rs.getString(i+1));
                }
                list.add(rsMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return list;
    }

}
