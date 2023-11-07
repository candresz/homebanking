package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

public interface ClientService {
List<Client> getAllClients();
List<ClientDTO> getAllClientsDTO();
Client findClientById(Long id);

ClientDTO findClientDTOById(Long id);
Client findClientByEmail(String email);

void saveClient(Client client);


}
