package com.revature.service;

import com.revature.dao.TicketDAO;
import com.revature.dao.TicketDAOImpl;
import com.revature.model.ReimbursementTicket;
import com.revature.controller.Prompt;
import com.revature.model.User;
import com.revature.util.TicketStatus;

import java.util.ArrayList;
import java.util.List;

public class TicketService {
    Prompt ticketPrompt = Prompt.getPrompt();

    public void submitTicket(User loggedInUser){
        TicketDAO td = new TicketDAOImpl();

        String amount = ticketPrompt.ask("Please enter the amount for your reimbursement");
        String description = ticketPrompt.ask("Please enter a short description of what this reimbursement is for.");
        ReimbursementTicket t = new ReimbursementTicket(loggedInUser, Double.parseDouble(amount), description);
        if (td.postNewTicket(t)) {
            System.out.println("Success!");
            System.out.println(t);
        }
    }

    public void displayUserTickets(User user){
        TicketDAO td = new TicketDAOImpl();

        List<ReimbursementTicket> tickets = td.getTicketsByUser(user);

        for (ReimbursementTicket ticket : tickets){
            prettyPrint(ticket);
        }
    }

    private void prettyPrint(ReimbursementTicket t){
        ticketPrompt.label("REIMBURSEMENT TICKET");
        ticketPrompt.say("Status: " + t.getStatus());
        ticketPrompt.say("User: " + t.getUser().getUsername());
        ticketPrompt.say("Amount: $" + floorDoubleToPrecision(t.getAmount(), 2));
        ticketPrompt.say("Description: " + t.getDescription());
        ticketPrompt.say("Created Time: " + t.getCreatedTime());
        if (t.getStatus() != TicketStatus.PENDING) {
            ticketPrompt.say("Fulfilled Time: " + t.getCreatedTime());
        }
    }

    private static double floorDoubleToPrecision(double val, int precision){
        int scale = (int) Math.pow(10, precision);
        return (double) Math.floor(val * scale) / scale;
    }
}
