package com.client.service.dto;

import com.client.service.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskCreateDto(
        UUID userId,
        String title,
        String description,
        TaskStatus status
        ) {
}
