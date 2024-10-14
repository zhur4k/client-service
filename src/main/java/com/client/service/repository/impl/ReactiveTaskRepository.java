package com.client.service.repository.impl;

import com.client.service.dto.TaskWithClientNameDto;
import com.client.service.model.Task;
import com.client.service.model.TaskStatus;
import com.client.service.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReactiveTaskRepository implements TaskRepository{

    private final DatabaseClient databaseClient;

    @Override
    public Flux<TaskWithClientNameDto> findAll() {
        return databaseClient.sql("""
            SELECT t.*, c.c_name AS c_client_name
            FROM t_task t
            JOIN t_client c ON t.c_client_id = c.id
           """)
                .map((row, metadata) -> new TaskWithClientNameDto(
                        row.get("id", UUID.class),
                        row.get("c_client_name", String.class),
                        row.get("c_title", String.class),
                        row.get("c_description", String.class),
                        TaskStatus.valueOf(row.get("c_status", String.class)),
                        row.get("c_created_at", LocalDateTime.class),
                        row.get("c_updated_at", LocalDateTime.class)))
                .all();
    }

    @Override
    public Mono<TaskWithClientNameDto> findById(Mono<UUID> idMono) {
        return idMono.flatMap(id -> databaseClient.sql("""
            SELECT t.*, c.c_name AS c_client_name 
            FROM t_task t 
            JOIN t_client c ON t.c_client_id = c.id 
            WHERE t.id = :id
           """)
                .bind("id", id)
                .map((row, metadata) -> new TaskWithClientNameDto(
                        row.get("id", UUID.class),
                        row.get("c_client_name", String.class),
                        row.get("c_title", String.class),
                        row.get("c_description", String.class),
                        TaskStatus.valueOf(row.get("c_status", String.class)),
                        row.get("c_created_at", LocalDateTime.class),
                        row.get("c_updated_at", LocalDateTime.class)))
                .one());
    }

    @Override
    public Mono<Void> save(Mono<Task> taskMono) {
        return taskMono.flatMap(task -> databaseClient.sql("""
            INSERT INTO t_task (id, c_client_id, c_title, c_description, c_status, c_created_at, c_updated_at) 
            VALUES (:id, :clientId, :title, :description, :status::task_status, :createdAt, :updatedAt)
           """)
                .bind("id", task.id())
                .bind("clientId", task.clientId())
                .bind("title", task.title())
                .bind("description", task.description())
                .bind("status", task.status().name())
                .bind("createdAt", task.createdAt())
                .bind("updatedAt", task.updatedAt())
                .fetch()
                .rowsUpdated()
                .then());
    }

    @Override
    public Mono<Void> delete(Mono<UUID> idMono) {
        return idMono.flatMap(id -> databaseClient.sql("DELETE FROM t_task WHERE id = :id")
                .bind("id", id)
                .fetch()
                .rowsUpdated()
                .then());
    }

    @Override
    public Mono<Void> update(Mono<Task> taskMono) {
        return taskMono.flatMap(task -> databaseClient.sql("""
            UPDATE t_task 
            SET c_title = :title, c_status = :status::task_status, c_description = :description, c_updated_at = :updatedAt 
            WHERE id = :id
           """)
                .bind("id", task.id())
                .bind("title", task.title())
                .bind("status", task.status().name())
                .bind("description", task.description())
                .bind("updatedAt", task.updatedAt())
                .fetch()
                .rowsUpdated()
                .then());
    }
}
