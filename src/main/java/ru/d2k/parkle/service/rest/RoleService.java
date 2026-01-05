package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.RoleDao;
import ru.d2k.parkle.dto.RoleDto;
import ru.d2k.parkle.entity.Role;
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

    /**
     * Return all roles from DB as DTO.
     * @return Set of dto.
     * **/
    @Transactional(readOnly = true)
    public Set<RoleDto> findAll() {
        log.info("Getting all roles...");

        Set<RoleDto> dtos = roleDao.getAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toSet());

        log.info("Roles was founded: {}", dtos.size());
        return dtos;
    }

    /**
     * Return role by ID as DTO.
     * @param id ID of role.
     * @return {@link RoleDto} dto.
     * **/
    @Transactional(readOnly = true)
    public RoleDto findById(UUID id) {
        log.info("Getting role by ID: {}...", id);

        Role role = roleDao.getById(id)
                .orElseThrow(() ->
                        new RoleNotFoundException("Role was not found with ID: " + id)
        );

        log.info("Role with ID = {} was founded", id);
        return roleMapper.toDto(role);
    }

    /**
     * Return role as {@link RoleDto} by name.
     * @param name Name of role.
     * @return {@link RoleDto} object.
     * **/
    @Transactional(readOnly = true)
    public RoleDto findByName(String name) {
        log.info("Getting roles by Name: {}...", name);

        Role role = roleDao.getByName(name)
                .orElseThrow(() ->
                        new RoleNotFoundException("Role was not found with name: " + name)
                );

        log.info("Role with name = {} was founded", name);

        return roleMapper.toDto(role);
    }

    /**
     * Create new role.
     * @param dto new {@code RoleDto} to create.
     * @return {@code RoleDto} role as DTO which was saved.
     * **/
    @Transactional
    public RoleDto create(RoleDto dto) {
        log.info("Creating role: {}...", dto.toString());

        if (roleDao.existByName( dto.name() )) {
            throw new IllegalArgumentException("Role with this name is already exists");
        }

        Role role = Role.create(dto.name(), dto.priority());
        role = roleDao.create(role);

        log.info("Role was created: {}", role);

        return roleMapper.toDto(role);
    }

    /**
     * Update role.
     * @param dto {@code RoleDto} to update.
     * @return {@code RoleDto} role as DTO which was saved.
     * **/
    @Transactional
    public RoleDto update(UUID id, RoleDto dto) {
        log.info("Updating role: {}...", dto);

        if ( Objects.isNull(id) ) {
            throw new IllegalArgumentException("RoleDto ID is null");
        }

        Role role = roleDao.getById(id)
                .orElseThrow(
                        () -> new RoleNotFoundException("Role with this ID is not exist!")
                );
        roleMapper.updateEntityByDto(role, dto);
        Role updatedRole = roleDao.update(id, role);

        log.info("Role was updated: {}", role);

        return roleMapper.toDto(updatedRole);
    }

    /**
     * Delete role by ID.
     * @param id role ID for delete.
     * **/
    @Transactional
    public boolean delete(UUID id) {
        log.info("Delete role by ID: {}...", id);

        if (Objects.nonNull(id)) {

            Optional<Role> existEntity = roleDao.getById(id);

            if (existEntity.isEmpty()) {
                log.info("Role with ID = {} is already not exist", id);
                return false;
            }

            roleDao.delete(existEntity.get());

            if (roleDao.existByName(existEntity.get().getName())) {
                log.info("Role by ID {} was deleted", id);
                return true;
            }
            else {
                log.info("Role by ID {} wasn't deleted", id);
            }
        }
        else { log.info("Role's ID equals null and now was deleted"); }

        return false;
    }
}
