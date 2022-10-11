package com.revature.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String un = req.getParameter("username");
        String pass = req.getParameter("password");

        res.getWriter().write("Username: " + un + "<br>");
        res.getWriter().write("Password: " + pass);
    }
}
