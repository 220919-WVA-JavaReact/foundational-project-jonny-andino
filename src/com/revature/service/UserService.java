package com.revature.service;

import com.revature.dao.UserDAO;
import com.revature.dao.UserTestDAOImpl;
import com.revature.model.User;

public class UserService {
    public void login(String username, String password){
        UserDAO userdao = new UserTestDAOImpl();
        User u = userdao.getByUsername(username);

        if (u != null && u.getPassword().equals(password)) {
            System.out.println(u);
        }
    }
}
