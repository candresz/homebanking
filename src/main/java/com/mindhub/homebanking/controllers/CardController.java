package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.implement.ClientServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.CardUtils.generateRandomCardNumber;
import static com.mindhub.homebanking.utils.CardUtils.generateRandomCvvNumber;


@RestController
@RequestMapping("/api/clients")
public class CardController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;


    // Metodo para generar un número de tarjeta de crédito aleatorio


    @PostMapping("/current/cards")
    public ResponseEntity<String> newCard(@RequestParam String cardType, @RequestParam String cardColor,
                                          Authentication authentication) {

        if (cardColor.isEmpty()) {
            return new ResponseEntity<>("You must choose a card type.", HttpStatus.FORBIDDEN);
        }
        if (cardType.isEmpty()) {
            return new ResponseEntity<>("You must choose a card color.", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findClientByEmail(authentication.getName());

        int numberOfCardType =
                (int) client.getCards().stream().filter(card -> card.getCardType().equals(CardType.valueOf(cardType))).count();

        if (numberOfCardType == 3) {
            return new ResponseEntity<>("You cannot have more than three cards of the same type.", HttpStatus.FORBIDDEN);
        }

        String cardNumber;
        do {
            cardNumber = generateRandomCardNumber();
        } while (cardService.existsCardByNumber(cardNumber));


        Card card = new Card(client.fullName(), CardType.valueOf(cardType), CardColor.valueOf(cardColor), cardNumber, generateRandomCvvNumber(), LocalDate.now().plusYears(5), LocalDate.now());
        client.addCard(card);
        cardService.saveCard(card);
        clientService.saveClient(client);

        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
    }
}

