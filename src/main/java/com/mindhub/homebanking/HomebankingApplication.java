package com.mindhub.homebanking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(
            ClientRepository clientRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            LoanRepository loanRepository,
            ClientLoanRepository clientLoanRepository) {

        return args -> {
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            LocalDateTime formattedLocalDateTime = LocalDateTime.parse(formattedDateTime, formatter);

            // Crear clientes
            Client melba = new Client("Melba", "Morel", "melbamorel@homebanking.com");
            clientRepository.save(melba);
            Client joe = new Client("Joe", "Biden", "jbiden@homebanking.com");
            clientRepository.save(joe);

            // Crear cuentas
            Account accountOne = new Account("VIN001", LocalDate.now(), 5000.00);
            melba.addAccount(accountOne);
            accountRepository.save(accountOne);

            Account accountTwo = new Account("VIN002", tomorrow, 7500.00);
            melba.addAccount(accountTwo);
            accountRepository.save(accountTwo);

            Account accountSecondUserOne = new Account("VIN003", LocalDate.now(), 29000);
            joe.addAccount(accountSecondUserOne);
            accountRepository.save(accountSecondUserOne);

            // Crear transacciones
            Transaction transactionOne = new Transaction(DEBIT, 1000.00, formattedLocalDateTime, "First transaction");
            accountOne.addTransaction(transactionOne);
            transactionRepository.save(transactionOne);

            Transaction transactionTwo = new Transaction(CREDIT, 10000.00, formattedLocalDateTime, "Second transaction");
            accountOne.addTransaction(transactionTwo);
            transactionRepository.save(transactionTwo);

            Transaction transactionAccountTwoOne = new Transaction(DEBIT, 14500.00, formattedLocalDateTime, "First transaction");
            accountTwo.addTransaction(transactionAccountTwoOne);
            transactionRepository.save(transactionAccountTwoOne);

            Transaction transactionAccountTwoThree = new Transaction(CREDIT, 11020.00, formattedLocalDateTime, "Second transaction");
            accountTwo.addTransaction(transactionAccountTwoThree);
            transactionRepository.save(transactionAccountTwoThree);

            Transaction transactionSecondUser = new Transaction(CREDIT, 145100.00, formattedLocalDateTime, "First transaction");
            accountSecondUserOne.addTransaction(transactionSecondUser);
            transactionRepository.save(transactionSecondUser);

            // Crear tipos de loans
            Loan mortgage = new Loan("Mortgage", 500000.00, List.of(12, 24, 36, 48, 60));
            loanRepository.save(mortgage);

            Loan personal = new Loan("Personal", 100000.00, List.of(6, 12, 24));
            loanRepository.save(personal);

            Loan car = new Loan("Car", 300000.00, List.of(6, 12, 24, 36));
            loanRepository.save(car);

            // Crear ClientLoan
            ClientLoan mortgageMelba = new ClientLoan( 400000.00, 60);
            melba.addClientLoan(mortgageMelba);
            mortgage.addClientLoan(mortgageMelba);
            clientLoanRepository.save(mortgageMelba);

            ClientLoan personalMelba = new ClientLoan( 50000.00, 12);
            melba.addClientLoan(personalMelba);
            personal.addClientLoan(personalMelba);
            clientLoanRepository.save(personalMelba);


            ClientLoan personalJoe = new ClientLoan(100000.00, 24);
            joe.addClientLoan(personalJoe);
            personal.addClientLoan(personalJoe);
            clientLoanRepository.save(personalJoe);

            ClientLoan carJoe = new ClientLoan(200000.00, 36);
            joe.addClientLoan(carJoe);
            car.addClientLoan(carJoe);
            clientLoanRepository.save(carJoe);
        };
    }
}

