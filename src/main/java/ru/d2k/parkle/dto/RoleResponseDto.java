package ru.d2k.parkle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record RoleResponseDto(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("name")
        String name
) { }