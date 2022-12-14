package com.revature.service;

import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.model.User;
import com.revature.controller.Prompt;
import com.revature.util.AuthUtil;

import java.util.Map;

public class UserServiceCLI {

    private static final Prompt userPrompt = Prompt.getPrompt();
    private static final AuthUtil auth = AuthUtil.getAuthUtil();
    private static final UserDAO userDao = new UserDAOImpl();
    public User loginCLI(){

        String uname = userPrompt.ask("Please input a username.");
        String pass  = userPrompt.ask("Please enter your Password");

        userPrompt.say("Logging in...");

        User u = userDao.getByUsername(uname);

        String salt = auth.retrieveUserSalt(uname);
        String encodedPass = auth.getHashedPassword(pass, salt);

        if (u != null && u.getPassword().equals(encodedPass)) {
            return u;
        }

        userPrompt.say("We're sorry, we didn't find an account matching this information.");
        userPrompt.say("Enter 1 to try again, or press Enter to return to Main Menu.");

        if (userPrompt.ask().equals("1")){
            u = loginCLI();
        }
        return u;
    }

    public User registerCLI(){

        User newUser = null;

        String uname = userPrompt.ask("Please register a username.");
        String pass = userPrompt.ask("Please register a password.");

        userPrompt.say("Validating registration...");
        User foundUser = userDao.getByUsername(uname);

        if (foundUser == null){
            String salt = auth.createSalt();
            auth.saveUserSalt(uname, salt);
            String encodedPass = auth.getHashedPassword(pass, salt);
            newUser = userDao.registerNewUser(uname, encodedPass);
            return newUser;
        }
        userPrompt.say("Sorry, a user with that username already exists.");
        userPrompt.say("Enter 1 to try again, or press Enter to return to Main Menu.");

        if (userPrompt.ask().equals("1")){
            newUser = registerCLI();
        }
        return newUser;
    }

    public int displayTicketInfoCLI(User user){

        Map<String, Integer> info = userDao.countUserTickets(user);

        // start with an empty string
        String out = "";

        // read the values from our map cuz we'll be referencing them a lot
        int open   = info.get("Open");
        int closed = info.get("Closed");

        if (open > 0){
            // if open has a value greater than zero, let's add a string to 'out'
            // first a line break, then the value of out, then we add an 's' to the string
            // only if that value is greater than one.
            out += "\n" + open + " open ticket" + ((open > 1) ? "s" : "");
        }

        if (closed > 0){
            // this is the same thing but for closed tickets
            out += "\n" + closed + " closed ticket" + ((closed > 1) ? "s" : "");
        }

        if (!out.equals("")){
            // if we added anything at all, let's put it all together, adding
            // "you have" to the beginning of the string.
            userPrompt.say("You have:" + out);
        }

        return open + closed;
    }
}
