package com.client.service.service;

import com.client.service.dto.TaskCreateDto;
import com.client.service.dto.TaskUpdateDto;
import com.client.service.dto.TaskWithClientNameDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TaskService {

    Mono<Void> createTask(Mono<TaskCreateDto> taskCreateDtoMono);

    Mono<TaskWithClientNameDto> getTaskById(Mono<UUID> idMono);

    Mono<Void> removeTask(Mono<UUID> idMono);

    Flux<TaskWithClientNameDto> getAll();

    Mono<Void> updateTask(Mono<TaskUpdateDto> updateDtoMono);
}
