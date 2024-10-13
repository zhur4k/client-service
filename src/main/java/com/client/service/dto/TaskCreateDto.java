package com.client.service.dto;

import com.client.service.model.TaskStatus;

import java.util.UUID;

public record TaskCreateDto(
        UUID clientId,
        String title,
        String description,
        TaskStatus status
        ) {
}
