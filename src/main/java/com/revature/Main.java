package com.revature;

import com.revature.controller.Page;
import com.revature.model.User;
import com.revature.service.TicketService;
import com.revature.service.UserService;
import com.revature.controller.Prompt;

public class Main {

    public static void main(String[] args) {
        Prompt mainPrompt = Prompt.getPrompt();
        UserService us    = new UserService();
        TicketService ts  = new TicketService();
        User loggedInUser = null;
        Page currentPage  = Page.HOME;

        boolean running   = true;
        while(running){
            // i have the whole thing running in a while loop. this is so that we don't have to restart the program
            // every time we reach the end of a block.

            // instead we have the 'running' boolean which we can decide when to toggle.
            switch(currentPage){
                // once we know we are running, let's do different things depending on the value of 'currentPage'
                case HOME: ////////////// -- the home page -- //////////////
                    // printing out menu options using the Prompt class
                    // more on that in controller.Prompt (if i got to making comments there by the time you're reading this)
                    // for now just know that it handles the Scanner stuff, and provides wrappers around print/nextLine statements.
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
                                // only if we're sure we have a logged-in user, change the page.

                                // since we are running in a loop, the program will circle back to the top,
                                // it will first check if we are still 'running'. after it confirms that,
                                // it will check the value of page, see that it is USER, and go to the corresponding
                                // case block in the switch
                            }
                            break;
                        case "2": // register
                            loggedInUser = us.register();
                            if (loggedInUser != null) {
                                currentPage = Page.USER;
                                // only if we're sure we have successfully registered a user
                                // (and logged them in), change the page
                            }
                            break;
                        case "3": // quit
                            // here the program will close after we turn the running boolean off, because it will
                            // loop back to the begging and 'while(running)' will no longer evaluate to true
                            running = false;
                            break;
                        default: // any other input
                            mainPrompt.say("Invalid input...");
                            // notice we don't have to do anything here other than print a line
                            // since we are running a while loop, and we haven't changed the page
                            // we will simply rerun from the top, allowing the user to make another
                            // choice without having to restart the program

                            // this will happen at the end of every other block that we haven't
                            // explicitly toggled the running boolean to be false
                    }
                    break;
                case USER: ////////////// -- the user dashboard -- //////////////
                    // if (loggedInUser == null) break;
                    // ^ i was going to check here just in case we somehow got to this page without
                    // a logged-in user, but intellij told me it's fine, so ¯\_(ツ)_/¯

                    mainPrompt.label("USER DASHBOARD");
                    mainPrompt.say("Welcome, " + loggedInUser.getUsername() + "!");

                    int ticketCount = us.displayTicketInfo(loggedInUser);
                    // displayTicketInfo makes a preemptive call to the database when you load the user page.
                    // and returns that count in the form of an integer, as well as outputs relevant info
                    // to the page.

                    // this is to get an updated count of tickets a user has, so that this info can be seen
                    // at a glance upon logging in.

                    // this next line uses a ternary operator, which is essentially a shorthand if -> else statement
                    mainPrompt.say("Your user role: " + ((loggedInUser.isAdmin()) ? "Administrator" : "Employee"));
                    //                                         ^ testing this condition.  ^ if true.        ^ if false.

                    mainPrompt.say("What would you like to do today?");
                    mainPrompt.say("1 - Logout");
                    mainPrompt.say("2 - Create a new Reimbursement Ticket");
                    if (ticketCount > 0) {
                        // only if there are actual tickets, show the option for viewing the tickets
                        // that way we aren't showing the user irrelevant options
                        mainPrompt.say("3 - View existing ticket" + ((ticketCount > 1) ? "s (" + ticketCount + ")" : ""));
                        // using ternary again to add 's' and the count if there is more than one ticket.
                    }
                    if (loggedInUser.isAdmin()) {
                        // only show this option if user is an admin
                        mainPrompt.say("4 - Admin dashboard");
                    }

                    switch(mainPrompt.ask()){
                        case "1": // logout
                            mainPrompt.say("Logging out...");
                            currentPage = Page.HOME;
                            loggedInUser = null;

                            // again our while loop takes care of bringing us back to the start of the program
                            // since we set the page to HOME, and the user to null, we'll be brought right back to
                            // the home page from here.
                            break;
                        case "2": // create ticket
                            ts.submitTicket(loggedInUser);
                            break;
                        case "3": // view tickets
                            ts.displayUserTickets(loggedInUser);
                            break;
                        case "4": // admin menu
                            if (loggedInUser.isAdmin()) {
                                currentPage = Page.ADMIN;
                            } else {
                                mainPrompt.say("Sorry, you don't have access to this menu...");
                                // even though we aren't showing the menu option to non-admins, we still want to make
                                // sure that they can't access this menu by entering 4
                            }
                            break;
                    }

                    break;
                case ADMIN: ////////////// -- the admin dashboard -- //////////////
                    mainPrompt.label("ADMIN DASHBOARD");
                    mainPrompt.say("Select an administrative action");
                    mainPrompt.say("1 - Logout");
                    mainPrompt.say("2 - Return to User Menu");
                    mainPrompt.say("3 - View open tickets");

                    switch(mainPrompt.ask()){
                        case "1": // logout
                            mainPrompt.say("Logging out...");
                            currentPage  = Page.HOME;
                            loggedInUser = null;
                            break;
                        case "2": // Return to user menu
                            mainPrompt.say("Returning to User Menu...");
                            currentPage = Page.USER;
                            break;
                        case "3": // view all tickets
                            mainPrompt.say("Getting all tickets...");
                            ts.displayAllTickets();
                            break;
                    }
                    break;
            }
        }
    }
}
