package ru.d2k.parkle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.d2k.parkle.entity.Role;

import java.util.UUID;

/** DTO of {@link Role} **/
public record RoleUpdateDto(
        @NotNull
        UUID id,

        @NotBlank(message = "RoleUpdateDto name is null or blank.")
        @Size(
                max = 32,
                message = "RoleUpdateDto name's length grater than 32 symbol"
        )
        String name,

        @NotNull(message = "RoleUpdateDto priority is null")
        @Positive(message = "RoleUpdateDto priority lower than 1")
        Integer priority
) {

}
