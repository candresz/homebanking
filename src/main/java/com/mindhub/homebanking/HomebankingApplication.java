package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {

        return args -> {
            LocalDate today = LocalDate.now(); // LocalDate es un objeto
            LocalDate tomorrow = today.plusDays(1);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            LocalDateTime formattedLocalDateTime = LocalDateTime.parse(formattedDateTime, formatter);

            // ---- Account One
            Client firstUser = new Client("Melba", "Morel", "melbamorel@homebanking.com");
            clientRepository.save(firstUser);

            Account accountOne = new Account("VIN001", LocalDate.now(), 5000.00);
            firstUser.addAccount(accountOne);
            accountRepository.save(accountOne);
            // --- Transactions accountOne
            Transaction transactionOne = new Transaction(DEBIT, 1000.00, formattedLocalDateTime, "First transaction");
            accountOne.addTransaction(transactionOne);
            transactionRepository.save(transactionOne);
            Transaction transactionTwo = new Transaction(CREDIT, 10000.000, formattedLocalDateTime, "Second transaction");
            accountOne.addTransaction(transactionTwo);
            transactionRepository.save(transactionTwo);

            // ---- Account Two
            Account accountTwo = new Account("VIN002", tomorrow, 7500.00);
            firstUser.addAccount(accountTwo);
            accountRepository.save(accountTwo);
            // Transactions accountTwo
            Transaction transactionAccountTwoOne = new Transaction(DEBIT, 14500.00, formattedLocalDateTime, "First transaction");
            accountTwo.addTransaction(transactionAccountTwoOne);
            transactionRepository.save((transactionAccountTwoOne));
            Transaction transactionAccountTwoThree = new Transaction(CREDIT, 11020.00, formattedLocalDateTime, "Second transaction");
            accountTwo.addTransaction(transactionAccountTwoThree);
            transactionRepository.save((transactionAccountTwoThree));

    // ---- Second User
            Client secondUser = new Client("Steph", "Durocher", "steph@homebanking.com");
            clientRepository.save(secondUser);
            Account accountSecondUserOne = new Account("VIN003", LocalDate.now(), 29000);
            secondUser.addAccount(accountSecondUserOne);
            accountRepository.save(accountSecondUserOne);
            Transaction transactionSecondUser = new Transaction(CREDIT, 145100.00, formattedLocalDateTime, "First transaction");
            accountSecondUserOne.addTransaction(transactionSecondUser);
            transactionRepository.save(transactionSecondUser);
        };
    }
}
