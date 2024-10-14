package com.client.service.repository;

import com.client.service.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

    List<Client> findAll();

    void save(Client client);

    Optional<Client> findById(UUID id);

    void delete(UUID id);

    void update(Client client);
}
