package ru.otus.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Client;
import ru.otus.repository.ClientRepository;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public Client saveClient(String name) {
        return clientRepository.save(new Client(name));
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}