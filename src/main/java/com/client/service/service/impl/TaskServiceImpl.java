package com.client.service.service.impl;

import com.client.service.dto.TaskCreateDto;
import com.client.service.dto.TaskUpdateDto;
import com.client.service.dto.TaskWithClientNameDto;
import com.client.service.model.Task;
import com.client.service.repository.TaskRepository;
import com.client.service.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Mono<Void> createTask(TaskCreateDto taskCreateDto) {
        return Mono.fromRunnable(() -> taskRepository.save(new Task(
                        UUID.randomUUID(),
                        taskCreateDto.clientId(),
                        taskCreateDto.title(),
                        taskCreateDto.description(),
                        taskCreateDto.status(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )))
                .then();
    }

    @Override
    public Mono<TaskWithClientNameDto> getTaskById(UUID id) {
        return Mono.just(taskRepository.findById(id).get());
    }

    @Override
    public Mono<Void> removeTask(UUID id) {
        return Mono.fromRunnable(() -> taskRepository.delete(id))
                .then();
    }

    @Override
    public Flux<TaskWithClientNameDto> getAll() {
        return Flux.fromIterable(taskRepository.findAll());
    }

    @Override
    public Mono<Void> updateTask(TaskUpdateDto updateDto) {
        Task task = new Task(
                updateDto.id(),
                null,
                updateDto.title(),
                updateDto.description(),
                updateDto.status(),
                null,
                LocalDateTime.now()
        );

        return Mono.fromRunnable(() -> taskRepository.update(task))
                .then();
    }
}
