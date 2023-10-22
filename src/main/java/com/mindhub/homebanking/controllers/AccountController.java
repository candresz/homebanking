package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/accounts") // Asocio las peticiones a esta ruta. get,post, etc.
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping // Asocio una solicitud get
    public List<AccountDTO> getAllAccounts(){ // Esto solo es un metodo!
        List<Account> accounts = accountRepository.findAll(); //Le pido al JPARepository el listado
        Stream<Account> accountStream = accounts.stream(); // Convertimos a stream para usar las funciones de orden superior
        Stream<AccountDTO> accountDTOStream = accountStream.map(AccountDTO::new);
        return accountDTOStream.collect(Collectors.toList());
    }

    @GetMapping("/{id}") // asocio una solicitud get a esta ruta.
    public AccountDTO getAccountById(@PathVariable Long id) { //Toma el valor que recibe de la URL y se lo asigna a id
        return accountRepository.findById(id) // Hago uso del metodo findById, gracias a la inyeccion de accountRepository
                .map(AccountDTO::new) // Convierte a cuentaDTO xq recibe la original, si encuentra el id
                .orElse(null); // Si no se encuentra, retorna null
    }
}
