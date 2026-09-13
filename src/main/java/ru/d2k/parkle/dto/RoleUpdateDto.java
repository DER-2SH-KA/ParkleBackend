package ru.d2k.parkle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record RoleUpdateDto(

        @JsonProperty("id")
        @NotNull(message = "RoleUpdateDto id cannot be null")
        UUID id,

        @JsonProperty("name")
        @NotBlank(message = "RoleUpdateDto name is null or blank.")
        @Size(max = 32, message = "RoleUpdateDto name's length grater than 32 symbol")
        String name
) { }