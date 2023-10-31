package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = now.format(formatter);
    LocalDateTime formattedLocalDateTime = LocalDateTime.parse(formattedDateTime, formatter);
    @Transactional
    @PostMapping("/clients/current/transaction")
    public ResponseEntity<Object> newTransaction(@RequestParam Double amount, @RequestParam String description, @RequestParam String fromAccount, @RequestParam String toAccount, Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName()); // Cliente autenticado

        if (amount <= 0) {
            return new ResponseEntity<>("The amount must not be zero", HttpStatus.FORBIDDEN);
        }
        if (description.isEmpty()) {
            return new ResponseEntity<>("Fill description field", HttpStatus.FORBIDDEN);
        }
        if (fromAccount.isEmpty()) {
            return new ResponseEntity<>("Fill 'FROM' account field", HttpStatus.FORBIDDEN);
        }
        if (toAccount.isEmpty()) {
            return new ResponseEntity<>("Fill 'TO' account field", HttpStatus.FORBIDDEN);
        }

        if (fromAccount.equals(toAccount)) {
            return new ResponseEntity<>("Same accounts numbers", HttpStatus.FORBIDDEN);
        }

        Account sAccount = accountRepository.findByNumber(fromAccount); // Cuenta origen
        if (sAccount == null) {
            return new ResponseEntity<>("Sender account does not exists", HttpStatus.FORBIDDEN);
        }


        if (!sAccount.getClient().equals(client)) {
            return new ResponseEntity<>("Account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }

        Account rAccount = accountRepository.findByNumber(toAccount); // Cuenta destino
        if (rAccount == null) {
            return new ResponseEntity<>("Recipient account does not exists", HttpStatus.FORBIDDEN);
        }

        if (sAccount.getBalance() <= amount) {
            return new ResponseEntity<>("Your balance is insufficient", HttpStatus.FORBIDDEN);
        }

        // Crear la transacción de débito
        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, amount, formattedLocalDateTime, description);
        sAccount.addTransaction(debitTransaction);

        sAccount.setBalance(sAccount.getBalance() - amount);
        transactionRepository.save(debitTransaction);

        // Crear la transacción de crédito
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, formattedLocalDateTime, description);
        rAccount.addTransaction(creditTransaction);


        rAccount.setBalance(rAccount.getBalance() + amount);
        transactionRepository.save(creditTransaction);


        return new ResponseEntity<>("Transfer successfully", HttpStatus.CREATED);
    }
}
