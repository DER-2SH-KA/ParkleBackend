package ru.d2k.parkle.dto;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String roleName,
        Integer rolePriority,
        String login,
        String email
) { }
