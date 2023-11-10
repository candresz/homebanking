package com.mindhub.homebanking.utilTests;


import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CardUtilsTests {
    @Test
    public void cardNumberIsCreated() {
        String cardNumber = CardUtils.generateRandomCardNumber();
        assertThat(cardNumber, is(not(emptyOrNullString())));
    }

    @Test public void cardNumberLength(){
        String cardNumber = CardUtils.generateRandomCardNumber();
        assertTrue(cardNumber.length() < 20);
    }

    @Test
    public void cvvNumber() {
        String cvvNumber = CardUtils.generateRandomCvvNumber();
        assertThat(cvvNumber, is(not(emptyOrNullString())));
    }
    @Test public void cvvNumberLength(){
        String cvvNumber = CardUtils.generateRandomCardNumber();
        assertTrue(cvvNumber.length() < 4);
    }
}
