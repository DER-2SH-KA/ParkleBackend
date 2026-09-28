package ru.d2k.parkle.service.rest.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.RoleDao;
import ru.d2k.parkle.dto.RoleCreateDto;
import ru.d2k.parkle.dto.RoleResponseDto;
import ru.d2k.parkle.dto.RoleUpdateDto;
import ru.d2k.parkle.entity.cache.RoleCache;
import ru.d2k.parkle.exception.entity.RoleEntityNotFoundException;
import ru.d2k.parkle.service.rest.RoleService;
import ru.d2k.parkle.utils.mapper.RoleMapper;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao dao;
    private final RoleMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Set<RoleResponseDto> findAll() {
        log.debug("Getting all roles...");

        Set<RoleCache> roleCaches = dao.getAll();

        log.debug("Roles was founded: {}", roleCaches.size());

        return roleCaches.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDto findById(UUID id) {
        log.debug("Getting role by ID: {}...", id);

        RoleCache roleCache = dao.getById(id)
                .orElseThrow(() -> new RoleEntityNotFoundException("RoleEntity was not found with ID: " + id));

        log.debug("RoleEntity with ID {} was founded", id);

        return mapper.toResponseDto(roleCache);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDto findByName(String name) {
        log.debug("Getting roles by Name '{}'...", name);

        RoleCache roleCache = dao.getByName(name)
                .orElseThrow(() -> new RoleEntityNotFoundException("RoleEntity was not found with name: " + name));

        log.debug("RoleEntity with name '{}' was founded", name);

        return mapper.toResponseDto(roleCache);
    }

    @Override
    @Transactional
    public RoleResponseDto create(RoleCreateDto createRoleDto) {
        log.debug("Creating role '{}'...", createRoleDto.toString());

        if (dao.existsByName(createRoleDto.name())) {
            throw new IllegalArgumentException("RoleEntity with this name is already exists");
        }

        RoleCache role = dao.create(createRoleDto);

        log.debug("RoleEntity '{}' was created", role.name());

        return mapper.toResponseDto(role);
    }

    @Override
    @Transactional
    public RoleResponseDto updateById(RoleUpdateDto updateRoleDto) {
        log.debug("Updating role by id '{}'...", updateRoleDto.id());

        RoleCache updatedRole = dao.updateById(updateRoleDto.id(), updateRoleDto);

        log.debug("RoleEntity with id {} was updated to {}", updateRoleDto.id(), updatedRole);

        return mapper.toResponseDto(updatedRole);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        log.debug("Delete role by ID {}...", id);

        if (id != null) {
            dao.deleteById(id);
        } else {
            log.error("RoleEntity's ID equals null");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return dao.existsById(id);
    }
}