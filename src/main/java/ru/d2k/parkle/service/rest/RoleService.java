package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dto.RoleDto;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.exception.RoleNotFoundException;
import ru.d2k.parkle.repository.RoleRepository;
import ru.d2k.parkle.utils.mapper.RoleMapper;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    /**
     * Return all roles from DB as DTO.
     * @return Set of dto.
     * **/
    @Transactional(readOnly = true)
    public Set<RoleDto> findRoles() {
        log.info("Getting all roles...");

        Set<RoleDto> dtos = roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toSet());

        log.info("Roles was founded: {}", dtos.size());
        return dtos;
    }

    // TODO: Сделать своё исключение.
    /**
     * Return role by ID as DTO.
     * @param id ID of role.
     * @return {@link RoleDto} dto.
     * **/
    @Transactional(readOnly = true)
    public RoleDto findRoleById(UUID id) {
        log.info("Getting role by ID: {}...", id);

        Role role = roleRepository.findById(id)
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
    public RoleDto findRoleByName(String name) {
        log.info("Getting roles by Name: {}...", name);

        Role role = roleRepository.findByName(name)
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
    public RoleDto createRole(RoleDto dto) {
        log.info("Creating role: {}...", dto.toString());

        if (roleRepository.existsByName( dto.getName() )) {
            throw new IllegalArgumentException("Role with this name is already exists");
        }

        Role role = Role.create(dto.getName(), dto.getPriority());
        role = roleRepository.save(role);

        log.info("Role was created: {}", role);

        return roleMapper.toDto(role);
    }

    /**
     * Create new roles and return them as DTO List.
     * @param dtos list of new {@code RoleDto} to create.
     * @return List of {@link RoleDto} of roles which was saved as DTO.
     * **/
    @Transactional
    public List<RoleDto> createRoles(List<RoleDto> dtos) {
        log.info("Creating {} roles...", dtos.size());

        List<RoleDto> createdRoleDtos = new ArrayList<>();

        for (RoleDto dto : dtos) {
            if (roleRepository.existsByName( dto.getName() )) {
                throw new IllegalArgumentException("Role with this name is already exists");
            }

            Role role = Role.create(dto.getName(), dto.getPriority());
            role = roleRepository.save(role);
            RoleDto newRoleDto = roleMapper.toDto(role);

            createdRoleDtos.add(newRoleDto);
        }

        log.info("Roles was created: {}", createdRoleDtos.size());

        return createdRoleDtos;
    }

    /**
     * Update role.
     * @param dto {@code RoleDto} to update.
     * @return {@code RoleDto} role as DTO which was saved.
     * **/
    @Transactional
    public RoleDto updateRole( UUID id, RoleDto dto) {
        log.info("Updating role: {}...", dto.toString());

        if ( Objects.isNull(id) ) {
            throw new IllegalArgumentException("RoleDto ID is null");
        }

        Role role = roleRepository.findById(id).orElseThrow();
        role = roleMapper.updateEntityByDto(role, dto);
        Role updatedRole = roleRepository.save(role);

        log.info("Role was updated: {}", role);

        return roleMapper.toDto(updatedRole);
    }

    /**
     * Update roles and return them as DTO List.
     * @param dtos list of new {@code RoleDto} to save.
     * @return List of {@link RoleDto} of roles which was saved as DTO.
     * **/
    @Transactional
    public List<RoleDto> updateRoles(List<RoleDto> dtos) {
        log.info("Updating {} roles...", dtos.size());

        List<RoleDto> updatedRoleDtos = new ArrayList<>();

        for (RoleDto dto : dtos) {
            if ( Objects.isNull(dto.getId()) ) {
                throw new IllegalArgumentException("RoleDto ID is null");
            }

            Role role = roleRepository.findById(dto.getId()).orElseThrow();
            role = roleMapper.updateEntityByDto(role, dto);
            Role updatedRole = roleRepository.save(role);

            RoleDto updatedRoleDto = roleMapper.toDto(updatedRole);

            updatedRoleDtos.add( updatedRoleDto );
        }

        log.info("Roles was updated: {}", updatedRoleDtos.size());

        return updatedRoleDtos;
    }

    /**
     * Delete role by ID.
     * @param id role ID for delete.
     * **/
    @Transactional
    public boolean deleteRole(UUID id) {
        log.info("Delete role by ID: {}...", id);

        if (Objects.nonNull(id)) {

            if (!roleRepository.existsById(id)) {
                log.info("Role with ID = {} is already not exists", id);
                return false;
            }

            roleRepository.deleteById(id);

            if (!roleRepository.existsById(id)) {
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
