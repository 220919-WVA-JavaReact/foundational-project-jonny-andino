package com.revature.service;

import com.revature.dao.TicketDAO;
import com.revature.dao.TicketDAOImpl;
import com.revature.model.ReimbursementTicket;
import com.revature.model.User;
import com.revature.util.ReimbursementType;
import com.revature.util.TicketStatus;

import java.util.List;
import java.util.stream.Collectors;

public class TicketServiceAPI {
    private final TicketDAO td = new TicketDAOImpl();

    public boolean submitTicket(User loggedInUser, String amount, String description, ReimbursementType type){

        ReimbursementTicket t = null;

        if (!amount.equals("") && !description.equals("")){
            t = new ReimbursementTicket(loggedInUser, Double.parseDouble(amount), description, type);

            return td.postNewTicket(t);
        }

        return false;
    }

    public List<ReimbursementTicket> displayUserTickets(User user, ReimbursementType type){

        if (type != ReimbursementType.ALL) return td.getTicketsByUserType(type, user);

        return td.getTicketsByUser(user);
    }

    public List<ReimbursementTicket> getAllPendingTickets(){

        return td.getTicketsByStatus(TicketStatus.PENDING);
    }

    public boolean reviewTicket(int id, TicketStatus status){

        ReimbursementTicket ticket = td.getTicketById(id);

        // closed tickets can not be reupdated

        if (ticket.getStatus() == TicketStatus.UNDER_REVIEW || ticket.getStatus() == TicketStatus.PENDING){
            return td.updateTicketStatus(ticket, status);
        }
        return false;
    }

    public double floorDoubleToPrecision(double val, int precision){
        int scale = (int) Math.pow(10, precision);
        return (double) Math.floor(val * scale) / scale;
    }
}
