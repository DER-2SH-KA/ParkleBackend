package ru.d2k.parkle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleCreateDto(

        @JsonProperty("name")
        @NotBlank(message = "RoleCreateDto name is null or blank.")
        @Size(max = 32, message = "RoleCreateDto name's length grater than 32 symbol")
        String name
) { }
