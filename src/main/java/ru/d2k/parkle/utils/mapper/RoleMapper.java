package ru.d2k.parkle.utils.mapper;

import org.mapstruct.*;
import ru.d2k.parkle.dto.RoleDto;
import ru.d2k.parkle.dto.RoleResponseDto;
import ru.d2k.parkle.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper{
    /**
     * Create new {@link RoleDto} by {@link Role} object.
     * @param role {@link Role} role entity.
     * @return {@link RoleDto} object.
     * **/
    RoleDto toDto(Role role);

    /**
     * Create new {@link RoleResponseDto} by {@link Role} object.
     * @param role
     * @return
     */
    RoleResponseDto toUserRoleDto(Role role);

    /**
     * Update {@link Role} object by {@link RoleDto}
     * without ID value.
     * **/
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    Role updateEntityByDto(
            @MappingTarget Role role,
            RoleDto dto
    );
}
