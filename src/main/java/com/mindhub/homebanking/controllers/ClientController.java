package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController // Controlador bajo los parametros de REST(HTTP)
@RequestMapping("/api/clients") // Asocio las peticiones a esta ruta(base).
public class ClientController {
    @Autowired //Injeccion de dependencias, le pedimos a Spring Boot que introduzca ClientRepository en esta clase.
    private ClientRepository clientRepository;

    @GetMapping
    public List<ClientDTO> getAllClients(){ // Esto solo es un metodo!
        List<Client> clients = clientRepository.findAll(); //Le pido al JPARepository el listado
        Stream<Client> clientStream = clients.stream();
        Stream<ClientDTO> clientDTOStream = clientStream.map(ClientDTO::new);
        return clientDTOStream.collect(Collectors.toList());
    }

    @GetMapping("/{id}") //Servlets anotaciones + metodo
    public ClientDTO getClientById(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(ClientDTO::new) // Convierte el cliente a un DTO
                .orElse(null); // Si no se encuentra, retorna null
    }
}
