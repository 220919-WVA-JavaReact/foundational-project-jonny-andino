package com.revature.dao;

import com.revature.model.ReimbursementTicket;
import com.revature.model.User;
import com.revature.util.ConnectionUtil;
import com.revature.util.ReimbursementType;
import com.revature.util.TicketStatus;

import java.sql.*;
import java.util.*;

public class TicketDAOImpl implements TicketDAO{
    @Override
    public ReimbursementTicket getTicketById(int id) {

        ReimbursementTicket ticket = null;
        UserDAO ud = new UserDAOImpl();

        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * from tickets WHERE ticket_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1,id);
            ResultSet rs;


            if ((rs = stmt.executeQuery()) != null) {
                rs.next();

                int user_id = rs.getInt("user_id");
                float amt = rs.getFloat("amount");
                String desc = rs.getString("description");
                TicketStatus status = TicketStatus.valueOf(rs.getString("status"));
                ReimbursementType type = ReimbursementType.valueOf(rs.getString("type"));
                Timestamp created = rs.getTimestamp("created_time");
                Timestamp fulfilled = rs.getTimestamp("fulfilled_time");

                User foundUser = ud.getById(user_id);

                ticket = new ReimbursementTicket(id,foundUser,amt,desc,status,type,created,fulfilled);
            }
        } catch(SQLException e){
            //System.out.println("No user found.");
        }
        return ticket;
    }

    @Override
    public boolean postNewTicket(ReimbursementTicket ticket) {
        try (Connection conn = ConnectionUtil.getConnection()){
            String sql =
                "INSERT INTO tickets (user_id, amount, description, type, created_time)" +
                "VALUES (?,?,?,?::ticket_type,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, ticket.getUser().getId());
            stmt.setDouble(2, ticket.getAmount());
            stmt.setString(3,ticket.getDescription());
            stmt.setString(4, ticket.getType().toString());
            stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

            int rowsUpdated = stmt.executeUpdate();

            return (rowsUpdated > 0);

        } catch (SQLException e) {
            //e.printStackTrace();
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
                    TicketStatus status = TicketStatus.valueOf(rs.getString("status"));
                    ReimbursementType type = ReimbursementType.valueOf(rs.getString("type"));
                    Timestamp created = rs.getTimestamp("created_time");
                    Timestamp fulfilled = rs.getTimestamp("fulfilled_time");

                    tickets.add(new ReimbursementTicket(id,user,amt,desc,status,type,created,fulfilled));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public List<ReimbursementTicket> getTicketsByStatus(TicketStatus status) {
        List<ReimbursementTicket> tickets = new ArrayList<>();

        Map<Integer, User> knownUsersById = new HashMap<>();
        UserDAO ud = new UserDAOImpl();

        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM tickets WHERE status = ?::ticket_status";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, status.toString());
            ResultSet rs;

            if ((rs = stmt.executeQuery()) != null) {
                while (rs.next()){
                    int id = rs.getInt("ticket_id");
                    float amt = rs.getFloat("amount");
                    String desc = rs.getString("description");
                    int user_id = rs.getInt("user_id");
                    ReimbursementType type = ReimbursementType.valueOf(rs.getString("type"));
                    Timestamp created = rs.getTimestamp("created_time");
                    Timestamp fulfilled = rs.getTimestamp("fulfilled_time");

                    User foundUser;

                    if (knownUsersById.containsKey(user_id)) {
                        foundUser = knownUsersById.get(user_id);
                    } else {
                        foundUser = ud.getById(user_id);
                        knownUsersById.put(user_id, foundUser);
                    }

                    tickets.add(new ReimbursementTicket(id,foundUser,amt,desc,status,type,created,fulfilled));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public List<ReimbursementTicket> getTicketsByUserType(ReimbursementType type, User user) {
        List<ReimbursementTicket> tickets = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM tickets WHERE type = ?::ticket_type AND user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, type.toString());
            stmt.setInt(2, user.getId());
            ResultSet rs;

            if ((rs = stmt.executeQuery()) != null) {
                while (rs.next()){
                    int id = rs.getInt("ticket_id");
                    float amt = rs.getFloat("amount");
                    String desc = rs.getString("description");
                    TicketStatus status = TicketStatus.valueOf(rs.getString("status"));
                    Timestamp created = rs.getTimestamp("created_time");
                    Timestamp fulfilled = rs.getTimestamp("fulfilled_time");


                    tickets.add(new ReimbursementTicket(id,user,amt,desc,status,type,created,fulfilled));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public List<ReimbursementTicket> getAllTickets() {
        List<ReimbursementTicket> tickets = new ArrayList<>();

        // i want to keep track of users i pull from the database in the while loop
        // so that we're not making so many unnecessary calls to db,
        // so im going to store them in this map
        Map<Integer, User> knownUsersById = new HashMap<>();
        UserDAO ud = new UserDAOImpl();

        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM tickets";
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs;

            if ((rs = stmt.executeQuery()) != null) {
                while (rs.next()){
                    int id = rs.getInt("ticket_id");
                    int user_id = rs.getInt("user_id");
                    float amt = rs.getFloat("amount");
                    String desc = rs.getString("description");
                    TicketStatus status = TicketStatus.valueOf(rs.getString("status"));
                    ReimbursementType type = ReimbursementType.valueOf(rs.getString("type"));
                    Timestamp created = rs.getTimestamp("created_time");
                    Timestamp fulfilled = rs.getTimestamp("fulfilled_time");

                    User foundUser;

                    if (knownUsersById.containsKey(user_id)) {
                        foundUser = knownUsersById.get(user_id);
                    } else {
                        // call the userDAO method only if we didn't already grab this
                        // user's info, also store them in the map here.
                        foundUser = ud.getById(user_id);
                        knownUsersById.put(user_id, foundUser);
                    }

                    tickets.add(new ReimbursementTicket(id,foundUser,amt,desc,status,type,created,fulfilled));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public boolean updateTicketStatus(ReimbursementTicket ticket, TicketStatus status) {
        try (Connection conn = ConnectionUtil.getConnection()){
            String sql =
                    "UPDATE tickets SET (status, fulfilled_time) = (?::ticket_status,?) " +
                            "WHERE ticket_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1,status.toString());
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(3, ticket.getId());

            int rowsUpdated = stmt.executeUpdate();

            return (rowsUpdated > 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
