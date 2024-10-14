package com.client.service.service.impl;

import com.client.service.dto.TaskCreateDto;
import com.client.service.dto.TaskUpdateDto;
import com.client.service.dto.TaskWithClientNameDto;
import com.client.service.model.Task;
import com.client.service.repository.TaskRepository;
import com.client.service.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Void> createTask(Mono<TaskCreateDto> taskCreateDtoMono) {
        return taskCreateDtoMono.flatMap(taskCreateDto -> taskRepository.save(
                Mono.just(new Task(
                        UUID.randomUUID(),
                        taskCreateDto.clientId(),
                        taskCreateDto.title(),
                        taskCreateDto.description(),
                        taskCreateDto.status(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
        ))).then());
    }

    @Override
    public Mono<TaskWithClientNameDto> getTaskById(Mono<UUID> idMono) {
        return transactionalOperator.transactional(
                taskRepository.findById(idMono)
        );
    }

    @Override
    public Mono<Void> removeTask(Mono<UUID> idMono) {
        return transactionalOperator.transactional(
                taskRepository.delete(idMono)
        ).then();
    }

    @Override
    public Flux<TaskWithClientNameDto> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public Mono<Void> updateTask(Mono<TaskUpdateDto> updateDtoMono) {
        return updateDtoMono.flatMap(updateDto -> taskRepository.update(
                Mono.just(
                        new Task(
                        updateDto.id(),
                        null,
                        updateDto.title(),
                        updateDto.description(),
                        updateDto.status(),
                        null,
                        LocalDateTime.now()
                )))).then();
    }
}
