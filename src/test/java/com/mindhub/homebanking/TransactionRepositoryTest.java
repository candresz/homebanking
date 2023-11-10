package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {
    @Autowired
    TransactionRepository transactionRepository;

    List<Transaction> transactions;
    @BeforeEach
    public void getTransactions(){
       transactions = transactionRepository.findAll();
    }

    @Test
    public void isEmpty(){
        assertThat(transactions, is(not(empty())));
    }

    @Test
    public void validTransaction(){
        assertThat(transactions, everyItem(hasProperty("description", is(not(empty())))));
    }

}
