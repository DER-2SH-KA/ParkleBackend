package ru.d2k.parkle.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dto.RoleCreateDto;
import ru.d2k.parkle.dto.RoleUpdateDto;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.cache.RoleCache;
import ru.d2k.parkle.redis.RedisCacheKeys;
import ru.d2k.parkle.repository.RoleRepository;
import ru.d2k.parkle.utils.mapper.RoleMapper;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleDao {
    private final RoleRepository roleRepository;
    private final RedisTemplate<String, RoleCache> redisRoleTemplate;
    private final RoleMapper roleMapper;

    // CRUD.
    // Create.
    public RoleCache create(RoleCreateDto dto) {
        Role entityToCreate = Role.create(dto.name(), dto.priority());

        Role createdEntity = roleRepository.save(entityToCreate);

        RoleCache cache = roleMapper.toCache(createdEntity);

        redisRoleTemplate.opsForValue().set(
                RedisCacheKeys.ROLE_SLICE_KEY + cache.name(),
                cache,
                Duration.ofMinutes(15)
        );

        return cache;
    }

    // Read.
    // TODO: Придумать и реализовать поиск списка ролей (возможно, по срезу ключей).
    public Set<RoleCache> getAll() {
        List<Role> entities = roleRepository.findAll();

        return entities.stream()
                .map(roleMapper::toCache)
                .collect(Collectors.toSet());
    }

    // TODO: сделать поиск по ID (нюанс в том, что ключ состоит из Name).
    public Optional<RoleCache> getById(UUID id) {
        Optional<Role> entityFromDb = roleRepository.findById(id);

        if (entityFromDb.isPresent()) {
            return Optional.of(roleMapper.toCache(entityFromDb.get()));
        }

        return Optional.empty();
    }

    public Optional<RoleCache> getByName(String name) {
        Optional<RoleCache> cache = Optional.ofNullable(
                redisRoleTemplate
                        .opsForValue()
                        .get(RedisCacheKeys.ROLE_SLICE_KEY + name)
        );

        if (cache.isPresent()) {
            log.info("Role with name '{}' taken from cache!", name);
            return cache;
        }
        else {
            Optional<Role> entityFromDb = roleRepository.findByName(name);

            if (entityFromDb.isPresent()) {
                RoleCache cacheFromEntity = roleMapper.toCache(entityFromDb.get());

                redisRoleTemplate.opsForValue().set(
                        RedisCacheKeys.ROLE_SLICE_KEY + cacheFromEntity.name(),
                        cacheFromEntity,
                        Duration.ofMinutes(15)
                );

                log.info("Role with name '{}' taken from database!", name);
                return Optional.of(cacheFromEntity);
            }
        }

        return Optional.empty();
    }

    // Update.
    public Optional<RoleCache> update(UUID id, RoleUpdateDto udto) {
        Optional<Role> entity = roleRepository.findById(id);

        if (entity.isPresent()) {
            roleMapper.updateEntityByDto(entity.get(), udto);

            Role updatedEntity = roleRepository.save(entity.get());
            RoleCache cache = roleMapper.toCache(updatedEntity);

            // Delete old entity information from Redis cache.
            redisRoleTemplate.opsForValue().getAndDelete(RedisCacheKeys.ROLE_SLICE_KEY + udto.name());

            redisRoleTemplate.opsForValue().set(
                    RedisCacheKeys.ROLE_SLICE_KEY + cache.name(),
                    cache,
                    Duration.ofMinutes(15)
            );

            return Optional.of(cache);
        }

        return Optional.empty();
    }

    // Delete.
    public boolean delete(UUID id) {
        Optional<Role> entityToDelete = roleRepository.findById(id);

        if (entityToDelete.isPresent()) {
            roleRepository.deleteById(id);
            redisRoleTemplate.opsForValue().getAndDelete(RedisCacheKeys.ROLE_SLICE_KEY + entityToDelete.get().getName());

            return !roleRepository.existsById(id);
        }

        return true;
    }

    // Exists.
    public boolean existById(UUID id) {
        Optional<RoleCache> entityFromCache = this.getById(id);

        if (entityFromCache.isPresent()) return true;
        else {
            return roleRepository.existsById(id);
        }
    }

    public boolean existByName(String name) {
        Optional<RoleCache> entityFromCache = this.getByName(name);

        if (entityFromCache.isPresent()) return true;
        else {
            return roleRepository.existsByName(name);
        }
    }
}
