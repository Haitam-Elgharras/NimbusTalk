package com.chat.nimbustalk.Server.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static Connection con;
    private static DBConnection c;

    /*String jdbcUrl = "jdbc:postgresql://ep-wandering-unit-78681101.eu-central-1.postgres.vercel-storage.com:5432/verceldb";
    String username = "default";
    String password = "4CBf1boiqQhm";*/

    private DBConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/NimbusTalk","root","ilyas-2002");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        if (c == null) c = new DBConnection();
        return con;
    }
}
