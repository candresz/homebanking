package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.implement.AccountServiceImplement;
import com.mindhub.homebanking.services.implement.ClientServiceImplement;
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
   private AccountService accountService;

    @Autowired
    private ClientService clientService;

                                 //  10        100                  // 0 - 1          90       +   10 = 10.25
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @GetMapping ("/accounts") // Asocio una solicitud get
    public List<AccountDTO> getAllAccounts(){ // Esto solo es un metodo!
        return accountService.getAllAccountsDTO();
    }

    @GetMapping("/accounts/{id}") // asocio una solicitud get a esta ruta.
    public AccountDTO getAccountById(@PathVariable Long id) { //Toma el valor que recibe de la URL y se lo asigna a id
        return accountService.getAccountDTOById(id); //

    }
    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccountClientCurrent(Authentication authentication) {
        return accountService.getAllAccountsDTOByClient(clientService.findClientByEmail(authentication.getName()));

    }


    @PostMapping("/clients/current/accounts")
    public ResponseEntity<String> newAccount(Authentication authentication) {

        // encapsulo el cliente
        Client client = clientService.findClientByEmail(authentication.getName());

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
        } while (accountService.existsAccountByNumber(accountNumberString));

        // Creo la cuenta nueva y la agrego al cliente
        Account account = new Account(accountNumberString, LocalDate.now(), 0 );
        client.addAccount(account);

        // guardo el cliente con la nueva cuenta y devuelvo respuesta exitosa
        clientService.saveClient(client);
        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
