package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {
    private Long ID;
    private TransactionType type;
    private double amount;
    private LocalDateTime date;
    private String description;

    public TransactionDTO(Transaction transaction){
    ID = transaction.getID();
    type = transaction.getType();
    amount = transaction.getAmount();
    date = transaction.getDate();
    description = transaction.getDescription();
    }

    public Long getID() {
        return ID;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
