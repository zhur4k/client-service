package com.client.service.controller;

import com.client.service.dto.*;
import com.client.service.model.TaskStatus;
import com.client.service.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskRestControllerTest{

    @Mock
    private TaskService taskService;

    @InjectMocks
    TaskRestController controller;

    @Test
    void getAll_ReturnTasks(){
        // given
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Flux<TaskWithClientNameDto> tasks = Flux.fromIterable(List.of(
                new TaskWithClientNameDto(id1, "Test Client 1", "Task 1", "Description 1", TaskStatus.COMPLETED,null,null),
                new TaskWithClientNameDto(id2, "Test Client 2", "Task 2", "Description 2", TaskStatus.IN_PROGRESS,null,null)
        ));
        doReturn(tasks).when(this.taskService).getAll();

        // when
        Flux<TaskWithClientNameDto> result = this.controller.getAll();

        // then
        StepVerifier.create(result)
                .expectNextMatches(task -> task.id().equals(id1))
                .expectNextMatches(task -> task.id().equals(id2))
                .verifyComplete();

        verify(this.taskService).getAll();
    }

    @Test
    void getTaskById_ReturnStatusOk() {
        // given
        UUID id = UUID.randomUUID();
        Mono<TaskWithClientNameDto> task = Mono.just(
                new TaskWithClientNameDto(id, "Test Client", "Task", "Description", TaskStatus.IN_PROGRESS, null, null)
        );
        doReturn(task).when(this.taskService).getTaskById(any(Mono.class));

        // when
        Mono<ResponseEntity<TaskWithClientNameDto>> result = this.controller.getTaskById(id);

        // then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode().equals(HttpStatus.OK) &&
                        response.getBody().id().equals(id))
                .verifyComplete();

        verify(taskService).getTaskById(any(Mono.class));
    }

    @Test
    void createTask_ReturnStatusOk() {
        // given
        Mono<TaskCreateDto> newTaskDto = Mono.just(new TaskCreateDto(UUID.randomUUID(), "Task Title", "Task Description",  TaskStatus.IN_PROGRESS));
        doReturn(Mono.just(newTaskDto)).when(taskService).createTask(newTaskDto);

        // when
        Mono<ResponseEntity<Object>> result = this.controller.createTask(newTaskDto);

        // then
        StepVerifier.create(result)
                .expectNext(ResponseEntity.ok().build())
                .verifyComplete();

        verify(taskService).createTask(newTaskDto);
    }

    @Test
    void updateTask_ReturnStatusOk() {
        // given
        UUID id = UUID.randomUUID();
        Mono<TaskUpdateDto> updatedTask = Mono.just(new TaskUpdateDto(id, "Updated Task Title", "Updated Description", TaskStatus.IN_PROGRESS));
        doReturn(Mono.just(updatedTask)).when(taskService).updateTask(updatedTask);

        // when
        Mono<ResponseEntity<Object>> result = this.controller.updateTask(updatedTask);

        // then
        StepVerifier.create(result)
                .expectNext(ResponseEntity.ok().build())
                .verifyComplete();

        verify(taskService).updateTask(updatedTask);
    }

    @Test
    void deleteTask_ReturnsNoContent() {
        // given
        UUID id = UUID.randomUUID();
        doReturn(Mono.empty()).when(this.taskService).removeTask(any(Mono.class));

        // when
        Mono<ResponseEntity<Object>> result = this.controller.deleteTask(id);

        // then
        StepVerifier.create(result)
                .expectNext(ResponseEntity.noContent().build())
                .verifyComplete();

        verify(this.taskService).removeTask(any(Mono.class));
    }
}
