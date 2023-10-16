package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long ID;

    private String cardHolder;
    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private String cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;

    public Card() {
    }


    public Card(String cardHolder, CardType cardType, CardColor cardColor, String number, String cvv, LocalDate thruDate, LocalDate fromDate) {
        this.cardHolder = cardHolder;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
    }

    public Long getId() {
        return ID;
    }

    @ManyToOne
    @JoinColumn(name = "client_id")
    public Client client;

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getMaskedCard() {
        int length = number.length();
        String lastFourDigits = number.substring(Math.max(0, length - 3)); // Extrae los últimos 3 dígitos, independientemente de los espacios.
        String masked = "****-****-****-" + lastFourDigits;
        return masked;
    }
}
