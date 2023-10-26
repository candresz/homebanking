package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByNumber(String number);
    boolean existsByCvv(String number);


}
