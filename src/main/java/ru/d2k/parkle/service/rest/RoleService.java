package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.ArrayUtils;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.repository.RoleRepository;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    /**
     * Return all roles.
     * @return List of roles.
     * **/
    public List<Role> findRoles() {
        log.info("Find all roles...");
        return roleRepository.findAll();
    }

    /**
     * Return role by ID.
     * @param id ID of role.
     * @return {@code Role} role.
     * **/
    public Role findRoleById(Integer id) {
        log.info("Find role by ID: {}...", id);
        return roleRepository.findById(id).orElseThrow();
    }

    /**
     * Return roles by name.
     * @param name Name of role.
     * @return {@code List<Role>} founded roles.
     * **/
    public List<Role> findRolesByName(String name) {
        log.info("Find roles by Name: {}...", name);
        return roleRepository.findRolesByName(name);
    }

    /**
     * Create new role.
     * @param newRole new {@code Role} to save.
     * @return {@code Role} which was saved.
     * **/
    public Role saveRole(Role newRole) {
        log.info("Save role: {}...", newRole.toString());
        return roleRepository.save(newRole);
    }

    /**
     * Create new roles.
     * @param newRoles list of new {@code Role} to save.
     * @return {@code List<Role>} which was saved.
     * **/
    public List<Role> saveRoles(List<Role> newRoles) {
        log.info("Save {} roles...", newRoles.size());
        return roleRepository.saveAll(newRoles);
    }

    /**
     * Delete role.
     * @param delRole role to delete.
     * **/
    public void deleteRole(Role delRole) {
        log.info("Delete role: {}...", delRole.toString());
        roleRepository.delete(delRole);
    }

    /**
     * Delete roles.
     * @param delRoles roles to delete.
     * **/
    public void deleteRoles(List<Role> delRoles) {
        log.info("Delete {} roles...", delRoles.size());
        roleRepository.deleteAll(delRoles);
    }
}
