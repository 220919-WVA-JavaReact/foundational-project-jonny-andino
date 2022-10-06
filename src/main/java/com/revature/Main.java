package com.revature;

import com.revature.model.User;
import com.revature.service.TicketService;
import com.revature.service.UserService;
import com.revature.controller.Prompt;

public class Main {

    public static void main(String[] args) {
        Prompt mainPrompt = Prompt.getPrompt();
        UserService us = new UserService();
        TicketService ts = new TicketService();
        User loggedInUser = null;
        boolean running = true;

        while(running){
            if (loggedInUser == null){
                mainPrompt.say("Welcome! Please choose one of the following options");
                mainPrompt.say("1 - Login");
                mainPrompt.say("2 - Register an account");
                mainPrompt.say("3 - Quit program");

                switch(mainPrompt.ask()){
                    case "1": // login
                        loggedInUser = us.login();
                        break;
                    case "2": // register
                        loggedInUser = us.register();
                        break;
                    case "3":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid input... Restarting");
                }

            } else {
                mainPrompt.label("USER DASHBOARD");
                mainPrompt.say("Welcome, " + loggedInUser.getUsername() + "!");
                mainPrompt.say("Your user role: " + ((loggedInUser.isAdmin()) ? "Administrator" : "Employee"));
                mainPrompt.say("What would you like to do today?");
                mainPrompt.say("1 - Create a reimbursement ticket");
                mainPrompt.say("2 - View existing ticket(s)");
                mainPrompt.say("3 - Logout");
                if (loggedInUser.isAdmin()) {
                    System.out.println("4 - Admin dashboard");
                }

                switch(Integer.parseInt(mainPrompt.ask())){
                    case 1: // create a ticket
                        ts.submitTicket(loggedInUser);
                        break;
                    case 2: // view tickets
                        ts.displayUserTickets(loggedInUser);
                        break;
                    case 3: // logout
                        mainPrompt.say("logging out...");
                        loggedInUser = null;
                        break;
                    case 4: // admin menu
                        break;
                }
            }
        }
    }
}
