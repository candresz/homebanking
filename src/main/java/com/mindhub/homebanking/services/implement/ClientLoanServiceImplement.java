package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }

    @Override
    public List<ClientLoan> getAllClientLoans(Client client) {
        return clientLoanRepository.findByClient(client);
    }

    public ClientLoan getClientLoan(Client client, Loan loan){
      return  clientLoanRepository.findByClientAndLoan(client, loan);
    }

    @Override
    public boolean existsById(Long id) {
        return clientLoanRepository.existsById(id);
    }

    @Override
    public ClientLoan getClientLoanById(Long id) {
        return clientLoanRepository.findById(id).orElse(null);
    }

    @Override
    public void paidLoan(long id) {
       ClientLoan clientLoan = clientLoanRepository.findById(id).orElse(null);
       clientLoan.setPaid(true);
       saveClientLoan(clientLoan);
    }


}
