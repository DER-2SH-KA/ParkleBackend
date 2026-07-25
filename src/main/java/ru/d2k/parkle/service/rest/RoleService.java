package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.RoleDao;
import ru.d2k.parkle.dto.RoleCreateDto;
import ru.d2k.parkle.dto.RoleResponseDto;
import ru.d2k.parkle.dto.RoleUpdateDto;
import ru.d2k.parkle.entity.cache.RoleCache;
import ru.d2k.parkle.exception.RoleNotFoundException;
import ru.d2k.parkle.utils.mapper.RoleMapper;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleService {

    @Autowired
    private final RoleDao dao;

    @Autowired
    private final RoleMapper mapper;

    @Transactional(readOnly = true)
    public Set<RoleResponseDto> findAll() {
        log.info("Getting all roles...");

        Set<RoleCache> roleCaches = dao.getAll();

        log.info("Roles was founded: {}", roleCaches.size());

        return roleCaches.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public RoleResponseDto findById(UUID id) {
        log.info("Getting role by ID: {}...", id);

        RoleCache roleCache = dao.getById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role was not found with ID: " + id));

        log.info("Role with ID {} was founded", id);

        return mapper.toResponseDto(roleCache);
    }

    @Transactional(readOnly = true)
    public RoleResponseDto findByName(String name) {
        log.info("Getting roles by Name '{}'...", name);

        RoleCache roleCache = dao.getByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Role was not found with name: " + name));

        log.info("Role with name '{}' was founded", name);

        return mapper.toResponseDto(roleCache);
    }

    @Transactional
    public RoleResponseDto create(RoleCreateDto cdto) {
        log.info("Creating role '{}'...", cdto.toString());

        if (dao.existsByName(cdto.name())) {
            throw new IllegalArgumentException("Role with this name is already exists");
        }

        RoleCache role = dao.create(cdto);

        log.info("Role '{}' was created", role.name());

        return mapper.toResponseDto(role);
    }

    @Transactional
    public RoleResponseDto update(UUID id, RoleUpdateDto udto) {
        log.info("Updating role by id '{}'...", id);

        if (id == null) {
            throw new IllegalArgumentException("RoleUpdateDto ID is null");
        }

        RoleCache updatedRole = dao.updateById(id, udto)
                .orElseThrow(() -> new RoleNotFoundException("Role with this ID is not exist!"));

        log.info("Role with id {} was updated to {}", id, updatedRole);

        return mapper.toResponseDto(updatedRole);
    }

    @Transactional
    public boolean delete(UUID id) {
        log.info("Delete role by ID {}...", id);

        if (id != null) {
            boolean isDeleted = dao.deleteById(id);

            if (isDeleted) {
                log.info("Role by ID {} was deleted", id);

                return true;
            } else {
                log.error("Role by ID {} wasn't deleted", id);
            }
        } else {
            log.error("Role's ID equals null");
        }

        return false;
    }
}