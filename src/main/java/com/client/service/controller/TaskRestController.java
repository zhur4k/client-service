package com.client.service.controller;

import com.client.service.dto.TaskCreateDto;
import com.client.service.dto.TaskUpdateDto;
import com.client.service.dto.TaskWithClientNameDto;
import com.client.service.service.TaskService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
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
@OpenAPIDefinition(info = @Info(title = "Task Service API", version = "1.0"))
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;

    @GetMapping("getAll")
    public Flux<TaskWithClientNameDto> getAll(){
        return taskService.getAll()
                .onErrorResume(ex -> Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch clients")));
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<TaskWithClientNameDto>> getTaskById(@PathVariable UUID id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PostMapping("create")
    public Mono<ResponseEntity<Object>> createTask(
            @Validated @RequestBody Mono<TaskCreateDto> taskMono
    ){
        return taskMono
                .flatMap(task -> this.taskService.createTask(task)
                .then(Mono.just(ResponseEntity.ok().build())))
                        .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("update")
    public Mono<ResponseEntity<Object>> updateTask(
            @Validated @RequestBody Mono<TaskUpdateDto> taskMono
    ){
        return taskMono
                .flatMap(task -> this.taskService.updateTask(task)
                        .then(Mono.just(ResponseEntity.ok().build())))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

   @DeleteMapping("{id}")
   public Mono<ResponseEntity<Object>> deleteTask(
           @PathVariable UUID id){
        return taskService.removeTask(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
   }
}
