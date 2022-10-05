package com.revature;

import com.revature.model.User;
import com.revature.service.TicketService;
import com.revature.service.UserService;
import com.revature.util.Prompt;

public class Main {

    public static void main(String[] args) {
        Prompt mainPrompt = new Prompt();
        UserService us = new UserService();
        TicketService ts = new TicketService();
        User loggedInUser = null;

        System.out.println("Welcome! Please choose one of the following options");
        System.out.println("1 - Login");
        System.out.println("2 - Register an account");

        switch(Integer.parseInt(mainPrompt.ask())){
            case 1: // login
                loggedInUser = us.login();
                break;
            case 2: // register
                loggedInUser = us.register();
                break;
            default:
                System.out.println("Invalid input... Restarting");
                main(new String[]{});
        }

        if (loggedInUser != null){
            System.out.println("Welcome back, " + loggedInUser.getUsername() + "!");
            System.out.println("What would you like to do today?");
            System.out.println("1 - Create a reimbursement ticket");
            System.out.println("2 - View existing ticket(s)");
            if (loggedInUser.isAdmin()) {
                System.out.println("3 - Admin dashboard");
            }

            switch(Integer.parseInt(mainPrompt.ask())){
                case 1: // create a ticket
                    ts.submitTicket(loggedInUser.getId());
                    break;
                case 2: // view tickets
                    ts.displayUserTickets(loggedInUser.getId());
                    break;
                case 3: // admin menu
                    break;
            }
        }
    }
}
