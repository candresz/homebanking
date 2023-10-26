package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController // Controlador bajo los parametros de REST(HTTP)/ le digo que esta clase va hacer el controlador.
@RequestMapping("/api/clients") // Asocio las peticiones a esta ruta(base).
public class ClientController {
    @Autowired //Injeccion de dependencias, le pedimos a Spring Boot que introduzca ClientRepository en esta clase.
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @GetMapping // Servlest es todo junto.
    public List<ClientDTO> getAllClients() { // Esto solo es un metodo!
        List<Client> clients = clientRepository.findAll(); //Le pido al JPARepository el listado
        Stream<Client> clientStream = clients.stream();
        Stream<ClientDTO> clientDTOStream = clientStream.map(ClientDTO::new);
        return clientDTOStream.collect(Collectors.toList());
    }

    @GetMapping("/{id}") // Endpoints
    public ClientDTO getClientById(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(ClientDTO::new) // Convierte el cliente a un DTO
                .orElse(null); // Si no se encuentra, retorna null
    }

    @PostMapping
    public ResponseEntity<Object> newClient(
            @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        // Verificar si la cuenta ya existe en accountRepository
        int accountNumber;
        String accountNumberString;

        do {
            accountNumber = getRandomNumber(0, 99999999);
            accountNumberString = String.valueOf(accountNumber);
        } while (accountRepository.existsByNumber(accountNumberString));


        if (accountRepository.existsByNumber(accountNumberString) ) {
            return new ResponseEntity<>("Account already in use", HttpStatus.FORBIDDEN);
        }


        // Crear un nuevo cliente y asociar la cuenta
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password), false);
        Account account = new Account(accountNumberString, LocalDate.now(), 0);
        client.addAccount(account);
        clientRepository.save(client);

        accountRepository.save(account);

        return new ResponseEntity<>("Client created successfully", HttpStatus.CREATED);
    }

    @RequestMapping("/current")
    public ClientDTO getClientCurrent(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
}
