package ru.d2k.parkle.utils.mapper;

import org.mapstruct.*;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.utils.resolver.RoleResolver;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class, RoleResolver.class}
)
public interface UserMapper {

    UserResponseDto toResponseDto(User entity);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "roleName", target = "role", qualifiedByName = "fromNameToRole")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "websites", ignore = true)
    User updateByDto(@MappingTarget User entity, UserUpdateDto dto);

}
