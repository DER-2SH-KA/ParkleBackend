package ru.d2k.parkle.utils.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.d2k.parkle.dto.RoleUpdateDto;
import ru.d2k.parkle.dto.RoleResponseDto;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.cache.RoleCache;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityByDto(@MappingTarget Role role, RoleUpdateDto dto);

    RoleResponseDto toResponseDto(RoleCache cache);

    RoleCache toCache(Role entity);
}