package com.client.service.dto;

import java.util.UUID;

public record ClientCreateDto(

        String name,

        String email,

        String phone
) {
}
