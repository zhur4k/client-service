package com.client.service.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The status of a task")
public enum TaskStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED
}
