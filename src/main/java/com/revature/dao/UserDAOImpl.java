package com.revature.dao;

import com.revature.model.User;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
            //System.out.println("No user found.");
        }
        return u;
    }

    @Override
    public User registerNewUser(String username, String password) {
        User u = new User();

        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "INSERT INTO users (username, password) VALUES (?,?) RETURNING *";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs;

            if ((rs = stmt.executeQuery()) != null) {
                rs.next();
                int receivedId = rs.getInt("user_id");
                String receivedUsername = rs.getString("username");
                String receivedPassword = rs.getString("password");
                boolean receivedAdmin = rs.getBoolean("is_admin");
                // assign final reference

                u = new User(receivedId,receivedUsername,receivedPassword,receivedAdmin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public Map<String, Integer> countUserTickets(User user)  {

        Map<String, Integer> info = new HashMap<>();
        info.put("Open", 0);
        info.put("Closed", 0);

        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM tickets WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, user.getId());
            ResultSet rs;

            if ((rs = stmt.executeQuery()) != null) {
                while (rs.next()){
                    String status = rs.getString("status");
                    if (status.equals("APPROVED") || status.equals("REJECTED")) {
                        info.put("Closed", info.get("Closed") + 1);
                    } else {
                        info.put("Open", info.get("Open") + 1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return info;
    }
}
