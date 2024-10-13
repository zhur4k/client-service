package com.client.service.repository.impl;

import com.client.service.dto.TaskWithClientNameDto;
import com.client.service.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {

    List<TaskWithClientNameDto> findAll();

    void save(Task task);

    Optional<TaskWithClientNameDto> findById(UUID id);

    void delete(UUID id);

    void update(Task task);
}
