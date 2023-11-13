package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

import java.util.Set;

public interface CardService {

    Card getCardById(Long id);
    boolean existsCardByNumber(String number);
    boolean existsCardById(Long id);

    void deletedCard(Long id);
    void saveCard(Card card);
    int countByClientAndIsDeleted (Client client);
    Set<Card> findByClientAndIsDeletedFalse(Client client);
}
