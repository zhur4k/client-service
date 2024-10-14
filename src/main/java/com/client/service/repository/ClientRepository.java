package com.client.service.repository;

import com.client.service.model.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ClientRepository {

    Flux<Client> findAll();

    Mono<Void> save(Mono<Client> clientMono);

    Mono<Client> findById(Mono<UUID> idMono);

    Mono<Void> delete(Mono<UUID> idMono);

    Mono<Void> update(Mono<Client> clientMono);
}
