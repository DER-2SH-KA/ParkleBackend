package ru.d2k.parkle.utils.mapper;

import org.mapstruct.*;
import ru.d2k.parkle.dto.WebsiteResponseDto;
import ru.d2k.parkle.dto.WebsiteUpdateDto;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.entity.Website;
import ru.d2k.parkle.entity.cache.WebsiteCache;

@Mapper(componentModel = "spring")
public interface WebsiteMapper {

    @Mapping(target = "userLogin", source = "user.login")
    WebsiteResponseDto toResponseDto(Website dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "dto.url", target = "url")
    @Mapping(
            target = "description",
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.SET_TO_NULL
    )
    Website updateByDto(@MappingTarget Website entity, WebsiteUpdateDto dto, User user);

    /**
     * CACHE => DTO
     * */
    @Mapping(target = "userLogin", source = "userLogin")
    WebsiteResponseDto toResponseDto(WebsiteCache cache);

    /**
     * ENTITY => CACHE
     * */
    @Mapping(target = "userLogin", source = "user.login")
    @Mapping(target = "userId", source = "user.id")
    WebsiteCache toCache(Website entity);
}
