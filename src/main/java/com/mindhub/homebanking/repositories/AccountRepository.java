package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.Set;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByNumber(String number);
    Account findByNumber(String number);

    Set<Account> findByClient(Client client);
    Set<Account> findByClientAndIsDeletedFalse(Client client);
    int countByClientAndIsDeleted (Client client, boolean isDeleted);
    boolean existsByIdAndBalanceGreaterThanEqual(Long id, Double balance);
}
