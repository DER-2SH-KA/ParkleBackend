package ru.d2k.parkle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record RoleResponseDto(
        String name,
        Integer priority
) { }
