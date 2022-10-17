package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.controller.Prompt;
import com.revature.model.ReimbursementTicket;
import com.revature.model.User;
import com.revature.service.TicketServiceAPI;

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

@WebServlet("/ticket")
public class TicketServlet extends HttpServlet {
    private static final Prompt prompt = Prompt.getPrompt();
    private static final ObjectMapper mapper = Prompt.mapper;
    private static User loggedInUser;

    // check for a logged-in user before calling any of the other methods
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        HttpSession session = req.getSession(false); // if they don't have a session do not make one!

        if (session == null){
            HashMap<String, Object> errorMsg = new HashMap<>();
            errorMsg.put("code", 400);
            errorMsg.put("message", "You must be logged in to perform this action");
            errorMsg.put("timestamp", LocalDateTime.now().toString());

            resp.setStatus(400);
            resp.getWriter().write(mapper.writeValueAsString(errorMsg));
            return;
        }

        loggedInUser = mapper.readValue((String) session.getAttribute("auth-user"), User.class);

        super.service(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // post new ticket method

        // store the provided registration info in a hashmap

        HashMap<String, Object> credentials = mapper.readValue(req.getInputStream(), HashMap.class);
        String providedAmount      = (String) credentials.get("amount");
        String providedDescription = (String) credentials.get("description");

        TicketServiceAPI ts = new TicketServiceAPI();

        if (ts.submitTicket(loggedInUser,providedAmount,providedDescription)){
            prompt.log("Successfully posted new ticket.");

            HashMap<String, Object> successMsg = new HashMap<>();
            successMsg.put("code", 201);
            successMsg.put("message", "Successfully posted ticket.");
            successMsg.put("timestamp", LocalDateTime.now().toString());

            resp.setStatus(201);
            resp.getWriter().write(mapper.writeValueAsString(successMsg));
        } else {
            prompt.log("There was an issue posting this ticket");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // view tickets method

        // make request for tickets on the user's behalf
        TicketServiceAPI ts = new TicketServiceAPI();

        List<ReimbursementTicket> tickets = ts.displayUserTickets(loggedInUser);

        if (tickets != null){
            resp.setStatus(200);

            // i don't want to show every attribute to every ticket, especially not passwords
            //so, let's create a list of maps to send off instead
            List<Map<String, Object>> msg = new ArrayList<>();

            for (ReimbursementTicket ticket: tickets){
                Map<String, Object> t = new HashMap<>();
                t.put("ticket_owner", ticket.getUser().getUsername());
                t.put("ticket_id", ticket.getId());
                t.put("ticket_status", ticket.getStatus());
                t.put("ticket_description", ticket.getDescription());
                t.put("ticket_amount", ts.floorDoubleToPrecision(ticket.getAmount(), 2));
                t.put("created_time", ticket.getCreatedTime().toString());
                if (ticket.getFulfilledTime() != null){
                    t.put("fulfilled_time", ticket.getFulfilledTime().toString());
                }
                msg.add(t);
            }

            resp.getWriter().write(mapper.writeValueAsString(msg));
        }
    }
}
