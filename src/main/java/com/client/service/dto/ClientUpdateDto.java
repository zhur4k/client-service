package com.client.service.dto;

import java.util.UUID;

public record ClientUpdateDto(
        UUID id,
        String name,
        String phone
) {
}
