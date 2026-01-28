package ru.d2k.parkle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RoleCreateDto(
        @NotBlank(message = "RoleCreateDto name is null or blank.")
        @Size(
                max = 32,
                message = "RoleCreateDto name's length grater than 32 symbol"
        )
        String name,

        @NotNull(message = "RoleCreateDto priority is null")
        @Positive(message = "RoleCreateDto priority lower than 1")
        Integer priority
) { }
