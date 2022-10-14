package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.controller.Prompt;
import com.revature.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

@WebServlet("/manager")
public class ManagerServlet extends HttpServlet {

    private static final Prompt prompt = Prompt.getPrompt();

    private final ObjectMapper mapper = Prompt.mapper;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        // verify logged-in user is a manager before processing any of these requests

        HttpSession session = req.getSession(false);

        if (session == null){
            HashMap<String, Object> errorMsg = new HashMap<>();
            errorMsg.put("code", 401);
            errorMsg.put("message", "You must be logged in to view these options.");
            errorMsg.put("timestamp", LocalDateTime.now().toString());

            resp.setStatus(401);
            resp.getWriter().write(mapper.writeValueAsString(errorMsg));
            return;
        }

        User loggedInUser = mapper.readValue((String) session.getAttribute("auth-user"), User.class);

        if (loggedInUser.isAdmin()) {
            super.service(req, resp);
        } else {
            HashMap<String, Object> errorMsg = new HashMap<>();
            errorMsg.put("code", 401);
            errorMsg.put("message", "You must be an administrator to view these options.");
            errorMsg.put("timestamp", LocalDateTime.now().toString());

            resp.setStatus(401);
            resp.getWriter().write(mapper.writeValueAsString(errorMsg));
        }
    }
}
