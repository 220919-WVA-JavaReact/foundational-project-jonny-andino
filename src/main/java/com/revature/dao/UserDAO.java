package com.revature.dao;

import com.revature.model.User;

public interface UserDAO {
    User getByUsername(String username);

    User registerNewUser(String username, String password);
}
