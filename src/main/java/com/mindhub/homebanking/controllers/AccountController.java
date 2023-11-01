package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api") // Asocio las peticiones a esta ruta. get,post, etc.
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
                                 //  10        100                  // 0 - 1          90       +   10 = 10.25
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @GetMapping ("/accounts") // Asocio una solicitud get
    public List<AccountDTO> getAllAccounts(){ // Esto solo es un metodo!
        List<Account> accounts = accountRepository.findAll(); //Le pido al JPARepository el listado
        Stream<Account> accountStream = accounts.stream(); // Convertimos a stream para usar las funciones de orden superior
        Stream<AccountDTO> accountDTOStream = accountStream.map(AccountDTO::new);
        return accountDTOStream.collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}") // asocio una solicitud get a esta ruta.
    public AccountDTO getAccountById(@PathVariable Long id) { //Toma el valor que recibe de la URL y se lo asigna a id
        return accountRepository.findById(id) // Hago uso del metodo findById, gracias a la inyeccion de accountRepository
                .map(AccountDTO::new) // Convierte a cuentaDTO xq recibe la original, si encuentra el id
                .orElse(null); // Si no se encuentra, retorna null
    }
    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccountClientCurrent(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName())
                .getAccounts()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toSet());
    }


    @PostMapping("/clients/current/accounts")
    public ResponseEntity<String> newAccount(Authentication authentication) {

        // encapsulo el cliente
        Client client = clientRepository.findByEmail(authentication.getName());

        // controlo que no haya mas de 3 cuentas
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Cannot create any more accounts for this client", HttpStatus.FORBIDDEN);
        }

        // genero un numero de cuenta random
        int accountNumber;
        String accountNumberString;

        do {
            accountNumber = getRandomNumber(0, 99999999);
            accountNumberString = String.valueOf(accountNumber);
        } while (accountRepository.existsByNumber(accountNumberString));

        // Creo la cuenta nueva y la agrego al cliente
        Account account = new Account(accountNumberString, LocalDate.now(), 0 );
        client.addAccount(account);

        // guardo el cliente con la nueva cuenta y devuelvo respuesta exitosa
        clientRepository.save(client);
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
