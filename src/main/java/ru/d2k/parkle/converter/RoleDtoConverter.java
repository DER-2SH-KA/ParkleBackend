package ru.d2k.parkle.converter;

import lombok.extern.slf4j.Slf4j;
import ru.d2k.parkle.dto.RoleDto;
import ru.d2k.parkle.entity.Role;

import java.util.Objects;

/**
 * Utility class converter between {@link Role} entity and {@link RoleDto}.
 * **/
public final class RoleDtoConverter {
    // Hidden constructor.
    private RoleDtoConverter() {}

    /**
     * Convert from Entity to DTO.
     * @param entity {@link Role} entity.
     * @return {@link RoleDto}.
     * **/
    public static RoleDto toDto(Role entity) {
        Objects.requireNonNull(entity, "Entity must be non null value!");

        return new RoleDto(entity.getId(), entity.getName(), entity.getPriority());
    }

    /**
     * Convert from DTO to Entity.
     * @param dto {@link RoleDto}.
     * @return {@link Role} entity with null ID.
     * **/
    public static Role toNewEntity(RoleDto dto) {
        Objects.requireNonNull(dto, "DTO must be non null value!");

        return new Role(
                dto.getName(),
                dto.getPriority()
        );
    }

    /**
     * Update Entity by DTO.
     * @param entity {@link  Role} entity to edit.
     * @param dto {@link RoleDto}.
     * @return Edited {@link Role} entity.
     * **/
    public static Role updateEntityByDto(Role entity, RoleDto dto) {
        Objects.requireNonNull(entity, "Entity must be non null value!");
        Objects.requireNonNull(dto, "DTO must be non null value!");

        entity.setName( dto.getName() );
        entity.setPriority( dto.getPriority() );

        return entity;
    }
}
