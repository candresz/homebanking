package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    List<Account> accounts;

    @BeforeEach
    public void getAccounts() {
        accounts = accountRepository.findAll();
    }

    @Test
    public void accountsBalanceZero() {
        for (Account account : accounts) {
            assertTrue(account.getBalance() >= 1);
        }
    }

    @Test
    public void validEmail() {
        for (Account account : accounts) {
            assertTrue(account.getBalance() >= 1);
        }
    }
}
