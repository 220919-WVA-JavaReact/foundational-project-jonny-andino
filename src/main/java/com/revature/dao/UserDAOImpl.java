package com.revature.dao;

import com.revature.model.User;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO{

    @Override
    public User getByUsername(String username) {
        User u = null;

        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * from users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1,username);
            ResultSet rs;

            if ((rs = stmt.executeQuery()) != null) {
                rs.next();

                int id = rs.getInt("user_id");
                String uname = rs.getString("username");
                String pass = rs.getString("password");
                boolean isAdmin = rs.getBoolean("is_admin");

                u = new User(id,uname,pass,isAdmin);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return u;
    }
}
