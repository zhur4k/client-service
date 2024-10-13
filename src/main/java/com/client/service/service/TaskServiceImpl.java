package com.client.service.service;

import com.client.service.dto.TaskUpdateDto;
import com.client.service.model.Client;
import com.client.service.model.Task;
import com.client.service.repository.impl.TaskRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @Override
    public Mono<Void> addTask(Task task) {
        return Mono.fromRunnable(() -> taskRepository.save(task))
                .then();
    }

    @Override
    public Mono<Task> getTaskById(UUID id) {
        return Mono.just(taskRepository.findById(id).get());
    }

    @Override
    public Mono<Void> removeTask(UUID id) {
        return Mono.fromRunnable(() -> taskRepository.delete(id))
                .then();
    }

    @Override
    public Flux<Task> getAll() {
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
