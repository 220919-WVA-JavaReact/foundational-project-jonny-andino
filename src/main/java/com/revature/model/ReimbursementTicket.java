package com.revature.model;

import com.revature.util.ReimbursementType;
import com.revature.util.TicketStatus;

import java.sql.Timestamp;
import java.util.Objects;

public class ReimbursementTicket {
    private int id;
    private User user;
    private double amount;
    private String description;
    private TicketStatus status;

    private ReimbursementType type;
    private Timestamp createdTime;
    private Timestamp fulfilledTime;

    public ReimbursementTicket() {
    }

    public ReimbursementTicket(int id, User user, double amount, String description, TicketStatus status, Timestamp createdTime, Timestamp fulfilledTime) {
        this.id = id;
        this.user = user;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.createdTime = createdTime;
        this.fulfilledTime = fulfilledTime;
    }

    public ReimbursementTicket(int id, User user, double amount, String description, TicketStatus status) {
        this.id = id;
        this.user = user;
        this.amount = amount;
        this.description = description;
        this.status = status;
    }

    public ReimbursementTicket(User user, double amount, String description, TicketStatus status, Timestamp createdTime, Timestamp fulfilledTime) {
        this.user = user;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.createdTime = createdTime;
        this.fulfilledTime = fulfilledTime;
    }

    public ReimbursementTicket(User user, double amount, String description) {
        this.user = user;
        this.amount = amount;
        this.description = description;
    }

    public ReimbursementTicket(User user, double amount, String description, ReimbursementType type) {
        this.user = user;
        this.amount = amount;
        this.description = description;
        this.type = type;
    }

    public ReimbursementTicket(int id, User user, double amount, String description, TicketStatus status, ReimbursementType type, Timestamp createdTime, Timestamp fulfilledTime) {
        this.id = id;
        this.user = user;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.type = type;
        this.createdTime = createdTime;
        this.fulfilledTime = fulfilledTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getFulfilledTime() {
        return fulfilledTime;
    }

    public void setFulfilledTime(Timestamp fulfilledTime) {
        this.fulfilledTime = fulfilledTime;
    }

    public ReimbursementType getType() {
        return type;
    }

    public void setType(ReimbursementType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ReimbursementTicket{" +
                "id=" + id +
                ", user=" + user +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", createdTime=" + createdTime +
                ", fulfilledTime=" + fulfilledTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReimbursementTicket that = (ReimbursementTicket) o;
        return id == that.id && Double.compare(that.amount, amount) == 0 && user.equals(that.user) && description.equals(that.description) && status == that.status && type == that.type && createdTime.equals(that.createdTime) && fulfilledTime.equals(that.fulfilledTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, amount, description, status, type, createdTime, fulfilledTime);
    }
}
