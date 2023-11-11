package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private long id;
    private String cardHolder;
    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private String cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;

    public CardDTO(Card card) {
        id = card.getId();
        cardHolder = card.getCardHolder();
        cardType = card.getCardType();
        cardColor = card.getCardColor();
        number = card.getNumber();
        cvv = card.getCvv();
        thruDate = card.getThruDate();
        fromDate = card.getFromDate();
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardType getCardType() {
        return cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }
}
