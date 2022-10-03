package com.revature.dao;

import com.revature.model.ReimbursementTicket;
import com.revature.model.User;

public interface TicketDAO {

    ReimbursementTicket getTicketById(int id);

    ReimbursementTicket[] getTicketsByUserId(int userId);
    // since we have a one to many relationship between users and tickets respectively,
    // i am imagining it will be useful to query a list of reimbursement tickets that belong
    // to a specific user

    User getUserByTicketId(int id);
    // i don't think i will need something like this, cause i will likely be joining user info
    // upon a ticket request, but it might be handy.
}
