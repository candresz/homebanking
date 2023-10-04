package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public List<ClientDTO> getAllClients(){
        List<Client> clients = clientRepository.findAll();
        Stream<Client> clientStream = clients.stream();
        Stream<ClientDTO> clientDTOStream = clientStream.map(ClientDTO::new);
        return clientDTOStream.collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClientDTO getClientById(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(ClientDTO::new) // Convierte el cliente a un DTO
                .orElse(null); // Si no se encuentra, retorna null
    }
}
