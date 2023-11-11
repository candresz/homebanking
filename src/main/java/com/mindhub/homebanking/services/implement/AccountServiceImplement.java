package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;


    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public List<AccountDTO> getAllAccountsDTO() {
        return getAllAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }


    @Override
    public Set<Account> getAllAccountsByClient(Client client) {
        return accountRepository.findByClient(client);
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }


    @Override
    public AccountDTO getAccountDTOById(Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }


    @Override
    public Set<AccountDTO> getAllAccountsDTOByClient(Client client) {
        return getAllAccountsByClient(client).stream().map(AccountDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Account findAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }


    @Override
    public boolean existsAccountByNumber(String string) {
        return accountRepository.existsByNumber(string);
    }


    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public boolean existsAccountById(Long id) {
        return accountRepository.existsById(id);
    }

    @Override
    public void deletedAccount(long id) {
        Account account = getAccountById(id);
        account.setDeleted(true);
        Set<Transaction> transactions = account.getTransactions();
        transactions
                .forEach(transaction -> {
                    transaction.setDeleted(true);
                    transactionRepository.save(transaction);
                });
        saveAccount(account);
    }
}
