package com.revature.util;

//represents a physical connection to our database

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//singleton connection instance
public class ConnectionUtil {

    private static Connection conn = null;

    private ConnectionUtil(){}

    public static Connection getConnection(){
        // check to see if there is a connection instance already
        // if there is, return it
        try {
            if (conn != null && !conn.isClosed()){
                System.out.println("Using a previously made connection.");
                return conn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String url = System.getenv("url");
        String username = System.getenv("username");
        String password = System.getenv("password");

        try {
            System.out.println("Creating new connection...");
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Couldn't establish connection...");
            throw new RuntimeException(e);
        }

        return conn;
    }
}
