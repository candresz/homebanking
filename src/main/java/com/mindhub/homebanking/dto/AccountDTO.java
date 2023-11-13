package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;

    private String number;
    private LocalDate creationDate;
    private double balance;

    private AccountType accountType;
    private Set<TransactionDTO> transactions;
    public AccountDTO(Account account){
    id = account.getId();
    number = account.getNumber();
    creationDate = account.getCreationDate();
    balance = account.getBalance();
    transactions = account.getTransactions().stream().filter(transaction -> !transaction.getDeleted()).map(transaction->new TransactionDTO(transaction)).collect(Collectors.toSet());
    accountType = account.getAccountType();
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}
