package com.revature.service;

import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.model.User;

import java.util.Base64;

public class UserServiceAPI {

    public User login(String username, String password){
        UserDAO userDao = new UserDAOImpl();
        User u = userDao.getByUsername(username);

        String encodedPass = Base64.getEncoder().encodeToString(password.getBytes());

        if (u != null && u.getPassword().equals(encodedPass)) {
            return u;
        }

        return null;
    }

    public User register(String username, String password){
        UserDAO userDao = new UserDAOImpl();

        User foundUser = userDao.getByUsername(username);

        if (foundUser == null){
            String encodedPass = Base64.getEncoder().encodeToString(password.getBytes());
            return userDao.registerNewUser(username, encodedPass);
        }
        return null;
    }
}
