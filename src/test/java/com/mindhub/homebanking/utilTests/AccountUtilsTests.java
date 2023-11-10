package com.mindhub.homebanking.utilTests;


import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AccountUtilsTests {
    @Test
    public void generateAccountNumber() {
        String accountNumber = CardUtils.generateRandomCardNumber();
        assertThat(accountNumber, is(not(emptyOrNullString())));
    }

    @Test public void cardNumberLength(){
        String cardNumber = CardUtils.generateRandomCardNumber();
        assertTrue(cardNumber.length() < 20);
    }
}
