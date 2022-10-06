package com.revature.service;

import com.revature.dao.TicketDAO;
import com.revature.dao.TicketDAOImpl;
import com.revature.model.ReimbursementTicket;
import com.revature.controller.Prompt;
import com.revature.model.User;

public class TicketService {
    Prompt ticketPrompt = Prompt.getPrompt();
    TicketDAO td = new TicketDAOImpl();

    public void submitTicket(User loggedInUser){
        String amount = ticketPrompt.ask("Please enter the amount for your reimbursement");
        String description = ticketPrompt.ask("Please enter a short description of what this reimbursement is for.");
        ReimbursementTicket t = new ReimbursementTicket(loggedInUser, Double.parseDouble(amount), description);
        //logic for posting the ticket to the db
        System.out.println(t);
    }

    public void displayUserTickets(User user){

    }
}
