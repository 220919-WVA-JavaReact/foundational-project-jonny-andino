package com.revature.dao;

import com.revature.model.ReimbursementTicket;
import com.revature.model.User;

import java.util.List;

public interface TicketDAO {

    ReimbursementTicket getTicketById(int id);

    boolean postNewTicket(ReimbursementTicket ticket);

    List<ReimbursementTicket> getTicketsByUser(User user);


}
