package ru.d2k.parkle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import ru.d2k.parkle.entity.Role;

import java.util.UUID;

/** DTO of {@link Role} **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private UUID id;

    @NotBlank(message = "RoleDto name is null or blank.")
    private String name;

    @NotNull(message = "RoleDto priority is null")
    @Positive(message = "RoleDto priority lower than 1")
    private Integer priority;
}
