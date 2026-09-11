package ru.d2k.parkle.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dto.RoleResponseDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.entity.RoleEntity;
import ru.d2k.parkle.entity.UserEntity;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.entity.cache.RoleCache;
import ru.d2k.parkle.entity.cache.UserCache;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(target = "roles", source = "roles", qualifiedByName = "getRoleDtosByEntities")
    public abstract UserResponseDto toResponseDto(UserEntity entity);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "getRoleDtosByCaches")
    public abstract UserResponseDto toResponseDto(UserCache userCache);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "getRoleCachesByEntities")
    @Mapping(target = "hashedPassword", source = "password")
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

    @Named("getRoleCachesByEntities")
    Set<RoleCache> getRoleCachesByEntities(Set<RoleEntity> entities) {
        return entities.stream().map(entity ->
                new RoleCache(entity.getId(), entity.getName())).collect(Collectors.toSet());
    }

    @Named("getRoleDtosByEntities")
    Set<RoleResponseDto> getRoleDtosByEntities(Set<RoleEntity> entities) {
        return entities.stream().map(entity ->
                new RoleResponseDto(entity.getId(), entity.getName())).collect(Collectors.toSet());
    }

    @Named("getRoleDtosByCaches")
    Set<RoleResponseDto> getRoleDtosByCaches(Set<RoleCache> caches) {
        return caches.stream().map(cache ->
                new RoleResponseDto(cache.id(), cache.name())).collect(Collectors.toSet());
    }
}