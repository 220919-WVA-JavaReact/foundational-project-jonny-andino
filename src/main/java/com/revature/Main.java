package com.revature;

import com.revature.controller.Page;
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
        Page currentPage = Page.HOME;
        boolean running = true;

        while(running){
            switch(currentPage){
                case HOME:
                    mainPrompt.label("MAIN MENU");
                    mainPrompt.say("Welcome! Please choose one of the following options");
                    mainPrompt.say("1 - Login");
                    mainPrompt.say("2 - Register an account");
                    mainPrompt.say("3 - Quit program");

                    switch(mainPrompt.ask()){
                        case "1": // login
                            loggedInUser = us.login();
                            if (loggedInUser != null) {
                                currentPage = Page.USER;
                            }
                            break;
                        case "2": // register
                            loggedInUser = us.register();
                            if (loggedInUser != null) {
                                currentPage = Page.USER;
                            }
                            break;
                        case "3":
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid input...");
                    }
                    break;
                case USER:
                    mainPrompt.label("USER DASHBOARD");
                    mainPrompt.say("Welcome, " + loggedInUser.getUsername() + "!");
                    int ticketCount = us.displayTicketInfo(loggedInUser);
                    mainPrompt.say("Your user role: " + ((loggedInUser.isAdmin()) ? "Administrator" : "Employee"));
                    mainPrompt.say("What would you like to do today?");
                    mainPrompt.say("1 - Logout");
                    mainPrompt.say("2 - Create a new Reimbursement Ticket");
                    if (ticketCount > 0) {
                        mainPrompt.say("3 - View existing ticket" + ((ticketCount > 1) ? "s (" + ticketCount + ")" : ""));
                    }
                    if (loggedInUser.isAdmin()) {
                        mainPrompt.say("4 - Admin dashboard");
                    }

                    switch(Integer.parseInt(mainPrompt.ask())){
                        case 1: // logout
                            mainPrompt.say("Logging out...");
                            currentPage = Page.HOME;
                            loggedInUser = null;
                            break;
                        case 2: // create ticket
                            ts.submitTicket(loggedInUser);
                            break;
                        case 3: // view tickets
                            ts.displayUserTickets(loggedInUser);
                            break;
                        case 4: // admin menu
                            if (!loggedInUser.isAdmin()) {
                                mainPrompt.say("Sorry, you don't have access to this menu...");
                            } else {
                                currentPage = Page.ADMIN;
                            }
                            break;
                    }

                    break;
                case ADMIN:
                    mainPrompt.label("ADMIN DASHBOARD");
                    mainPrompt.say("Select an administrative action");
                    mainPrompt.say("1 - Logout");
                    mainPrompt.say("2 - Return to User Menu");
                    mainPrompt.say("3 - View open tickets");

                    switch(Integer.parseInt(mainPrompt.ask())){
                        case 1: // logout
                            mainPrompt.say("Logging out...");
                            currentPage = Page.HOME;
                            loggedInUser = null;
                            break;
                        case 2: // Return to user menu
                            mainPrompt.say("Returning to User Menu...");
                            currentPage = Page.USER;
                            break;
                        case 3: // view all tickets

                            break;
                    }
                    break;
            }
        }
    }
}
