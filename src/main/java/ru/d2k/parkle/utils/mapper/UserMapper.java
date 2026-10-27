package ru.d2k.parkle.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dto.RoleResponseDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.entity.RoleEntity;
import ru.d2k.parkle.entity.UserEntity;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.model.domain.Role;
import ru.d2k.parkle.model.domain.User;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public abstract class UserMapper {

    @Autowired
    private RoleMapper roleMapper;

    @Mapping(target = "roles", source = "roles", qualifiedByName = "getRoleDtosByCaches")
    public abstract UserResponseDto toResponseDto(User user);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "getRoleCachesByEntities")
    @Mapping(target = "hashedPassword", source = "password")
    @Mapping(target = "websiteIds", source = "websites", qualifiedByName = "getWebsiteIds")
    public abstract User toDomain(UserEntity entity);

    @Named("getRoleDtosByCaches")
    Set<RoleResponseDto> getRoleDtosByCaches(Set<Role> caches) {
        return caches.stream().map(roleMapper::toResponseDto).collect(Collectors.toSet());
    }

    @Named("getRoleCachesByEntities")
    Set<Role> getRoleCachesByEntities(Set<RoleEntity> entities) {
        return entities.stream().map(roleMapper::toDomain).collect(Collectors.toSet());
    }

    @Named("getWebsiteIds")
    List<UUID> getWebsiteIds(List<WebsiteEntity> websites) {
        return websites.stream()
                .map(WebsiteEntity::getId)
                .toList();
    }
}