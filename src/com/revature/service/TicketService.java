package com.revature.service;

import com.revature.dao.TicketDAO;
import com.revature.dao.TicketTestDAOImpl;
import com.revature.util.Prompt;

public class TicketService {
    Prompt ticketPrompt = new Prompt();
    TicketDAO td = new TicketTestDAOImpl();

    public void displayTicket(){

    }
}
