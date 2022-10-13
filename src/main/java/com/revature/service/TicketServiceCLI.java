package com.revature.service;

import com.revature.dao.TicketDAO;
import com.revature.dao.TicketDAOImpl;
import com.revature.model.ReimbursementTicket;
import com.revature.controller.Prompt;
import com.revature.model.User;
import com.revature.util.TicketStatus;

import java.util.List;
import java.util.stream.Collectors;

public class TicketServiceCLI {
    Prompt ticketPrompt = Prompt.getPrompt();

    public void submitTicketCLI(User loggedInUser){
        TicketDAO td = new TicketDAOImpl();
        ReimbursementTicket t = null;

        String amount = ticketPrompt.ask("Please enter the amount for your reimbursement");
        String description = ticketPrompt.ask("Please enter a short description of what this reimbursement is for.");

        if (!amount.equals("") && !description.equals("")){
            t = new ReimbursementTicket(loggedInUser, Double.parseDouble(amount), description);

            if (td.postNewTicket(t)) {
                ticketPrompt.say("Successfully created ticket!");
            }
        } else {
            ticketPrompt.say("Please be sure to add a full amount and description to your ticket.");
            submitTicketCLI(loggedInUser);
        }

    }

    public void reviewTicketCLI(int id){
        TicketDAO td = new TicketDAOImpl();

        ReimbursementTicket ticket = td.getTicketById(id);
        prettyPrintCLI(ticket);

        ticketPrompt.say("Once you have reviewed the ticket contents, please select a status to apply to the ticket.");
        ticketPrompt.say("1 - Approve");
        ticketPrompt.say("2 - Deny");
        ticketPrompt.say("3 - Needs further review");

        TicketStatus selection = ticket.getStatus();
        boolean updated = true;

        switch(ticketPrompt.ask()){
            case "1":
                selection = TicketStatus.APPROVED;
                break;
            case "2":
                selection = TicketStatus.REJECTED;
                break;
            case "3":
                selection = TicketStatus.UNDER_REVIEW;
                break;
            default:
                updated = false;
                ticketPrompt.say("Sorry, that wasn't a valid option");
                ticketPrompt.say("Enter 1 to try again, or press Enter to return to the Admin Dashboard");
                if (ticketPrompt.ask().equals("1")){
                    reviewTicketCLI(id);
                }
                break;
        }

        if (updated) {
            ticketPrompt.say("Applying changes...");
            if (td.updateTicketStatus(ticket, selection)){
                ticketPrompt.say("Successfully applied update.");
            }
        }
    }

    public void displayUserTicketsCLI(User user){
        TicketDAO td = new TicketDAOImpl();

        List<ReimbursementTicket> tickets = td.getTicketsByUser(user);

        for (ReimbursementTicket ticket : tickets){
            prettyPrintCLI(ticket);
        }
    }



    public void displayAllTicketsCLI(){
        TicketDAO td = new TicketDAOImpl();

        List<ReimbursementTicket> tickets = td.getAllTickets();

        for (ReimbursementTicket ticket : tickets){
            prettyPrintCLI(ticket);
            ticketPrompt.say("Ticket ID: " + ticket.getId());
        }
    }

    public void displayAllPendingTicketsCLI(){
        TicketDAO td = new TicketDAOImpl();

        List<ReimbursementTicket> tickets = td.getAllTickets();

        //java is a good and normal language where good and normal lines like this happen
        tickets = tickets.stream().filter(ticket -> ticket.getStatus().equals(TicketStatus.PENDING)).collect(Collectors.toList());

        for (ReimbursementTicket ticket : tickets){
            prettyPrintCLI(ticket);
            ticketPrompt.say("Ticket ID: " + ticket.getId());
        }
    }

    private void prettyPrintCLI(ReimbursementTicket t){
        ticketPrompt.label("REIMBURSEMENT TICKET");
        ticketPrompt.say("Status: " + t.getStatus());
        ticketPrompt.say("User: " + t.getUser().getUsername());
        ticketPrompt.say("Amount: $" + floorDoubleToPrecision(t.getAmount(), 2));
        ticketPrompt.say("Description: " + t.getDescription());
        ticketPrompt.say("Created Time: " + t.getCreatedTime());
        if (t.getStatus() == TicketStatus.APPROVED || t.getStatus() == TicketStatus.REJECTED) {
            ticketPrompt.say("Fulfilled Time: " + t.getFulfilledTime());
        }
    }

    private static double floorDoubleToPrecision(double val, int precision){
        int scale = (int) Math.pow(10, precision);
        return (double) Math.floor(val * scale) / scale;
    }
}
