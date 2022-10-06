package com.revature.service;

import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.model.User;
import com.revature.controller.Prompt;

public class UserService {

    Prompt userPrompt = Prompt.getPrompt();
    public User login(){

        String uname = userPrompt.ask("Please input a username.");
        String pass  = userPrompt.ask("Please enter your Password");

        UserDAO userDao = new UserDAOImpl();
        User u = userDao.getByUsername(uname);

        if (u != null && u.getPassword().equals(pass)) {
            System.out.println(u);
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

        String uname = userPrompt.ask("Please register a username.");


        User foundUser = userDao.getByUsername(uname);

        String pass  = userPrompt.ask("Please register a password.");
        // validation stuff

        return userDao.registerNewUser(uname, pass);
    }
}
