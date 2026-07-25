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
@RequiredArgsConstructor
@Component
public class RoleDao {

    @Autowired
    private final RoleDatabaseSource database;

    @Autowired
    private final RoleMapper mapper;

    public RoleCache create(RoleCreateDto createRoleDto) {
        Role entityToCreate = Role.create(createRoleDto.name(), createRoleDto.priority());

        Role createdEntity = this.saveToDatabase(entityToCreate);

        return mapper.toCache(createdEntity);
    }

    // TODO: Придумать и реализовать поиск списка ролей (возможно, по срезу ключей).
    public Set<RoleCache> getAll() {
        List<Role> entities = this.getAllFromDatabase();

        return entities.stream()
                .map(mapper::toCache)
                .collect(Collectors.toSet());
    }

    // TODO: сделать поиск по ID (нюанс в том, что ключ состоит из Name).
    public Optional<RoleCache> getById(UUID id) {
        Optional<Role> entityFromDb = this.getFromDatabaseById(id);

        if (entityFromDb.isPresent()) {
            log.debug("Role with id '{}' taken from database!", id);

            return Optional.of(mapper.toCache(entityFromDb.get()));
        }

        return Optional.empty();
    }

    public Optional<RoleCache> getByName(String name) {
        Optional<Role> entityFromDb = this.getFromDatabaseByName(name);

        if (entityFromDb.isPresent()) {
            RoleCache cacheFromEntity = mapper.toCache(entityFromDb.get());

            log.debug("Role with name '{}' taken from database!", name);

            return Optional.of(cacheFromEntity);
        }

        return Optional.empty();
    }

    public Role getReferenceById(UUID id) {
        return database.getReferenceById(id);
    }

    public Optional<RoleCache> updateById(UUID id, RoleUpdateDto updateRoleDto) {
        Optional<Role> entity = this.getFromDatabaseById(id);

        if (entity.isPresent()) {
            mapper.updateEntityByDto(entity.get(), updateRoleDto);

            Role updatedEntity = this.saveToDatabase(entity.get());
            RoleCache cache = mapper.toCache(updatedEntity);

            return Optional.of(cache);
        }

        return Optional.empty();
    }

    public boolean deleteById(UUID id) {
        Optional<Role> entityToDelete = this.getFromDatabaseById(id);

        if (entityToDelete.isPresent()) {
            this.deleteFromDatabaseById(id);

            return !this.existInDatabaseById(id);
        }

        log.error("Role to delete with id '{}' not exist!", id);

        return true;
    }

    public boolean existsById(UUID id) {
        Optional<RoleCache> entityFromCache = this.getById(id);

        if (entityFromCache.isPresent()) return true;

        return this.existInDatabaseById(id);
    }

    public boolean existsByName(String name) {
        return database.existsByName(name);
    }

    private Role saveToDatabase(Role entity) {
        return database.save(entity);
    }

    private List<Role> getAllFromDatabase() {
        return database.getAll();
    }

    private Optional<Role> getFromDatabaseById(UUID id) {
        return database.getById(id);
    }

    public Optional<Role> getFromDatabaseByName(String name) {
        return database.getByName(name);
    }

    private void deleteFromDatabaseById(UUID id) {
        database.deleteById(id);
    }

    private boolean existInDatabaseById(UUID id) {
        return database.existsById(id);
    }
}