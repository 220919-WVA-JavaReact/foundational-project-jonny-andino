package com.revature.dao;

import com.revature.model.ReimbursementTicket;
import com.revature.model.User;

import java.util.ArrayList;
import java.util.List;

public class TicketDAOImpl implements TicketDAO{
    @Override
    public ReimbursementTicket getTicketById(int id) {
        return null;
    }

    @Override
    public ReimbursementTicket postNewTicket() {
        return null;
    }

    @Override
    public List<ReimbursementTicket> getTicketsByUser(User user) {

        return new ArrayList<>();
    }
}
