package com.revature.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet(value = "/register")
@MultipartConfig(
        fileSizeThreshold=1024*1024,
        maxFileSize=1024*1024*5,
        maxRequestSize=1024*1024*5*5
)
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        Enumeration<String> content = req.getAttributeNames();
        out.write("<ul>");

        while (content.hasMoreElements()){
            String paramName = content.nextElement();
            out.write("<li>" + paramName + ": ");
            String paramValue = req.getParameter(paramName);
            out.write(paramValue + "</li>");
        }

        out.write("</ul>");
    }
}
