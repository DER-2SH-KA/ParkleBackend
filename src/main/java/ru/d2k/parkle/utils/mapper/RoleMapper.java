package ru.d2k.parkle.utils.mapper;

import org.mapstruct.*;
import ru.d2k.parkle.dto.RoleUpdateDto;
import ru.d2k.parkle.dto.RoleResponseDto;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.cache.RoleCache;

@Mapper(componentModel = "spring")
public interface RoleMapper{

    /**
     * DTO => ENTITY
     * Update {@link Role} object by {@link RoleUpdateDto}
     * without ID value.
     * **/
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    Role updateEntityByDto(
            @MappingTarget Role role,
            RoleUpdateDto dto
    );

    /**
     * CACHE => DTO
     * */
    RoleResponseDto toResponseDto(RoleCache cache);

    /**
     * ENTITY => CACHE
     * */
    RoleCache toCache(Role entity);
}
