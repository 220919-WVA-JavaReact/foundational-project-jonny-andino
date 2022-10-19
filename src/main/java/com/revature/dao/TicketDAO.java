package com.revature.dao;

import com.revature.model.ReimbursementTicket;
import com.revature.model.User;
import com.revature.util.ReimbursementType;
import com.revature.util.TicketStatus;

import java.util.List;

public interface TicketDAO {

    ReimbursementTicket getTicketById(int id);

    boolean postNewTicket(ReimbursementTicket ticket);

    List<ReimbursementTicket> getTicketsByUser(User user);

    List<ReimbursementTicket> getTicketsByStatus(TicketStatus status);

    List<ReimbursementTicket> getTicketsByUserType(ReimbursementType type, User user);

    List<ReimbursementTicket> getAllTickets();

    boolean updateTicketStatus(ReimbursementTicket ticket, TicketStatus status);
}
