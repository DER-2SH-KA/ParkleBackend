package ru.d2k.parkle.utils.converter;

public interface DtoConverter<ENTITY, DTO> {
    DTO toDto(ENTITY entity);
    ENTITY toNewEntity(DTO dto);
    ENTITY updateEntityByDto(ENTITY entity, DTO dto);
}
