package com.revature.model;


import com.revature.util.TicketStatus;

import java.sql.Timestamp;
import java.util.Objects;

public class ReimbursementTicket {
    private int id;
    private int userId;
    private double amount;
    private String description;
    private TicketStatus status;
    private Timestamp createdTime;
    private Timestamp fulfilledTime;

    public ReimbursementTicket(int id, int userId, double amount, String description, TicketStatus status, Timestamp createdTime, Timestamp fulfilledTime) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.createdTime = createdTime;
        this.fulfilledTime = fulfilledTime;
    }

    public ReimbursementTicket(int id, int userId, double amount, String description, TicketStatus status) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.status = status;
    }

    public ReimbursementTicket() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "ReimbursementTicket{" +
                "id=" + id +
                ", userId=" + userId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", fulfilledTime=" + fulfilledTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReimbursementTicket that = (ReimbursementTicket) o;
        return id == that.id && userId == that.userId && Double.compare(that.amount, amount) == 0 && description.equals(that.description) && status == that.status && createdTime.equals(that.createdTime) && fulfilledTime.equals(that.fulfilledTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, amount, description, status, createdTime, fulfilledTime);
    }
}
