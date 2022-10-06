package com.revature.dao;

import com.revature.model.ReimbursementTicket;
import com.revature.model.User;

import java.util.List;

public interface TicketDAO {

    ReimbursementTicket getTicketById(int id);

    ReimbursementTicket postNewTicket();

    List<ReimbursementTicket> getTicketsByUserId(User user);


}
