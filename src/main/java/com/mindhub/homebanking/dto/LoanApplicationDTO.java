package com.mindhub.homebanking.dto;

public class LoanApplicationDTO {
    private Long loanId;
    private double amount;
    private int payments;
    private String toAccount;

    public LoanApplicationDTO(Long loanId, double amount, int payments, String toAccount) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.toAccount = toAccount;
    }

    public Long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getToAccount() {
        return toAccount;
    }
}
