package com.client.service.controller;

import com.client.service.dto.ClientCreateDto;
import com.client.service.dto.ClientUpdateDto;
import com.client.service.dto.TaskCreateDto;
import com.client.service.dto.TaskUpdateDto;
import com.client.service.model.Task;
import com.client.service.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("tasks")
public class TaskRestController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping("getAll")
    public Flux<Task> getAll(){
        return taskService.getAll()
                .onErrorResume(ex -> Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch clients")));
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Task>> getClientById(@PathVariable UUID id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PostMapping("create")
    public Mono<ResponseEntity<Object>> createClient(
            @Validated @RequestBody Mono<TaskCreateDto> taskMono
    ){
        return taskMono
                .flatMap(task -> this.taskService.createTask(task)
                .then(Mono.just(ResponseEntity.ok().build())))
                        .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("update")
    public Mono<ResponseEntity<Object>> updateClient(
            @Validated @RequestBody Mono<TaskUpdateDto> taskMono
    ){
        return taskMono
                .flatMap(task -> this.taskService.updateTask(task)
                        .then(Mono.just(ResponseEntity.ok().build())))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

   @DeleteMapping("{id}")
   public Mono<ResponseEntity<Object>> deleteClient(
           @PathVariable UUID id){
        return taskService.removeTask(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
   }
}
