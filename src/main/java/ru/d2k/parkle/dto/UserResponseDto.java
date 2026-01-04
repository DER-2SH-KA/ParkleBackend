package ru.d2k.parkle.dto;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        RoleResponseDto role,
        String login,
        String email
) { }
