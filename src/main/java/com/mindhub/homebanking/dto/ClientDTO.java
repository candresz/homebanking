package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private long ID;

    private String firstName, lastName, email;
    private Set<AccountDTO> accounts;
    private Set<ClientLoanDTO> loans;
    private Set<CardDTO> cards;


    public ClientDTO(Client client) {
        ID = client.getId();
        firstName = client.getFirstName();
        lastName = client.getLastName();
        email = client.getEmail();
        accounts = client.getAccounts().stream().filter(account -> !account.getDeleted()).map(account -> new AccountDTO(account)).collect(Collectors.toSet());
        loans = client.getClientLoans().stream().filter(clientLoan -> !clientLoan.isPaid()).map(loan-> new ClientLoanDTO(loan)).collect(Collectors.toSet());
        cards = client.getCards().stream()
                .filter(card -> !card.getDeleted())
                .map(CardDTO::new)
                .collect(Collectors.toSet());

    }

    public long getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }
    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }
}
