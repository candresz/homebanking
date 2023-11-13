package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource // genericos
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);



    Boolean existsByEmail(String email);
}