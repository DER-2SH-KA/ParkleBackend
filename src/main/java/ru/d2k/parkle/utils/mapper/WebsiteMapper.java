package ru.d2k.parkle.utils.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.d2k.parkle.dto.WebsiteResponseDto;
import ru.d2k.parkle.dto.WebsiteUpdateDto;
import ru.d2k.parkle.entity.UserEntity;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.entity.cache.WebsiteCache;

@Mapper(componentModel = "spring")
public interface WebsiteMapper {

    @Mapping(target = "userLogin", source = "user.login")
    WebsiteResponseDto toResponseDto(WebsiteEntity dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "dto.url", target = "url")
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void updateEntityByDto(@MappingTarget WebsiteEntity entity, WebsiteUpdateDto dto, UserEntity user);

    @Mapping(target = "userLogin", source = "userLogin")
    WebsiteResponseDto toResponseDto(WebsiteCache cache);

    @Mapping(target = "userLogin", source = "user.login")
    @Mapping(target = "userId", source = "user.id")
    WebsiteCache toCache(WebsiteEntity entity);
}