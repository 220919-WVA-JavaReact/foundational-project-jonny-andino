package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.controller.Prompt;
import com.revature.model.ReimbursementTicket;
import com.revature.model.User;
import com.revature.service.TicketServiceAPI;
import com.revature.util.TicketStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/manager")
public class ManagerServlet extends HttpServlet {

    private static final Prompt prompt = Prompt.getPrompt();
    private static final ObjectMapper mapper = Prompt.mapper;
    private static User loggedInUser;
    private final TicketServiceAPI ts = new TicketServiceAPI();


    @Override
    // use service() to verify logged-in user is a manager before processing any of these requests
    // it's kind of a gateway to any of our managerial actions
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        HttpSession session = req.getSession(false);

        if (session == null){
            prompt.log("Prevented non-logged-in user from accessing manager functionality");

            HashMap<String, Object> errorMsg = new HashMap<>();
            errorMsg.put("code", 401);
            errorMsg.put("message", "You must be logged in to perform this action.");
            errorMsg.put("timestamp", LocalDateTime.now().toString());

            resp.setStatus(401);
            resp.getWriter().write(mapper.writeValueAsString(errorMsg));
            return;
        }

        loggedInUser = mapper.readValue((String) session.getAttribute("auth-user"), User.class);

        if (loggedInUser.isAdmin()) {
            // proceed only if we have a user, and they're confirmed a manager
            prompt.log("Successfully authenticated manager (id: " + loggedInUser.getId() + ")");
            super.service(req, resp);
        } else {
            prompt.log("Prevented non-authorized user (id: " + loggedInUser.getId() + ") from accessing manager functionality");

            HashMap<String, Object> errorMsg = new HashMap<>();
            errorMsg.put("code", 401);
            errorMsg.put("message", "You must be an administrator to perform this action.");
            errorMsg.put("timestamp", LocalDateTime.now().toString());

            resp.setStatus(401);
            resp.getWriter().write(mapper.writeValueAsString(errorMsg));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // view all open tickets

        resp.setContentType("application/json");
        List<ReimbursementTicket> tickets = ts.getAllPendingTickets();

        List<Map<String, Object>> msg = new ArrayList<>();

        for (ReimbursementTicket ticket: tickets){
            Map<String, Object> t = new HashMap<>();
            t.put("ticket_owner", ticket.getUser().getUsername());
            t.put("ticket_id", ticket.getId());
            t.put("ticket_status", ticket.getStatus());
            t.put("ticket_description", ticket.getDescription());
            t.put("ticket_type", ticket.getType());
            t.put("ticket_amount", ts.floorDoubleToPrecision(ticket.getAmount(), 2));
            t.put("created_time", ticket.getCreatedTime().toString());
            if (ticket.getFulfilledTime() != null){
                t.put("fulfilled_time", ticket.getFulfilledTime().toString());
            }
            msg.add(t);
        }

        resp.getWriter().write(mapper.writeValueAsString(msg));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // responding to a given ticket
        resp.setContentType("application.json");

        HashMap<String, Object> inputData = mapper.readValue(req.getInputStream(), HashMap.class);

        Integer ticketId       = (Integer) inputData.get("ticketId");
        TicketStatus newStatus = TicketStatus.valueOf((String) inputData.get("newStatus"));
        // ^ this is the hardest line i ever wrote. i feel good at java now thanks

        if (ts.reviewTicket(ticketId,newStatus)){
            prompt.log("Successfully updated ticket " + ticketId + " to status: " + newStatus.toString());

            HashMap<String, Object> successMsg = new HashMap<>();
            successMsg.put("code", 200);
            successMsg.put("message", "Successfully updated ticket.");
            successMsg.put("ticket_id", ticketId);
            successMsg.put("new_status", newStatus.toString());
            successMsg.put("timestamp", LocalDateTime.now().toString());

            resp.setStatus(200);
            resp.getWriter().write(mapper.writeValueAsString(successMsg));
        } else {
            prompt.log("This ticket could not be updated.");

            HashMap<String, Object> errorMsg = new HashMap<>();
            errorMsg.put("code", 409);
            errorMsg.put("message", "Error: could not update already closed ticket.");
            errorMsg.put("timestamp", LocalDateTime.now().toString());

            resp.setStatus(409);
            resp.getWriter().write(mapper.writeValueAsString(errorMsg));
        }
    }
}
