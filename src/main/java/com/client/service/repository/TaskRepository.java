package com.client.service.repository;

import com.client.service.dto.TaskWithClientNameDto;
import com.client.service.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TaskRepository {

    Flux<TaskWithClientNameDto> findAll();

    Mono<Void> save(Mono<Task> taskMono);

    Mono<TaskWithClientNameDto> findById(Mono<UUID> idMono);

    Mono<Void> delete(Mono<UUID> idMono);

    Mono<Void> update(Mono<Task> taskMono);
}
