package ru.otus.service;

import ru.otus.model.Client;

import java.util.List;

public interface ClientService {

    Client saveClient(String name);

    List<Client> findAll();
}