package com.mindhub.homebanking.repositoryTests;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;




@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {
    @Autowired
    ClientRepository clientRepository;

    List<Client> clients;

    @BeforeEach
    public void getAllClients() {
      clients =  clientRepository.findAll();
    }

    @Test
    public void isEmpty() {
        assertThat(clients, is(not(empty())));
    }

    @Test
    public void validEmail() {
        for (Client client : clients) {
            assertThat(client.getEmail(), containsString("@"));
        }
    }
}