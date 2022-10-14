package com.revature.service;

import com.revature.dao.TicketDAO;
import com.revature.dao.TicketDAOImpl;
import com.revature.model.ReimbursementTicket;
import com.revature.model.User;

import java.util.List;

public class TicketServiceAPI {

    public boolean submitTicket(User loggedInUser, String amount, String description){

        TicketDAO td = new TicketDAOImpl();
        ReimbursementTicket t = null;

        if (!amount.equals("") && !description.equals("")){
            t = new ReimbursementTicket(loggedInUser, Double.parseDouble(amount), description);

            return td.postNewTicket(t);
        }

        return false;
    }

    public List<ReimbursementTicket> displayUserTickets(User user){
        TicketDAO td = new TicketDAOImpl();

        // further validation can happen here?

        return td.getTicketsByUser(user);
    }
}
