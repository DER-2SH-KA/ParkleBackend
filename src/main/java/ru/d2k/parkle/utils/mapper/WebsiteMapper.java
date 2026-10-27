package ru.d2k.parkle.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dto.WebsiteResponseDto;
import ru.d2k.parkle.dto.WebsiteTagResponseDto;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import ru.d2k.parkle.model.domain.Website;
import ru.d2k.parkle.model.domain.WebsiteTag;
import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class WebsiteMapper {

    @Mapping(target = "tags", source = "tags", qualifiedByName = "getWebsiteTagDtosByCaches")
    public abstract WebsiteResponseDto toResponseDto(Website cache);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "getWebsiteTagCachesByEntities")
    public abstract Website toDomain(WebsiteEntity entity);

    @Named("getWebsiteTagCachesByEntities")
    List<WebsiteTag> getWebsiteTagCachesByEntities(List<WebsiteTagEntity> entities) {
        return entities.stream().map(entity ->
                new WebsiteTag(entity.getId(), entity.getTitle(), entity.getHex())).toList();
    }

    @Named("getWebsiteTagDtosByCaches")
    List<WebsiteTagResponseDto> getWebsiteTagDtosByCaches(List<WebsiteTag> caches) {
        return caches.stream().map(cache ->
                new WebsiteTagResponseDto(cache.id(), cache.title(), cache.hex())
        ).toList();
    }
}