package com.revature.service;

import com.revature.dao.TicketDAO;
import com.revature.dao.TicketTestDAOImpl;
import com.revature.model.ReimbursementTicket;
import com.revature.util.Prompt;
import com.revature.util.TicketStatus;

public class TicketService {
    Prompt ticketPrompt = new Prompt();
    TicketDAO td = new TicketTestDAOImpl();

    public void submitTicket(int userId){
        String amount = ticketPrompt.ask("Please enter the amount for your reimbursement");
        String description = ticketPrompt.ask("Please enter a short description of what this reimbursement is for.");
        ReimbursementTicket t = new ReimbursementTicket(userId, Double.parseDouble(amount), description, TicketStatus.PENDING);
        //logic for posting the ticket to the db
        System.out.println(t);
    }

    public void displayUserTickets(int userId){
        ReimbursementTicket[] tickets = td.getTicketsByUserId(userId);

        for (ReimbursementTicket t : tickets){
            System.out.println(t);
        }
    }
}
