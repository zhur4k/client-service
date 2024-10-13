package com.client.service.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Client(
        UUID id,
        String name,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        ) {
}
