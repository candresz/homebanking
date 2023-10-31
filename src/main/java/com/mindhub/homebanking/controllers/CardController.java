package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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


@RestController
@RequestMapping("/api/clients")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;


    // Metodo para generar un número de tarjeta de crédito aleatorio
    private String generateRandomCardNumber() {

        // String holaMundo = "Hola";
        //    holaMundo+= "Mundo";
        StringBuilder cardNumber; // String Builder(Clase de Java) me permite modificar el contenido de mi cadena sin necesitar de instanciar.


        do {
           cardNumber = new StringBuilder(); // Se crea un objeto nuevo cada vez que se completa un numero de tarjeta.
            for (int i = 0; i < 16; i++) { // 16 iteraciones
                int digit = (int) (Math.random() * 10); // Genera un número de 0 a 9 y lo guardamos en digit
                cardNumber.append(digit); // El número generado lo guardamos con .append gracias a StringBuilder

                if ((i + 1) % 4 == 0 && i != 15) {
                    cardNumber.append("-"); // Agregar un guión después de cada 4 iteraciones.
                }
            }
        } while (cardRepository.existsByNumber(cardNumber.toString()));

        return cardNumber.toString();
    }


    // Metodo para generar un número de tarjeta de crédito aleatorio
    private String generateRandomCvvNumber() {
        StringBuilder cardCvvNumber;

        do {
           cardCvvNumber = new StringBuilder();
            for (int i = 0; i < 4; i++) { // 16 iteraciones
                int digit = (int) (Math.random() * 4); // Genera un número de 0 a 9 y lo guardamos en digit
                cardCvvNumber.append(digit); // El número generado lo guardamos con .append gracias a StringBuilder

            }
        } while (cardRepository.existsByNumber(cardCvvNumber.toString()));

        return cardCvvNumber.toString();
    }

    @PostMapping("/current/cards")
    public ResponseEntity<Object> newCard(@RequestParam String cardType, @RequestParam String cardColor,
                                          Authentication authentication) {

        if (cardColor.isEmpty()) {
            return new ResponseEntity<>("You must choose a card type.", HttpStatus.FORBIDDEN);
        }
        if (cardType.isEmpty()) {
            return new ResponseEntity<>("You must choose a card color.", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.findByEmail(authentication.getName());

        int numberOfCardType =
                (int) client.getCards().stream().filter(card -> card.getCardType().equals(CardType.valueOf(cardType))).count();

        if (numberOfCardType == 3) {
            return new ResponseEntity<>("You cannot have more than three cards of the same type.", HttpStatus.FORBIDDEN);
        }

        Card card = new Card(client.fullName(), CardType.valueOf(cardType), CardColor.valueOf(cardColor), generateRandomCardNumber(), generateRandomCvvNumber(), LocalDate.now().plusYears(5), LocalDate.now());
        client.addCard(card);
        cardRepository.save(card);
        clientRepository.save(client);

        return new ResponseEntity<>("Card created successfully",HttpStatus.CREATED);
    }
}

