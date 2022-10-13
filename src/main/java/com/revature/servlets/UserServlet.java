package com.revature.servlets;


import com.revature.controller.Prompt;
import com.revature.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/register")
public class UserServlet extends HttpServlet {

    private static final Prompt prompt = Prompt.getPrompt();

    @Override
    public void init() throws ServletException {
        prompt.log("Register servlet initialized.");
    }

    @Override
    public void destroy() {
        prompt.log("Register servlet destroyed.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        prompt.log("Register servlet received a request");

        User newUser = Prompt.mapper.readValue(req.getInputStream(), User.class);

        if (newUser != null) {
            String resPayload = Prompt.mapper.writeValueAsString(newUser);
            res.setStatus(200);
            res.setContentType("application/json");
            res.getWriter().write(resPayload);
        }
    }
}
