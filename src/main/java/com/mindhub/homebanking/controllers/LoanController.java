package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import com.mindhub.homebanking.services.implement.ClientServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.TransactionUtils.dateTime;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/loans")
    public Set<LoanDTO> getLoans() {
        return loanService.getAllLoansDTO();

    }

    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<String> newLoan(@RequestBody LoanApplicationDTO loanApplication, Authentication authentication) {

        Client client = clientService.findClientByEmail(authentication.getName());
        Loan loan = loanService.getLoanById(loanApplication.getLoanId());

        if (loanApplication.getToAccount().isBlank()) {
            return new ResponseEntity<>("Please Fill 'TO' account field", HttpStatus.FORBIDDEN);
        }

        if (loanApplication.getAmount() <= 0) {
            return new ResponseEntity<>("Amount must not be zero", HttpStatus.FORBIDDEN);
        }

        if (loanApplication.getPayments() <= 0) {
            return new ResponseEntity<>("Payment amount must not be zero or negative", HttpStatus.FORBIDDEN);
        }

        // Verifico que exista el prestamo
        Long idLoan = loanApplication.getLoanId();
        if (!loanService.existsLoanById(idLoan)) {
            return new ResponseEntity<>("This type of loan does not exists", HttpStatus.FORBIDDEN);
        }

        if (loanApplication.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Amount exceeds the max loan limits", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplication.getPayments())) {
            return new ResponseEntity<>("The installment pay plan is not available", HttpStatus.FORBIDDEN);
        }
        if (!accountService.existsAccountByNumber(loanApplication.getToAccount())) {
            return new ResponseEntity<>("The account does not exists", HttpStatus.FORBIDDEN);
        }


        Account toAccount = accountService.findAccountByNumber(loanApplication.getToAccount());
        if (!client.getAccounts().contains(toAccount)) {
            return new ResponseEntity<>("The account does not belong client", HttpStatus.FORBIDDEN);
        }

        // Agrego el 20%
        double add20 = loanApplication.getAmount() * (loan.getInterestRate() / 12);


        // Crear la transacción de crédito
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, loanApplication.getAmount(), toAccount.getBalance() + loanApplication.getAmount(), dateTime(), loan.getName() + " Loan approved");
        toAccount.addTransaction(creditTransaction);
        transactionService.saveTransaction(creditTransaction);

        ClientLoan newLoan = new ClientLoan(add20, loanApplication.getPayments(), false);
        client.addClientLoan(newLoan);
        loan.addClientLoan(newLoan);

        clientLoanService.saveClientLoan(newLoan);

        toAccount.setBalance(loanApplication.getAmount() + toAccount.getBalance());
        accountService.saveAccount(toAccount);

        return new ResponseEntity<>("Approved credit", HttpStatus.CREATED);

    }

    @PostMapping("/loans/create")
    @Transactional
    public ResponseEntity<String> newAdminLoan(@RequestParam String loanType, @RequestParam List<Integer> payments, @RequestParam double maxAmount, @RequestParam double interestRate, Authentication authentication) {
        Client client = clientService.findClientByEmail(authentication.getName());


        if (loanType.isBlank()) {
            return new ResponseEntity<>("Please write the loan type", HttpStatus.FORBIDDEN);
        }
        if (payments.isEmpty()) {
            return new ResponseEntity<>("Payments must be higher than 0", HttpStatus.FORBIDDEN);
        }
        if (maxAmount <= 0) {
            return new ResponseEntity<>("Max Amount must be higher than 0", HttpStatus.FORBIDDEN);
        }
        if (interestRate <= 0) {
            return new ResponseEntity<>("Interest Rate must be higher than 0", HttpStatus.FORBIDDEN);
        }

        Loan newLoan = new Loan(loanType, maxAmount, interestRate, payments);
        loanService.saveLoan(newLoan);
        return new ResponseEntity<>("New Loan Created", HttpStatus.CREATED);
    }

    @PostMapping("/loans/payments")
    @Transactional
    public ResponseEntity<String> payLoan(@RequestParam long clientLoanId, long accountId, double amount, int payments, String description, Authentication authentication) {
        ClientLoan clientLoan = clientLoanService.getClientLoanById(clientLoanId);
        Account account = accountService.getAccountById(accountId);


        if (!clientLoanService.existsById(clientLoanId)) {
            return new ResponseEntity<>("Loan does not exists", HttpStatus.FORBIDDEN);
        }
        if (!accountService.existsAccountById(accountId)) {
            return new ResponseEntity<>("Account does not exists", HttpStatus.FORBIDDEN);
        }
        if (accountService.getBalanceByAccountId(accountId) < amount) {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }
        if (clientLoan.getPayments() < payments) {
            return new ResponseEntity<>("Monthly payment not valid", HttpStatus.FORBIDDEN);
        }
        if(amount == clientLoan.getAmount()){
            clientLoanService.paidLoan(clientLoanId);
        }

        // Disminuir saldo de la cuenta
        double newAccountBalance = account.getBalance() - amount;
        account.setBalance(newAccountBalance);

        // Disminuir saldo pendiente del préstamo
        double newLoanBalance = clientLoan.getAmount() - amount;
        clientLoan.setAmount(newLoanBalance);

        int newPayments = clientLoan.getPayments() - payments;
        clientLoan.setPayments(newPayments);


        Transaction transaction = new Transaction(TransactionType.valueOf("DEBIT"), amount, account.getBalance()-amount, dateTime(), description);


        // Guardar los cambios
        accountService.saveAccount(account);
        clientLoanService.saveClientLoan(clientLoan);
        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);


        return new ResponseEntity<>("Paid successfully", HttpStatus.OK);
    }

}
