package com.revature.service;

import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.model.User;
import com.revature.controller.Prompt;

import java.util.Base64;
import java.util.Map;

public class UserService {

    Prompt userPrompt = Prompt.getPrompt();
    public User login(){

        String uname = userPrompt.ask("Please input a username.");
        String pass  = userPrompt.ask("Please enter your Password");

        userPrompt.say("Logging in...");
        UserDAO userDao = new UserDAOImpl();
        User u = userDao.getByUsername(uname);

        String encodedPass = Base64.getEncoder().encodeToString(pass.getBytes());

        if (u != null && u.getPassword().equals(encodedPass)) {
            return u;
        }

        userPrompt.say("We're sorry, we didn't find an account matching this information.");
        userPrompt.say("Enter 1 to try again, or press Enter to exit.");

        if (userPrompt.ask().equals("1")){
            u = login();
        }
        return u;
    }

    public User register(){
        UserDAO userDao = new UserDAOImpl();
        User newUser = null;

        String uname = userPrompt.ask("Please register a username.");
        String pass = userPrompt.ask("Please register a password.");

        userPrompt.say("Validating registration...");
        User foundUser = userDao.getByUsername(uname);

        if (foundUser == null){
            String encodedPass = Base64.getEncoder().encodeToString(pass.getBytes());
            newUser = userDao.registerNewUser(uname, encodedPass);
            return newUser;
        }
        userPrompt.say("Sorry, a user with that username already exists.");
        userPrompt.say("Enter 1 to try again, or press Enter to exit.");

        if (userPrompt.ask().equals("1")){
            newUser = register();
        }
        return newUser;
    }

    public int displayTicketInfo(User user){
        UserDAO userDao = new UserDAOImpl();
        Map<String, Integer> info = userDao.countUserTickets(user);
        String out = "";
        if (info.get("Open") > 0){
            out += "\n" + info.get("Open") + " open ticket(s).";
        }

        if (info.get("Closed") > 0){
            out += "\n" + info.get("Closed") + " closed ticket(s).";
        }

        if (!out.equals("")){
            userPrompt.say("You have:" + out);
        }

        return info.get("Open") + info.get("Closed");
    }
}
