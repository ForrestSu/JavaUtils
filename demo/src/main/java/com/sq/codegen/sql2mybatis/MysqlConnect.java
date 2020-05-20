package com.sq.codegen.sql2mybatis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlConnect {
    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://192.168.0.216:3306/xone_data";

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://192.168.0.216:3306/xone_data?useSSL=false&serverTimezone=UTC";


    // 创建一个数据库连接
    private Connection conn = null;
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USERNAME = "root";
    static final String PASSWORD = "mysql.admin.pass";

    public MysqlConnect() {}

    /**
     * 获取Connection对象
     */
    public void getConnection() {
        if (conn != null)
            return;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            // System.out.println("成功连接数据库");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not find !", e);
        } catch (SQLException e) {
            throw new RuntimeException("get connection error!", e);
        }
    }

    /**
     * 释放资源
     */
    public void Release() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<TableFields> queryTableScheme(String db, String tableName) {
        List<TableFields> result = new ArrayList<>();
        String sql = "SELECT column_name, column_comment, upper(Data_TYPE) as type" +
                " FROM information_schema.columns\n" +
                " WHERE table_schema = '" + db + "'" +
                " AND table_name = '" + tableName + "' ORDER BY ordinal_position;\n";

        getConnection();
        ResultSet rs = null;
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                // 通过字段检索
                String name = rs.getString("column_name");
                String comment = rs.getString("column_comment");
                String type = rs.getString("type");
                TableFields row = new TableFields(name,type, comment);
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            Release();
        }
        return result;
    }

    /**
     * 查询数据
     */
    private void selectData() {
        // 创建一个结果集对象
        ResultSet rs = null;
        getConnection();
        String sql = "SELECT id, name, code FROM t_institution";
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                // 通过字段检索
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String code = rs.getString("code");
                System.out.println("ID: " + id + ", 站点名称: " + name + ", 站点 code: " + code);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            Release();
        }
    }

    public static void main(String[] args) {
        // new MysqlConnector().SelectData();
        new MysqlConnect().queryTableScheme("xone_data", "t_institution");
        System.out.println("Goodbye!");
    }
}
