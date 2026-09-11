package ru.d2k.parkle.utils.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dto.WebsiteResponseDto;
import ru.d2k.parkle.dto.WebsiteUpdateDto;
import ru.d2k.parkle.entity.UserEntity;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import ru.d2k.parkle.entity.cache.WebsiteCache;
import ru.d2k.parkle.entity.cache.WebsiteTagCache;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public abstract class WebsiteMapper {

    @Mapping(target = "userLogin", source = "user.login")
    public abstract WebsiteResponseDto toResponseDto(WebsiteEntity dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "dto.url", target = "url")
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    public abstract void updateEntityByDto(@MappingTarget WebsiteEntity entity, WebsiteUpdateDto dto, UserEntity user);

    @Mapping(target = "userLogin", source = "userLogin")
    public abstract WebsiteResponseDto toResponseDto(WebsiteCache cache);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "getWebsiteTagCachesByEntities")
    public abstract WebsiteCache toCache(WebsiteEntity entity);

    @Named("getWebsiteTagCachesByEntities")
    Set<WebsiteTagCache> getWebsiteTagCachesByEntities(Set<WebsiteTagEntity> entities) {
        return entities.stream().map(entity ->
                new WebsiteTagCache(entity.getId(), entity.getTitle(), entity.getHex())).collect(Collectors.toSet());
    }
}