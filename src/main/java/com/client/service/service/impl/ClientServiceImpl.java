package com.client.service.service.impl;

import com.client.service.dto.ClientCreateDto;
import com.client.service.dto.ClientUpdateDto;
import com.client.service.model.Client;
import com.client.service.repository.ClientRepository;
import com.client.service.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final TransactionalOperator transactionalOperator;
    @Override
    public Mono<Void> createClient(Mono<ClientCreateDto> clientCreateDtoMono) {
        return clientCreateDtoMono.flatMap(clientCreateDto -> {
            Client client = new Client(
                    UUID.randomUUID(),
                    clientCreateDto.name(),
                    clientCreateDto.email(),
                    clientCreateDto.phone(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            return transactionalOperator.transactional(clientRepository.save(Mono.just(client)));
        }).then();
    }

    @Override
    public Mono<Client> getClientById(Mono<UUID> idMono) {
        return transactionalOperator.transactional(
                clientRepository.findById(idMono));
    }

    @Override
    public Mono<Void> removeClient(Mono<UUID> idMono) {
        return transactionalOperator.transactional(
                clientRepository.delete(idMono))
                .then();
    }

    @Override
    public Flux<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public Mono<Void> updateClient(Mono<ClientUpdateDto> updateDtoMono) {
        return updateDtoMono.flatMap(updateDto -> {
            Client client = new Client(
                    updateDto.id(),
                    updateDto.name(),
                    updateDto.email(),
                    updateDto.phone(),
                    null,
                    LocalDateTime.now()
            );
            return transactionalOperator.transactional(
                    clientRepository.update(Mono.just(client)));
        }).then();
    }
}
