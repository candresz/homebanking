package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = now.format(formatter);
    LocalDateTime formattedLocalDateTime = LocalDateTime.parse(formattedDateTime, formatter);


    @GetMapping("/loans")
    public Set<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toSet());

    }

    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<String> newLoan(@RequestBody LoanApplicationDTO loanApplication, Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findById(loanApplication.getLoanId()).orElse(null);

        if (loanApplication.getToAccount().isBlank()) {
            return new ResponseEntity<>("Please Fill 'TO' account field", HttpStatus.FORBIDDEN);
        }

        if (loanApplication.getAmount() <= 0) {
            return new ResponseEntity<>("Amount must not be zero", HttpStatus.FORBIDDEN);
        }

        if (loanApplication.getPayments() <= 0) {
            return new ResponseEntity<>("Payment amount must not be zero", HttpStatus.FORBIDDEN);
        }

        // Verifico que exista el prestamo
        Long idLoan = loanApplication.getLoanId();
        if (!loanRepository.existsById(idLoan)) {
            return new ResponseEntity<>("This type of loan does not exists", HttpStatus.FORBIDDEN);
        }

        if (loanApplication.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Amount exceeds the max loan limits", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplication.getPayments())) {
            return new ResponseEntity<>("The installment pay plan is not available", HttpStatus.FORBIDDEN);
        }
        if (!accountRepository.existsByNumber(loanApplication.getToAccount())) {
            return new ResponseEntity<>("The account does not exists", HttpStatus.FORBIDDEN);
        }


        Account toAccount = accountRepository.findByNumber(loanApplication.getToAccount());
        if (!client.getAccounts().contains(toAccount)) {
            return new ResponseEntity<>("The account does not belong client", HttpStatus.FORBIDDEN);
        }

        // Agrego el 20%
        double add20 = loanApplication.getAmount() * 1.2;



        // Crear la transacción de crédito
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, loanApplication.getAmount(), formattedLocalDateTime, loan.getName() + " Loan approved");
        toAccount.addTransaction(creditTransaction);
        transactionRepository.save(creditTransaction);

        ClientLoan newLoan = new ClientLoan(add20, loanApplication.getPayments());
        client.addClientLoan(newLoan);
        loan.addClientLoan(newLoan);

        clientLoanRepository.save(newLoan);

        toAccount.setBalance(loanApplication.getAmount() + toAccount.getBalance());
        accountRepository.save(toAccount);

        return new ResponseEntity<>("Approved credit", HttpStatus.CREATED);

    }
}
