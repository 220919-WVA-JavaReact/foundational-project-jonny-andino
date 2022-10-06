package com.revature.dao;

import com.revature.model.ReimbursementTicket;
import com.revature.model.User;
import com.revature.util.ConnectionUtil;
import com.revature.util.TicketStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAOImpl implements TicketDAO{
    @Override
    public ReimbursementTicket getTicketById(int id) {
        return null;
    }

    @Override
    public boolean postNewTicket(ReimbursementTicket ticket) {
        try (Connection conn = ConnectionUtil.getConnection()){
            String sql =
                "INSERT INTO tickets (user_id,amount,description,created_time)" +
                "VALUES (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, ticket.getUser().getId());
            stmt.setDouble(2, ticket.getAmount());
            stmt.setString(3,ticket.getDescription());
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            int rowsUpdated = stmt.executeUpdate();

            return (rowsUpdated > 0);

        } catch (SQLException e) {
            System.out.println("Couldn't post timestamp");
        }

        return false;
    }

    @Override
    public List<ReimbursementTicket> getTicketsByUser(User user) {
        List<ReimbursementTicket> tickets = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM tickets WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, user.getId());
            ResultSet rs;

            if ((rs = stmt.executeQuery()) != null) {
                while (rs.next()){
                    int id = rs.getInt("ticket_id");
                    float amt = rs.getFloat("amount");
                    String desc = rs.getString("description");
                    TicketStatus status = statusFromString(rs.getString("status"));
                    Timestamp created = rs.getTimestamp("created_time");
                    Timestamp fulfilled = rs.getTimestamp("fulfilled_time");

                    tickets.add(new ReimbursementTicket(id,user,amt,desc,status,created,fulfilled));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    private TicketStatus statusFromString(String str){
        // there's probably a better way to do this lol
        switch(str){
            case "UNDER_REVIEW":
                return TicketStatus.UNDER_REVIEW;
            case "APPROVED":
                return TicketStatus.APPROVED;
            case "REJECTED":
                return TicketStatus.REJECTED;
            default:
                return TicketStatus.PENDING;
        }
    }
}
