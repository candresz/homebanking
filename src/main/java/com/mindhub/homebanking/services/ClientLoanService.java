package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface ClientLoanService {

    void saveClientLoan(ClientLoan clientLoan);

    List<ClientLoan> getAllClientLoans(Client client);

    ClientLoan getClientLoan(Client client, Loan loan);
    boolean existsById(Long id);
    ClientLoan getClientLoanById(Long id);
    void paidLoan(long id);

}