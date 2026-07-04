package ru.d2k.parkle.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dao.database.role.RoleDatabaseSource;
import ru.d2k.parkle.dto.RoleCreateDto;
import ru.d2k.parkle.dto.RoleUpdateDto;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.cache.RoleCache;
import ru.d2k.parkle.utils.mapper.RoleMapper;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleDao {

    @Autowired
    private final RoleDatabaseSource roleDatabase;

    @Autowired
    private final RoleMapper roleMapper;

    // CRUD.
    // Create.
    /**
     * Create and save role in database and cache.
     * @param dto {@link RoleCreateDto} object with info about new role.
     * @return {@link RoleCache} object of created role.
     * */
    public RoleCache create(RoleCreateDto dto) {
        Role entityToCreate = Role.create(dto.name(), dto.priority());

        Role createdEntity = this.saveToDatabase(entityToCreate);
        RoleCache cache = roleMapper.toCache(createdEntity);

        return cache;
    }

    // Read.
    // TODO: Придумать и реализовать поиск списка ролей (возможно, по срезу ключей).
    public Set<RoleCache> getAll() {
        List<Role> entities = this.getAllFromDatabase();

        return entities.stream()
                .map(roleMapper::toCache)
                .collect(Collectors.toSet());
    }

    /**
     * Get {@link Role} entity from database or cache by ID as {@link RoleCache}.
     * @param id ID of role.
     * @return entity as {@link RoleCache} object.
     * */
    // TODO: сделать поиск по ID (нюанс в том, что ключ состоит из Name).
    public Optional<RoleCache> getById(UUID id) {
        Optional<Role> entityFromDb = this.getFromDatabaseById(id);

        if (entityFromDb.isPresent()) {
            log.debug("Role with id '{}' taken from database!", id);

            return Optional.of(roleMapper.toCache(entityFromDb.get()));
        }

        return Optional.empty();
    }

    /**
     * Get {@link Role} entity from database or cache by name as {@link RoleCache}.
     * @param name name of role.
     * @return entity as {@link RoleCache} object.
     * */
    public Optional<RoleCache> getByName(String name) {
        Optional<Role> entityFromDb = this.getFromDatabaseByName(name);

        if (entityFromDb.isPresent()) {
            RoleCache cacheFromEntity = roleMapper.toCache(entityFromDb.get());

            log.debug("Role with name '{}' taken from database!", name);

            return Optional.of(cacheFromEntity);
        }

        return Optional.empty();
    }

    public Role getReferenceById(UUID id) {
        return roleDatabase.getReferenceById(id);
    }

    // Update.
    /**
     * Update {@link Role} entity in database and cache.
     * @param id ID of role.
     * @param udto {@link RoleUpdateDto} DTO for update entity.
     * @return updated entity as {@link RoleCache} object.
     * */
    public Optional<RoleCache> update(UUID id, RoleUpdateDto udto) {
        Optional<Role> entity = this.getFromDatabaseById(id);

        if (entity.isPresent()) {
            roleMapper.updateEntityByDto(entity.get(), udto);

            Role updatedEntity = this.saveToDatabase(entity.get());
            RoleCache cache = roleMapper.toCache(updatedEntity);

            return Optional.of(cache);
        }

        return Optional.empty();
    }

    // Delete.
    /**
     * Delete {@link Role} entity from database and cache by ID.
     * @param id ID of role.
     * @return is entity was deleted.
     * */
    public boolean deleteById(UUID id) {
        Optional<Role> entityToDelete = this.getFromDatabaseById(id);

        if (entityToDelete.isPresent()) {
            this.deleteFromDatabaseById(id);

            return !this.existInDatabaseById(id);
        }

        log.error("Role to delete with id '{}' not exist!", id);

        return true;
    }

    // Exists.
    /**
     * Check is exist {@link Role} in database by ID (ONLY DATABASE AS SOURCE OF TRUTH).
     * @param id ID of role.
     * @return is role exist in database.
     * */
    public boolean existsById(UUID id) {
        Optional<RoleCache> entityFromCache = this.getById(id);

        if (entityFromCache.isPresent()) return true;

        return this.existInDatabaseById(id);
    }

    /**
     * Check is exist {@link Role} in database by name (ONLY DATABASE AS SOURCE OF TRUTH).
     * @param name name of role.
     * @return is role exist in database.
     * */
    public boolean existsByName(String name) {
        return roleDatabase.existsByName(name);
    }

    private Role saveToDatabase(Role entity) {
        return roleDatabase.save(entity);
    }

    private List<Role> getAllFromDatabase() {
        return roleDatabase.getAll();
    }

    private Optional<Role> getFromDatabaseById(UUID id) {
        return roleDatabase.getById(id);
    }

    public Optional<Role> getFromDatabaseByName(String name) {
        return roleDatabase.getByName(name);
    }

    private void deleteFromDatabaseById(UUID id) {
        roleDatabase.deleteById(id);
    }

    private boolean existInDatabaseById(UUID id) {
        return roleDatabase.existsById(id);
    }
}