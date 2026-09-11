package ru.d2k.parkle.dto;

import java.util.Set;
import java.util.UUID;

public record UserResponseDto(UUID id, String login, Set<RoleResponseDto> roles) { }