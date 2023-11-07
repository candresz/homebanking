package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

public interface CardService {
    boolean existsCardByNumber(String number);

    void saveCard(Card card);
}
