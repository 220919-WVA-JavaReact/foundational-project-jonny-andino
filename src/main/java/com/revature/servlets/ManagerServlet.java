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
import java.util.HashMap;
import java.util.List;

@WebServlet("/manager")
public class ManagerServlet extends HttpServlet {

    private static final Prompt prompt = Prompt.getPrompt();
    private final ObjectMapper mapper = Prompt.mapper;
    private User loggedInUser;
    private HttpSession session;
    private TicketServiceAPI ts = new TicketServiceAPI();


    @Override
    // use service() to verify logged-in user is a manager before processing any of these requests
    // it's kind of a gateway to any of our managerial actions
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        session = req.getSession(false);

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
        resp.getWriter().write(mapper.writeValueAsString(tickets));
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
            prompt.log("There was an issue updating this ticket");
        }
    }
}
