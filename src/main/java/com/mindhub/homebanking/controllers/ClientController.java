package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.implement.ClientServiceImplement;
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

@RestController // Clase como controlador bajo los parametros de REST(HTTP)/ le digo que esta clase va hacer el controlador.
@RequestMapping("/api/clients") // Asocio las peticiones a esta ruta(base).
public class ClientController {
    @Autowired //Injeccion de dependencias
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @GetMapping // Serverless es todo junto, metodo y EndPoint.
    public List<ClientDTO> getAllClients() { // Esto solo es un metodo devuelve una lista de ClientDTO
        return clientService.getAllClientsDTO();
    }

    @GetMapping("/{id}") // Endpoint.
    public ClientDTO getClientById(@PathVariable Long id) { //PathVariable toma el valor de la URL(id)
        return clientService.findClientDTOById(id);
    }

    @PostMapping // Solicitud a ruta principal /api/clients
    public ResponseEntity<String> newClient(
            @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password) {

        if (firstName.isBlank()) {
            return new ResponseEntity<>("Type your first name", HttpStatus.FORBIDDEN);
        }
        if (lastName.isBlank()) {
            return new ResponseEntity<>("Type your last name", HttpStatus.FORBIDDEN);
        }
        if (email.isBlank()) {
            return new ResponseEntity<>("Type your email", HttpStatus.FORBIDDEN);
        }
        if (password.isBlank()) {
            return new ResponseEntity<>("Type your password", HttpStatus.FORBIDDEN);
        }

        if (clientService.findClientByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        // Verificar si la cuenta ya existe en accountRepository
        int accountNumber; // Guardamos el numero (int)
        String accountNumberString; // Convertimos el numero a String
        // Se ejecuta el do sin verificar la condicion la primera vez, y se guarda el numero
        // Luego si revisa la condicion while
        do {
            accountNumber = getRandomNumber(0, 99999999); // Hacemos uso del metodo getRandomNumber previamente escrito.
            accountNumberString = String.valueOf(accountNumber); // tipos primitivos no tienen toString().
//          String vin = "VIN-" + accountNumber;
        } while (accountService.existsAccountByNumber(accountNumberString));


        if (accountService.existsAccountByNumber(accountNumberString) ) {
            return new ResponseEntity<>("Account already in use", HttpStatus.FORBIDDEN);
        }


        // Crear un nuevo cliente y asociar la cuenta
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password), false);
        Account account = new Account(accountNumberString, LocalDate.now(), 1000, AccountType.SAVINGS);
        client.addAccount(account);
        clientService.saveClient(client);

        accountService.saveAccount(account);

        return new ResponseEntity<>("Client created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/current") // authentication es la cookie, contiene el token e informacion del usuario.
    //// Interfaz que representa los detalles del usuario autenticado
    // Obtenemos un ClientDTO Autenticado
    public ClientDTO getClientCurrent(Authentication authentication) {
        return new ClientDTO(clientService.findClientByEmail(authentication.getName()));
    }
}
