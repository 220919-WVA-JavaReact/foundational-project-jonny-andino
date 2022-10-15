package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.controller.Prompt;
import com.revature.model.User;
import com.revature.service.UserServiceAPI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    private static final Prompt prompt = Prompt.getPrompt();
    private static final ObjectMapper mapper = Prompt.mapper;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // register new user

        resp.setContentType("application/json");
        // store the provided registration info in a hashmap

        HashMap<String, Object> credentials = mapper.readValue(req.getInputStream(), HashMap.class);
        String providedUsername = (String) credentials.get("username");
        String providedPassword = (String) credentials.get("password");

        // return a user if registration successful
        UserServiceAPI us = new UserServiceAPI();
        User registeredUser = us.register(providedUsername,providedPassword);

        if (registeredUser != null) {
            prompt.log("Successfully registered user (id: " + registeredUser.getId() + ")");
            HttpSession session = req.getSession();

            session.setAttribute("auth-user", mapper.writeValueAsString(registeredUser));

            resp.setStatus(200);
            resp.getWriter().write(mapper.writeValueAsString(registeredUser));
            return;
        }

        resp.setStatus(400);

        prompt.log("Unsuccessful registration attempt");

        HashMap<String, Object> errorMsg = new HashMap<>();
        errorMsg.put("code", 400);
        errorMsg.put("message", "There was an issue with your registration");
        errorMsg.put("timestamp", LocalDateTime.now().toString());

        resp.getWriter().write(mapper.writeValueAsString(errorMsg));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // login method
        resp.setContentType("application/json");
        // store the provided login info in a hashmap

        HashMap<String, Object> credentials = mapper.readValue(req.getInputStream(), HashMap.class);
        String providedUsername = (String) credentials.get("username");
        String providedPassword = (String) credentials.get("password");

        // return a user if login successful
        UserServiceAPI us = new UserServiceAPI();
        User loggedInUser = us.login(providedUsername,providedPassword);

        if (loggedInUser != null) {
            prompt.log("Successfully logged in user (id: " + loggedInUser.getId() + ")");
            HttpSession session = req.getSession();

            session.setAttribute("auth-user", mapper.writeValueAsString(loggedInUser));

            resp.setStatus(200);
            resp.getWriter().write(mapper.writeValueAsString(loggedInUser));
            return;
        }

        prompt.log("Unsuccessful login attempt");

        resp.setStatus(400);

        HashMap<String, Object> errorMsg = new HashMap<>();
        errorMsg.put("code", 400);
        errorMsg.put("message", "No user found with provided credentials");
        errorMsg.put("timestamp", LocalDateTime.now().toString());

        resp.getWriter().write(mapper.writeValueAsString(errorMsg));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //logout method
        HttpSession session = req.getSession(false); // false means do not create a session if one is not found

        if (session != null){
            User user = mapper.readValue((String) session.getAttribute("auth-user"), User.class);
            prompt.log("Successfully logged out user (id: " + user.getId() + ")");

            session.invalidate();
        }
    }
}
