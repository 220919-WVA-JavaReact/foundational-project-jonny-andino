package com.revature.service;

import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.model.User;
import com.revature.util.AuthUtil;


public class UserServiceAPI {
    private static final AuthUtil auth = AuthUtil.getAuthUtil();
    private static final UserDAO userDao = new UserDAOImpl();

    public User login(String username, String password) {
        User u = userDao.getByUsername(username);

        String salt = auth.retrieveUserSalt(username);
        String encodedPass = auth.getHashedPassword(password, salt);

        System.out.println(encodedPass);

        if (u != null && u.getPassword().equals(encodedPass)) {
            return u;
        }

        return null;
    }

    public User register(String username, String password) {

        User foundUser = userDao.getByUsername(username);

        if (foundUser == null){
            String salt = auth.createSalt();
            auth.saveUserSalt(username, salt);
            String encodedPass = auth.getHashedPassword(password, salt);
            return userDao.registerNewUser(username, encodedPass);
        }
        return null;
    }

    public boolean updateUserStatus(int userId, boolean isAdmin){

        return userDao.changeUserRole(userId, isAdmin);
    }
}
