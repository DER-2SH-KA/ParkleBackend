package ru.d2k.parkle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import java.util.UUID;

public record UserResponseDto(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("login")
        String login,

        @JsonProperty("roles")
        Set<RoleResponseDto> roles
) { }