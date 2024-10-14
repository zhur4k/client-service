package com.client.service.service;

import com.client.service.dto.ClientCreateDto;
import com.client.service.dto.ClientUpdateDto;
import com.client.service.model.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ClientService {

    Mono<Void> createClient(Mono<ClientCreateDto> clientCreateDtoMono);

    Mono<Client> getClientById(Mono<UUID> idMono);

    Mono<Void> removeClient(Mono<UUID> idMono);

    Flux<Client> getAll();

    Mono<Void> updateClient(Mono<ClientUpdateDto> updateDtoMono);
}
