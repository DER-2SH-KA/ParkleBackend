package ru.d2k.parkle.dto;

public interface DtoConverterImpl<ENTITY, DTO> {
    DTO toDto(ENTITY entity);
    ENTITY toNewEntity(DTO dto);
    ENTITY updateEntityByDto(ENTITY entity, DTO dto);
}
