package com.revature.util;

//represents a physical connection to our database

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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

        String url      = "";
        String username = "";
        String password = "";

        Properties prop = new Properties();

        try {
            prop.load(new FileReader("src/main/resources/application.properties"));

            url      = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");

            conn = DriverManager.getConnection(url,username,password);
        } catch (IOException e) {
            System.out.println("Properties file not found");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println("Could not establish connection");
            throw new RuntimeException(e);
        }

        return conn;
    }
}
