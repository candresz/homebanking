package com.mindhub.homebanking.dto;


import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {
    private Long id;
    private String name;
    private Double maxAmount;
    private List<Integer> payments;
    private Double interestRate;

    public LoanDTO(Loan loan) {
        this.id = loan.getID();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.interestRate = loan.getInterestRate();
        this.payments = loan.getPayments();
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }
}
