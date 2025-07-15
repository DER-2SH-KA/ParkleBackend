package ru.d2k.parkle.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.d2k.parkle.dto.RoleDto;
import ru.d2k.parkle.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper{
    /**
     * Create new {@link RoleDto} by {@link Role} object.
     * **/
    RoleDto toDto(Role role);

    /**
     * Create new {@link Role} object by {@link RoleDto}
     * without ID value.
     * **/
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    Role toNewEntity(RoleDto dto);

    /**
     * Update {@link Role} object by {@link RoleDto}
     * without ID value.
     * **/
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    Role updateEntityByDto(
            @MappingTarget Role role,
            RoleDto dto
    );
}
