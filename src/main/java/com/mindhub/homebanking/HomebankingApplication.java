package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {

        return args -> {
            LocalDate today =  LocalDate.now(); // LocalDate es un objeto
            LocalDate tomorrow = today.plusDays(1);
            Client firstUser = new Client("Melba", "Morel", "melbamorel@homebanking.com");
            clientRepository.save(firstUser);

            Account accountOne = new Account("VIN001", LocalDate.now(), 5000);
            firstUser.addAccount(accountOne);
            accountRepository.save(accountOne);
            Account accountTwo = new Account("VIN002", tomorrow, 7500);
            firstUser.addAccount(accountTwo);
            accountRepository.save(accountTwo);

            Client secondUser = new Client("Steph", "Durocher", "steph@homebanking.com");
            clientRepository.save(secondUser);

        };
    }
}
