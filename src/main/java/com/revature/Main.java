package com.revature;

import com.revature.controller.CLIPage;
import com.revature.model.User;
import com.revature.service.TicketServiceCLI;
import com.revature.service.UserServiceCLI;
import com.revature.controller.Prompt;

public class Main {

    public static void main(String[] args) {
        Prompt mainPrompt = Prompt.getPrompt();
        UserServiceCLI us    = new UserServiceCLI();
        TicketServiceCLI ts  = new TicketServiceCLI();
        User loggedInUser = null;
        CLIPage currentPage  = CLIPage.HOME;

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
                            loggedInUser = us.loginCLI();
                            if (loggedInUser != null) {

                                currentPage = (loggedInUser.isAdmin()) ? CLIPage.ADMIN : CLIPage.USER;
                                // only if we're sure we have a logged-in user, change the page.

                                // since we are running in a loop, the program will circle back to the top,
                                // it will first check if we are still 'running'. after it confirms that,
                                // it will check the value of page, see that it is USER, and go to the corresponding
                                // case block in the switch
                            }
                            break;
                        case "2": // register
                            loggedInUser = us.registerCLI();
                            if (loggedInUser != null) {
                                currentPage = CLIPage.USER;
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

                    int ticketCount = us.displayTicketInfoCLI(loggedInUser);
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
                            currentPage = CLIPage.HOME;
                            loggedInUser = null;

                            // again our while loop takes care of bringing us back to the start of the program
                            // since we set the page to HOME, and the user to null, we'll be brought right back to
                            // the home page from here.
                            break;
                        case "2": // create ticket
                            ts.submitTicketCLI(loggedInUser);
                            break;
                        case "3": // view tickets
                            ts.displayUserTicketsCLI(loggedInUser);
                            break;
                        case "4": // admin menu
                            if (loggedInUser.isAdmin()) {
                                currentPage = CLIPage.ADMIN;
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
                    mainPrompt.say("2 - User options");
                    mainPrompt.say("3 - View pending tickets");
                    mainPrompt.say("4 - View all tickets");
                    mainPrompt.say("5 - Respond to a Ticket (must provide ticket ID)");

                    switch(mainPrompt.ask()){
                        case "1": // logout
                            mainPrompt.say("Logging out...");
                            currentPage  = CLIPage.HOME;
                            loggedInUser = null;
                            break;
                        case "2": // Return to user menu
                            mainPrompt.say("Returning to User Menu...");
                            currentPage = CLIPage.USER;
                            break;
                        case "3": // view all tickets
                            mainPrompt.say("Getting all open tickets...");
                            ts.displayAllPendingTicketsCLI();
                            break;
                        case "4":
                            mainPrompt.say("Getting all tickets");
                            ts.displayAllTicketsCLI();
                            break;
                        case "5":
                            int tid = Integer.parseInt(mainPrompt.ask("Please provide the numeric ID for the ticket you wish to respond to."));
                            ts.reviewTicketCLI(tid);
                            break;
                    }
                    break;
            }
        }
    }
}
