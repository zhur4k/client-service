package com.client.service.controller;

import com.client.service.dto.ClientCreateDto;
import com.client.service.dto.ClientUpdateDto;
import com.client.service.model.Client;
import com.client.service.service.ClientService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("clients")
@OpenAPIDefinition(info = @Info(title = "Client Service API", version = "1.0"))
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("getAll")
    public Flux<Client> getAll(){
        return clientService.getAll()
                .onErrorResume(ex -> Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch clients")));
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Client>> getClientById(@PathVariable UUID id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PostMapping("create")
    public Mono<ResponseEntity<Object>> createClient(
            @Validated @RequestBody Mono<ClientCreateDto> clientMono
    ){
        return clientMono
                .flatMap(client -> this.clientService.createClient(client)
                .then(Mono.just(ResponseEntity.ok().build())))
                        .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("update")
    public Mono<ResponseEntity<Object>> updateClient(
            @Validated @RequestBody Mono<ClientUpdateDto> clientMono
    ){
        return clientMono
                .flatMap(client -> this.clientService.updateClient(client)
                        .then(Mono.just(ResponseEntity.ok().build())))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

   @DeleteMapping("{id}")
   public Mono<ResponseEntity<Object>> deleteClient(
           @PathVariable UUID id){
        return clientService.removeClient(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
   }
}
