package com.revature.dao;

import com.revature.model.ReimbursementTicket;
import com.revature.model.User;
import com.revature.util.TicketStatus;

public class TicketTestDAOImpl implements TicketDAO{

    ReimbursementTicket[] tickets = {
        new ReimbursementTicket(0,0,1000.00,"Work Laptop", TicketStatus.OPEN),
        new ReimbursementTicket(1,0,20.00,"Lunch on the boss today", TicketStatus.OPEN)
    };


    @Override
    public ReimbursementTicket getTicketById(int id) {
        for (ReimbursementTicket ticket : tickets){
            if (ticket.getId() == id) return ticket;
        }
        return null;
    }

    @Override
    public ReimbursementTicket[] getTicketsByUserId(int userId) {
        return tickets;
    }

    @Override
    public User getUserByTicketId(int id) {
        return null;
    }
}
