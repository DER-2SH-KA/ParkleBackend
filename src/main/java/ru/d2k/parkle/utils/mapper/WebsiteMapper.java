package ru.d2k.parkle.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dto.WebsiteResponseDto;
import ru.d2k.parkle.dto.WebsiteTagResponseDto;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import ru.d2k.parkle.entity.cache.WebsiteCache;
import ru.d2k.parkle.entity.cache.WebsiteTagCache;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public abstract class WebsiteMapper {

    @Mapping(target = "tags", source = "tags", qualifiedByName = "getWebsiteTagDtosByEntities")
    public abstract WebsiteResponseDto toResponseDto(WebsiteEntity dto);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "getWebsiteTagDtosByCaches")
    public abstract WebsiteResponseDto toResponseDto(WebsiteCache cache);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "getWebsiteTagCachesByEntities")
    public abstract WebsiteCache toCache(WebsiteEntity entity);

    @Named("getWebsiteTagCachesByEntities")
    Set<WebsiteTagCache> getWebsiteTagCachesByEntities(Set<WebsiteTagEntity> entities) {
        return entities.stream().map(entity ->
                new WebsiteTagCache(entity.getId(), entity.getTitle(), entity.getHex())).collect(Collectors.toSet());
    }

    @Named("getWebsiteTagDtosByEntities")
    Set<WebsiteTagResponseDto> getWebsiteTagDtosByEntities(Set<WebsiteTagEntity> entities) {
        return entities.stream().map(entity ->
                        new WebsiteTagResponseDto(entity.getId(), entity.getTitle(), entity.getHex())
                ).collect(Collectors.toSet());
    }

    @Named("getWebsiteTagDtosByCaches")
    Set<WebsiteTagResponseDto> getWebsiteTagDtosByCaches(Set<WebsiteTagCache> caches) {
        return caches.stream().map(cache ->
                new WebsiteTagResponseDto(cache.id(), cache.title(), cache.hex())
        ).collect(Collectors.toSet());
    }
}