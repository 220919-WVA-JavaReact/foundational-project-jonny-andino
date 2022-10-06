package com.revature.dao;

import com.revature.model.User;

import java.util.Map;

public interface UserDAO {
    User getByUsername(String username);

    User registerNewUser(String username, String password);

    Map<String, Integer> countUserTickets(User user);
}
