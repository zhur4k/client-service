package com.client.service.service;

import com.client.service.dto.TaskCreateDto;
import com.client.service.dto.TaskUpdateDto;
import com.client.service.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TaskService {

    Mono<Void> createTask(TaskCreateDto taskCreateDto);

    Mono<Task> getTaskById(UUID id);

    Mono<Void> removeTask(UUID id);

    Flux<Task> getAll();

    Mono<Void> updateTask(TaskUpdateDto updateDto);
}
