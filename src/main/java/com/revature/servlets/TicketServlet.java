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
import java.util.HashMap;
import java.util.List;

@WebServlet("/ticket")
public class TicketServlet extends HttpServlet {
    private static final Prompt prompt = Prompt.getPrompt();

    private final ObjectMapper mapper = Prompt.mapper;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // post a new ticket

        resp.setContentType("application/json");
        // store the provided registration info in a hashmap

        // check for a logged-in user
        HttpSession session = req.getSession(false); // if they don't have a session do not make one!

        if (session == null){
            HashMap<String, Object> errorMsg = new HashMap<>();
            errorMsg.put("code", 400);
            errorMsg.put("message", "No user found with provided credentials");
            errorMsg.put("timestamp", LocalDateTime.now().toString());

            resp.setStatus(400);
            resp.getWriter().write(mapper.writeValueAsString(errorMsg));
            return;
        }

        User loggedInUser = mapper.readValue((String) session.getAttribute("auth-user"), User.class);

        HashMap<String, Object> credentials = mapper.readValue(req.getInputStream(), HashMap.class);
        String providedAmount      = (String) credentials.get("amount");
        String providedDescription = (String) credentials.get("description");

        TicketServiceAPI ts = new TicketServiceAPI();

        if (ts.submitTicket(loggedInUser,providedAmount,providedDescription)){
            HashMap<String, Object> successMsg = new HashMap<>();
            successMsg.put("code", 201);
            successMsg.put("message", "Successfully posted ticket.");
            successMsg.put("timestamp", LocalDateTime.now().toString());

            resp.setStatus(201);
            resp.getWriter().write(mapper.writeValueAsString(successMsg));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // view tickets
        resp.setContentType("application/json");
        // get the user
        HttpSession session = req.getSession(false);

        // validate session
        if (session == null){
            HashMap<String, Object> errorMsg = new HashMap<>();
            errorMsg.put("code", 401);
            errorMsg.put("message", "You must be logged in to view user tickets.");
            errorMsg.put("timestamp", LocalDateTime.now().toString());

            resp.setStatus(401);
            resp.getWriter().write(mapper.writeValueAsString(errorMsg));
            return;
        }

        User loggedInUser = mapper.readValue((String) session.getAttribute("auth-user"), User.class);

        // make request for tickets on the user's behalf
        TicketServiceAPI ts = new TicketServiceAPI();

        List<ReimbursementTicket> tickets = ts.displayUserTickets(loggedInUser);

        if (tickets != null){
            resp.setStatus(200);
            resp.getWriter().write(mapper.writeValueAsString(tickets));
        }
    }
}
