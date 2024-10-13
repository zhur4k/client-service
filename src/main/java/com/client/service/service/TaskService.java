package com.client.service.service;

import com.client.service.dto.TaskCreateDto;
import com.client.service.dto.TaskUpdateDto;
import com.client.service.dto.TaskWithClientNameDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TaskService {

    Mono<Void> createTask(TaskCreateDto taskCreateDto);

    Mono<TaskWithClientNameDto> getTaskById(UUID id);

    Mono<Void> removeTask(UUID id);

    Flux<TaskWithClientNameDto> getAll();

    Mono<Void> updateTask(TaskUpdateDto updateDto);
}
