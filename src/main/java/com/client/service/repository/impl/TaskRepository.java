package com.client.service.repository.impl;

import com.client.service.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {

    List<Task> findAll();

    void save(Task task);

    Optional<Task> findById(UUID id);

    void delete(UUID id);
}
