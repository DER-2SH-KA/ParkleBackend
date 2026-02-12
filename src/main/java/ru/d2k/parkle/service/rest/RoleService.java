package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.RoleDao;
import ru.d2k.parkle.dto.RoleCreateDto;
import ru.d2k.parkle.dto.RoleResponseDto;
import ru.d2k.parkle.dto.RoleUpdateDto;
import ru.d2k.parkle.entity.cache.RoleCache;
import ru.d2k.parkle.exception.RoleNotFoundException;
import ru.d2k.parkle.utils.mapper.RoleMapper;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleDao roleDao;
    private final RoleMapper roleMapper;

    @Transactional(readOnly = true)
    public Set<RoleResponseDto> findAll() {
        log.info("Getting all roles...");

        Set<RoleCache> dtos = roleDao.getAll();

        log.info("Roles was founded: {}", dtos.size());
        return dtos.stream()
                .map(roleMapper::toResponseDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public RoleResponseDto findById(UUID id) {
        log.info("Getting role by ID: {}...", id);

        RoleCache role = roleDao.getById(id)
                .orElseThrow(() ->
                        new RoleNotFoundException("Role was not found with ID: " + id)
        );

        log.info("Role with ID {} was founded", id);
        return roleMapper.toResponseDto(role);
    }

    @Transactional(readOnly = true)
    public RoleResponseDto findByName(String name) {
        log.info("Getting roles by Name '{}'...", name);

        RoleCache role = roleDao.getByName(name)
                .orElseThrow(() ->
                        new RoleNotFoundException("Role was not found with name: " + name)
                );

        log.info("Role with name '{}' was founded", name);

        return roleMapper.toResponseDto(role);
    }

    @Transactional
    public RoleResponseDto create(RoleCreateDto cdto) {
        log.info("Creating role '{}'...", cdto.toString());

        if (roleDao.existsByName( cdto.name() )) {
            throw new IllegalArgumentException("Role with this name is already exists");
        }

        RoleCache role = roleDao.create(cdto);

        log.info("Role '{}' was created", role.name());

        return roleMapper.toResponseDto(role);
    }

    @Transactional
    public RoleResponseDto update(UUID id, RoleUpdateDto udto) {
        log.info("Updating role by id '{}'...", id);

        if ( Objects.isNull(id) ) {
            throw new IllegalArgumentException("RoleUpdateDto ID is null");
        }

        RoleCache updatedRole = roleDao.update(id, udto)
                .orElseThrow(
                () -> new RoleNotFoundException("Role with this ID is not exist!")
        );

        log.info("Role with id {} was updated to {}", id, updatedRole);
        return roleMapper.toResponseDto(updatedRole);
    }

    @Transactional
    public boolean delete(UUID id) {
        log.info("Delete role by ID {}...", id);

        if (Objects.nonNull(id)) {

            boolean isDeleted = roleDao.deleteById(id);

            if (isDeleted) {
                log.info("Role by ID {} was deleted", id);
                return true;
            }
            else {
                log.error("Role by ID {} wasn't deleted", id);
            }
        }
        else {
            log.error("Role's ID equals null");
        }

        return false;
    }
}
