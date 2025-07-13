package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.repository.RoleRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleService {
    @Autowired
    private final RoleRepository ROLE_REPOSITORY;

    /**
     * Return all roles.
     * @return List of roles.
     * **/
    public List<Role> findRoles() {
        return ROLE_REPOSITORY.findAll();
    }

    /**
     * Return role by ID.
     * @param id ID of role.
     * @return {@code Role} role.
     * **/
    public Optional<Role> findRoleById(Integer id) {
        Optional<Role> roleById = Optional.empty();

        try {
            roleById = ROLE_REPOSITORY.findById(id);
        }
        catch (Exception ex) {
            log.error("Exception in class: {}", this.getClass().getName(), ex);
        }

        return roleById;
    }

    /**
     * Return roles by name.
     * @param name Name of role.
     * @return {@code List<Role>} founded roles.
     * **/
    public Optional<Map<Integer, Role>> findRolesByName(String name) {
        Map<Integer, Role> rolesByName = null;

        try {
            rolesByName = ROLE_REPOSITORY.findRolesByName(name);
        }
        catch (Exception ex) {
            log.error("Exception in class: {}", this.getClass().getName(), ex);
        }

        return Optional.ofNullable(rolesByName);
    }

    /**
     * Create new role.
     * @param newRole new {@code Role} to save.
     * @return {@code Optional<Role>} which was saved.
     * **/
    public Optional<Role> saveRole(Role newRole) {
        Role savedRole = null;

        try {
            savedRole = ROLE_REPOSITORY.save(newRole);
        }
        catch (Exception ex) {
            log.error("Exception in class: {}", this.getClass().getName(), ex);
        }

        return Optional.ofNullable(savedRole);
    }

    /**
     * Create new roles.
     * @param newRoles list of new {@code Role} to save.
     * @return {@code Optional<Role>} which was saved.
     * **/
    public Optional<List<Role>> saveRoles(List<Role> newRoles) {
        List<Role> savedRoles = null;

        try {
            savedRoles = ROLE_REPOSITORY.saveAll(newRoles);
        }
        catch (Exception ex) {
            log.error("Exception in class: {}", this.getClass().getName(), ex);
        }

        return Optional.ofNullable(savedRoles);
    }

    /**
     * Delete role.
     * @param delRole role to delete.
     * **/
    public void deleteRole(Role delRole) {
        try {
            ROLE_REPOSITORY.delete(delRole);
        }
        catch (Exception ex) {
            log.error("Exception in class: {}", this.getClass().getName(), ex);
        }
    }

    /**
     * Delete roles.
     * @param delRoles roles to delete.
     * **/
    public void deleteRoles(List<Role> delRoles) {
        try {
            ROLE_REPOSITORY.deleteAll(delRoles);
        }
        catch (Exception ex) {
            log.error("Exception in class: {}", this.getClass().getName(), ex);
        }
    }
}
