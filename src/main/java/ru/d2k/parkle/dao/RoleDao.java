package ru.d2k.parkle.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dao.database.role.RoleDatabase;
import ru.d2k.parkle.dto.RoleCreateDto;
import ru.d2k.parkle.dto.RoleUpdateDto;
import ru.d2k.parkle.entity.RoleEntity;
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
    private final RoleDatabase database;

    @Autowired
    private final RoleMapper mapper;

    public RoleCache create(RoleCreateDto createRoleDto) {
        RoleEntity entityToCreate = RoleEntity.create(createRoleDto.name(), createRoleDto.priority());

        RoleEntity createdEntity = this.saveToDatabase(entityToCreate);

        return mapper.toCache(createdEntity);
    }

    // TODO: Придумать и реализовать поиск списка ролей (возможно, по срезу ключей).
    public Set<RoleCache> getAll() {
        List<RoleEntity> entities = this.getAllFromDatabase();

        return entities.stream()
                .map(mapper::toCache)
                .collect(Collectors.toSet());
    }

    // TODO: сделать поиск по ID (нюанс в том, что ключ состоит из Name).
    public Optional<RoleCache> getById(UUID id) {
        Optional<RoleEntity> entityFromDb = this.getByIdFromDatabase(id);

        if (entityFromDb.isPresent()) {
            log.debug("RoleEntity with id '{}' taken from database!", id);

            return Optional.of(mapper.toCache(entityFromDb.get()));
        }

        return Optional.empty();
    }

    public Optional<RoleCache> getByName(String name) {
        Optional<RoleEntity> entityFromDb = this.getByNameFromDatabase(name);

        if (entityFromDb.isPresent()) {
            RoleCache cacheFromEntity = mapper.toCache(entityFromDb.get());

            log.debug("RoleEntity with name '{}' taken from database!", name);

            return Optional.of(cacheFromEntity);
        }

        return Optional.empty();
    }

    public RoleEntity getReferenceById(UUID id) {
        return database.getReferenceById(id);
    }

    public Optional<RoleCache> updateById(UUID id, RoleUpdateDto updateRoleDto) {
        Optional<RoleEntity> entity = this.getByIdFromDatabase(id);

        if (entity.isPresent()) {
            mapper.updateEntityByDto(entity.get(), updateRoleDto);

            RoleEntity updatedEntity = this.saveToDatabase(entity.get());
            RoleCache cache = mapper.toCache(updatedEntity);

            return Optional.of(cache);
        }

        return Optional.empty();
    }

    public boolean deleteById(UUID id) {
        Optional<RoleEntity> entityToDelete = this.getByIdFromDatabase(id);

        if (entityToDelete.isPresent()) {
            this.deleteByIdFromDatabase(id);

            return !this.existsByIdInDatabase(id);
        }

        log.error("RoleEntity to delete with id '{}' not exist!", id);

        return true;
    }

    public boolean existsById(UUID id) {
        Optional<RoleCache> entityFromCache = this.getById(id);

        if (entityFromCache.isPresent()) return true;

        return this.existsByIdInDatabase(id);
    }

    public boolean existsByName(String name) {
        return database.existsByName(name);
    }

    private RoleEntity saveToDatabase(RoleEntity entity) {
        return database.save(entity);
    }

    private List<RoleEntity> getAllFromDatabase() {
        return database.getAll();
    }

    private Optional<RoleEntity> getByIdFromDatabase(UUID id) {
        return database.getById(id);
    }

    public Optional<RoleEntity> getByNameFromDatabase(String name) {
        return database.getByName(name);
    }

    private void deleteByIdFromDatabase(UUID id) {
        database.deleteById(id);
    }

    private boolean existsByIdInDatabase(UUID id) {
        return database.existsById(id);
    }
}