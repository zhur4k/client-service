package com.client.service.dto;

import com.client.service.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskWithClientNameDto(
    UUID id,
    String clientName,
    String title,
    String description,
    TaskStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){
}
