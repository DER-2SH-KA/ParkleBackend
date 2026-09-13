package ru.d2k.parkle.utils.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dto.WebsiteTagResponseDto;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import ru.d2k.parkle.entity.cache.WebsiteTagCache;

@Component
@Mapper(componentModel = "spring")
public abstract class WebsiteTagMapper {

    public abstract WebsiteTagResponseDto toResponseDto(WebsiteTagCache cache);
    public abstract WebsiteTagCache toCache(WebsiteTagEntity entity);
}
