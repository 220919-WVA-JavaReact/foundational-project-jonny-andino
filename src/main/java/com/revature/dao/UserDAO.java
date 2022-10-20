package com.revature.dao;

import com.revature.model.User;

import java.util.Map;

public interface UserDAO {
    User getByUsername(String username);

    User getById(int id);

    User registerNewUser(String username, String password);

    boolean changeUserRole (int userId, boolean isAdmin);

    Map<String, Integer> countUserTickets(User user);
}
