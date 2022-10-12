package com.revature.servlets;
import com.revature.controller.Prompt;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(value = "/test")
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,String> testCollection = new HashMap<>();
        testCollection.put("test-key", "test-value");
        testCollection.put("test-key2", "test-value2");
        testCollection.put("test-key3", "test-value3");

        Prompt prompt = Prompt.getPrompt();
        String response = prompt.jsonify(testCollection);
        resp.setStatus(200);
        resp.setHeader("Content-type", "text/json");
        resp.getWriter().write(response);
    }
}