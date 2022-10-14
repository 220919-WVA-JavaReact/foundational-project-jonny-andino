package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.controller.Prompt;
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

@WebServlet("/ticket")
public class TicketServlet extends HttpServlet {
    private static final Prompt prompt = Prompt.getPrompt();

    private final ObjectMapper mapper = Prompt.mapper;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        // store the provided registration info in a hashmap

        //check for a logged-in user
        HttpSession session = req.getSession(false); // if they don't have a session do not make one!

        if (session == null){
            HashMap<String, Object> errorMsg = new HashMap<>();
            errorMsg.put("Error code", 400);
            errorMsg.put("Message", "No user found with provided credentials");
            errorMsg.put("Timestamp", LocalDateTime.now().toString());

            resp.getWriter().write(mapper.writeValueAsString(errorMsg));
            return;
        }

        User loggedInUser = mapper.readValue((String) session.getAttribute("auth-user"), User.class);

        HashMap<String, Object> credentials = mapper.readValue(req.getInputStream(), HashMap.class);
        String providedAmount      = (String) credentials.get("amount");
        String providedDescription = (String) credentials.get("description");

        TicketServiceAPI ts = new TicketServiceAPI();
        boolean success = ts.submitTicket(loggedInUser,providedAmount,providedDescription);
    }
}
