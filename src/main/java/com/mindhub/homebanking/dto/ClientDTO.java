package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private long ID;

    private String firstName, lastName, email;
    private Set<AccountDTO> accounts;
    public ClientDTO(Client client){
    ID = client.getID();
    firstName = client.getFirstName();
    lastName = client.getLastName();
    email = client.getEmail();
    accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
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
}
