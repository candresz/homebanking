package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

public interface CardService {

    Card getCardById(Long id);
    boolean existsCardByNumber(String number);
    boolean existsCardById(Long id);

    void deletedCard(Long id);
    void saveCard(Card card);
}
