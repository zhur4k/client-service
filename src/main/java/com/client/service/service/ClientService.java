package com.client.service.service;

import com.client.service.dto.ClientUpdateDto;
import com.client.service.model.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ClientService {

    Mono<Void> addClient(Client client);

    Mono<Client> getClientById(UUID id);

    Mono<Void> removeClient(UUID id);

    Flux<Client> getAll();

    Mono<Void> updateClient(ClientUpdateDto updateDto);
}
