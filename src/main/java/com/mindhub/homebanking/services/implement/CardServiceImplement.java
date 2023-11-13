package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CardServiceImplement implements CardService{
    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card getCardById(Long id){
      return  cardRepository.findById(id).orElse(null);

    }
    @Override
    public boolean existsCardByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    @Override
    public boolean existsCardById(Long id) {
        return cardRepository.existsById(id);
    }

    @Override
    public void deletedCard(Long id) {
        Card card = getCardById(id);
        card.setDeleted(true);
        saveCard(card);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public int countByClientAndIsDeleted(Client client) {
        return cardRepository.countByClientAndIsDeleted(client, false);
    }

    @Override
    public Set<Card> findByClientAndIsDeletedFalse(Client client) {
        return cardRepository.findByClientAndIsDeletedFalse(client);
    }
}
