package com.mindhub.homebanking.repositoryTests;

import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LoanRepositoryTest {

    @Autowired
    LoanRepository loanRepository;

    List<Loan> loans;

    @BeforeEach
    public void getLoans() {
        loans = loanRepository.findAll();
    }

    @Test
    public void isEmpty() {
        assertThat(loans, is(not(empty())));
    }

    @Test
    public void maxAmount() {
        for (Loan loan : loans) {
            assertTrue(loan.getMaxAmount() > 500000);
        }
    }
}
