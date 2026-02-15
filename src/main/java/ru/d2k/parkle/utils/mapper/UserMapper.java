package ru.d2k.parkle.utils.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.entity.Website;
import ru.d2k.parkle.entity.cache.UserCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mapper(componentModel = "spring")
@Component
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "rolePriority", source = "role.priority")
    public abstract UserResponseDto toResponseDto(User entity);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "role", target = "role")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "websites", ignore = true)
    @Mapping(target = "password", source = "dto.password", qualifiedByName = "hashPassword")
    public abstract User updateByDto(@MappingTarget User entity, UserUpdateDto dto, Role role);

    /**
     * CACHE => DTO
     * */
    public abstract UserResponseDto toResponseDto(UserCache userCache);

    /**
     * ENTITY => CACHE
     * */
    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "rolePriority", source = "role.priority")
    @Mapping(target = "hashedPassword", source = "password")
    @Mapping(target = "isBlocked", source = "isBlocked")
    @Mapping(target = "websiteIds", source = "websites", qualifiedByName = "fromWebsitesToUUID")
    public abstract UserCache toCache(User entity);

    @Named("fromWebsitesToUUID")
    List<UUID> fromWebsitesToUUID(List<Website> websites) {
        if (Objects.isNull(websites) || websites.isEmpty()) {
            return new ArrayList<>();
        }

        return websites.stream()
                .map(Website::getId)
                .toList();
    }

    @Named("hashPassword")
    String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
