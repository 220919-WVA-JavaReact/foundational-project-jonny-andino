package com.revature.dao;

import com.revature.model.User;

public class UserTestDAOImpl implements UserDAO{
    @Override
    public User getByUsername(String username) {
        if (username.equals("jonny")) {
            //return a dummy user
            return new User(0, "jonny", "pass", false);
        }
        return null;
    }
}
