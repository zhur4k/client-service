package com.client.service.service.impl;

import com.client.service.dto.ClientCreateDto;
import com.client.service.dto.ClientUpdateDto;
import com.client.service.model.Client;
import com.client.service.repository.ClientRepository;
import com.client.service.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Mono<Void> createClient(ClientCreateDto clientCreateDto) {

        return Mono.fromRunnable(() -> clientRepository.save(new Client(
                        UUID.randomUUID(),
                        clientCreateDto.name(),
                        clientCreateDto.email(),
                        clientCreateDto.phone(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )))
                .then();
    }

    @Override
    public Mono<Client> getClientById(UUID id) {
        return Mono.just(clientRepository.findById(id).get());
    }

    @Override
    public Mono<Void> removeClient(UUID id) {
        return Mono.fromRunnable(() -> clientRepository.delete(id))
                .then();
    }

    @Override
    public Flux<Client> getAll() {
        return Flux.fromIterable(clientRepository.findAll());
    }

    @Override
    public Mono<Void> updateClient(ClientUpdateDto updateDto) {
        Client client = new Client(
                updateDto.id(),
                updateDto.name(),
                updateDto.email(),
                updateDto.phone(),
                null,
                LocalDateTime.now()
        );

        return Mono.fromRunnable(() -> clientRepository.update(client))
                .then();
    }
}
