package com.client.service.controller;

import com.client.service.dto.ClientCreateDto;
import com.client.service.dto.ClientUpdateDto;
import com.client.service.model.Client;
import com.client.service.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientRestControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientRestController controller;

    @Test
    void getAll_ReturnClients() {
        // given
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Flux<Client> clients = Flux.fromIterable(List.of(
                new Client(id1, null, null, null, null, null),
                new Client(id2, null, null, null, null, null)
        ));
        doReturn(clients).when(this.clientService).getAll();

        // when
        Flux<Client> result = this.controller.getAll();

        // then
        StepVerifier.create(result)
                .expectNextMatches(client -> client.id().equals(id1))
                .expectNextMatches(client -> client.id().equals(id2))
                .verifyComplete();

        verify(this.clientService).getAll();
    }

    @Test
    void getClientById_ReturnStatusOk() {
        // given
        UUID id = UUID.randomUUID();
        Mono<Client> client = Mono.just(new Client(id, null, null, null, null, null));
        doReturn(client).when(this.clientService).getClientById(any(Mono.class));

        // when
        Mono<ResponseEntity<Client>> result = this.controller.getClientById(id);

        // then
        StepVerifier.create(result)
                .expectNextMatches(verify -> verify.getStatusCode().equals(HttpStatus.OK) &&
                        verify.getBody().id().equals(id))
                .verifyComplete();

        verify(clientService).getClientById(any(Mono.class));
    }

    @Test
    void createClient_ReturnStatusOk() {
        // given
        String name = "test_name";
        String email = "test_email";
        String phone = "test_phone";
        Mono<ClientCreateDto> newClientMono = Mono.just(new ClientCreateDto(name, email, phone));
        doReturn(Mono.just(newClientMono)).when(clientService).createClient(newClientMono);

        // when
        Mono<ResponseEntity<Object>> result = this.controller.createClient(newClientMono);

        // then
        StepVerifier.create(result)
                .expectNext(ResponseEntity.ok().build())
                .verifyComplete();

        verify(clientService).createClient(newClientMono);
    }

    @Test
    void updateClient_ReturnClients() {
        // given
        UUID id = UUID.randomUUID();
        Mono<ClientUpdateDto> updatedClient = Mono.just(new ClientUpdateDto(id, null, null, null));
        doReturn(Mono.just(updatedClient)).when(clientService).updateClient(updatedClient);

        // when
        Mono<ResponseEntity<Object>> result = this.controller.updateClient(updatedClient);

        // then
        StepVerifier.create(result)
                .expectNext(ResponseEntity.ok().build())
                .verifyComplete();

        verify(clientService).updateClient(updatedClient);
    }

    @Test
    void deleteClient_ReturnsNoContent() {
        // given
        UUID id = UUID.randomUUID();
        ArgumentCaptor<Mono<UUID>> captor = ArgumentCaptor.forClass(Mono.class);

        doReturn(Mono.empty()).when(this.clientService).removeClient(any(Mono.class));

        // when
        Mono<ResponseEntity<Object>> result = this.controller.deleteClient(id);

        // then
        StepVerifier.create(result)
                .expectNext(ResponseEntity.noContent().build())
                .verifyComplete();

        verify(this.clientService).removeClient(captor.capture());
        captor.getValue().subscribe(actualId -> assertEquals(id, actualId));
    }
}
