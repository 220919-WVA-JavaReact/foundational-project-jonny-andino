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

        return null;
    }

    public User register(){
        String uname = userPrompt.ask("Please register a username.");
        String pass  = userPrompt.ask("Please register a password.");

        // validation stuff

        return new User(0, uname, pass, false);
    }
}
