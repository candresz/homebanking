package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;
import java.util.Set;

public interface AccountService {
    List<Account> getAllAccounts();
    List<AccountDTO> getAllAccountsDTO();

    Set<Account> getAllAccountsByClient(Client client);

    AccountDTO getAccountDTOById(Long id);
    Set<AccountDTO> getAllAccountsDTOByClient(Client client);

    Account findAccountByNumber(String number);

    boolean existsAccountByNumber(String string);
    void saveAccount(Account account);
}
