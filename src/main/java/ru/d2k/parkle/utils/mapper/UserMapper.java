package ru.d2k.parkle.utils.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.entity.RoleEntity;
import ru.d2k.parkle.entity.UserEntity;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.entity.cache.UserCache;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "rolePriority", source = "role.priority")
    public abstract UserResponseDto toResponseDto(UserEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "role", target = "role")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "websites", ignore = true)
    @Mapping(target = "password", source = "dto.password", qualifiedByName = "getHashOfPassword")
    public abstract void updateEntityByDto(@MappingTarget UserEntity entity, UserUpdateDto dto, RoleEntity role);

    public abstract UserResponseDto toResponseDto(UserCache userCache);

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "rolePriority", source = "role.priority")
    @Mapping(target = "hashedPassword", source = "password")
    @Mapping(target = "isBlocked", source = "isBlocked")
    @Mapping(target = "websiteIds", source = "websites", qualifiedByName = "getWebsiteIds")
    public abstract UserCache toCache(UserEntity entity);

    @Named("getWebsiteIds")
    List<UUID> getWebsiteIds(List<WebsiteEntity> websites) {
        if (Objects.isNull(websites) || websites.isEmpty()) {
            return new ArrayList<>();
        }

        return websites.stream()
                .map(WebsiteEntity::getId)
                .toList();
    }

    @Named("getHashOfPassword")
    String getHashOfPassword(String password) {
        return passwordEncoder.encode(password);
    }
}