package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.ClientLoan; // Importa la clase correcta

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {
}
