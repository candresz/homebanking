package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping
    public List<AccountDTO> getAllAccounts(){ // Esto solo es un metodo!
        List<Account> accounts = accountRepository.findAll(); //Le pido al JPARepository el listado
        Stream<Account> accountStream = accounts.stream();
        Stream<AccountDTO> accountDTOStream = accountStream.map(AccountDTO::new);
        return accountDTOStream.collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AccountDTO getAccountById(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(AccountDTO::new) // Convierte la cuenta a un DTO
                .orElse(null); // Si no se encuentra, retorna null
    }
}
