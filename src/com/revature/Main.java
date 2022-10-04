package com.revature;

import com.revature.service.UserService;

public class Main {

    public static void main(String[] args) {
        Prompt userPrompt = new Prompt();
        UserService us = new UserService();

        String uname = userPrompt.ask("Please input a username.");

        String pass = userPrompt.ask("Please enter your Password");
        us.login(uname, pass);
    }
}
