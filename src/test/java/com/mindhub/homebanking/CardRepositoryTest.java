package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
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
public class CardRepositoryTest {
    @Autowired
    CardRepository cardRepository;

    List<Card> cards;

    @BeforeEach
    public void getCards() {
        cards = cardRepository.findAll();
    }

    @Test
    public void isCardsEmpty() {
        assertThat(cards,is(not(empty())));
    }

    @Test
    public void cardNumberLength(){
        for(Card card : cards){
            assertTrue(card.getCvv().length() > 3);
        }
    }
}
