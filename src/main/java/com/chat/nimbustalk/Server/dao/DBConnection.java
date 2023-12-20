package com.chat.nimbustalk.Server.dao;

import java.sql.*;

public class DBConnection {
    private static Connection con;

    private DBConnection() {
        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String databaseName = System.getenv("DB_NAME");
        String userName = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        if (host == null || port == null || databaseName == null) {
            System.out.println("Host, port, database information is required");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?sslmode=require", userName, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (con == null) {
            new DBConnection();
        }
        return con;
    }



    public static void getUser(int id) {
        String sql = "SELECT * FROM user WHERE id = " + id;
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Full Name: " + rs.getString("fullName"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Phone Number: " + rs.getString("phoneNumber"));
            } else {
                System.out.println("No user found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}