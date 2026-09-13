package ru.d2k.parkle.service.rest;

import ru.d2k.parkle.dto.RoleCreateDto;
import ru.d2k.parkle.dto.RoleResponseDto;
import ru.d2k.parkle.dto.RoleUpdateDto;
import java.util.Set;
import java.util.UUID;

public interface RoleService {

    Set<RoleResponseDto> findAll();
    RoleResponseDto findById(UUID id);
    RoleResponseDto findByName(String name);
    RoleResponseDto create(RoleCreateDto createRoleDto);
    RoleResponseDto updateById(UUID id, RoleUpdateDto updateRoleDto);
    boolean deleteById(UUID id);
}