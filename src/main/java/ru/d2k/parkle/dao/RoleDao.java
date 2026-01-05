package ru.d2k.parkle.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.exception.EntityAlreadyExistException;
import ru.d2k.parkle.exception.RoleNotFoundException;
import ru.d2k.parkle.redis.RedisCacheKeys;
import ru.d2k.parkle.repository.RoleRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoleDao {
    private final RoleRepository roleRepository;
    private final RedisTemplate<String, Role> redisRoleTemplate;

    // CRUD.
    // Create.
    public Role create(Role newEntity) {
        Role createdEntity = roleRepository.save(newEntity);

        redisRoleTemplate.opsForValue().set(
                RedisCacheKeys.ROLE_SLICE_KEY + createdEntity.getName(),
                createdEntity,
                Duration.ofMinutes(15)
        );

        return createdEntity;
    }

    // Read.
    // TODO: Придумать и реализовать поиск списка ролей (возможно, по срезу ключей).
    public List<Role> getAll() {
        List<Role> entities = new ArrayList<>();

        entities = roleRepository.findAll();

        return entities;
    }

    // TODO: сделать поиск по ID (нюанс в том, что ключ состоит из Name).
    public Optional<Role> getById(UUID id) {
        Optional<Role> entityFromDb = roleRepository.findById(id);

        return entityFromDb;
    }

    public Optional<Role> getByName(String name) {
        Optional<Role> entityFromCache = Optional
                .ofNullable(redisRoleTemplate.opsForValue().get(RedisCacheKeys.ROLE_SLICE_KEY + name));

        if (entityFromCache.isPresent()) {
            return entityFromCache;
        }
        else {
            Optional<Role> entityFromDb = roleRepository.findByName(name);

            if (entityFromDb.isPresent()) {
                Role entity = entityFromDb.get();

                redisRoleTemplate.opsForValue().set(
                        RedisCacheKeys.ROLE_SLICE_KEY + entity.getName(),
                        entity,
                        Duration.ofMinutes(15)
                );
            }

            return entityFromDb;
        }
    }

    // Update.
    public Role update(Role oldEntity, Role modifiedEntity) {
        Role updatedEntity = roleRepository.save(modifiedEntity);

        // Delete old entity information from Redis cache.
        redisRoleTemplate.opsForValue().getAndDelete(RedisCacheKeys.ROLE_SLICE_KEY + oldEntity.getName());

        redisRoleTemplate.opsForValue().set(
                RedisCacheKeys.ROLE_SLICE_KEY + updatedEntity.getName(),
                updatedEntity,
                Duration.ofMinutes(15)
        );

        return updatedEntity;
    }

    // Delete.
    public boolean delete(Role entityToDelete) {
        roleRepository.deleteById(entityToDelete.getId());
        redisRoleTemplate.opsForValue().getAndDelete(RedisCacheKeys.ROLE_SLICE_KEY + entityToDelete.getName());

        return !roleRepository.existsById(entityToDelete.getId());
    }
}
