package com.client.service.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Task(
        UUID id,
        UUID userId,
        String title,
        String description,
        TaskStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        ) {
}
