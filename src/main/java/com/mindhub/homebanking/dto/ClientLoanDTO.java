package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.ClientLoan;


public class ClientLoanDTO {

    private Long ID;
    private Long loanId;
    private String loanName;
    private double amount;

    private int payments;


    public ClientLoanDTO(ClientLoan clientLoan) {
        ID = clientLoan.getId();
        loanId = clientLoan.getLoan().getID();
        loanName = clientLoan.getLoan().getName();
        amount = clientLoan.getAmount();
        payments = clientLoan.getPayments();

    }

    public Long getID() {
        return ID;
    }

    public Long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }


}
