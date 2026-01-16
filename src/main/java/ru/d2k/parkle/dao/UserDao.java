package ru.d2k.parkle.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dto.RoleUpdateDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.entity.cache.RoleCache;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.redis.RedisCacheKeys;
import ru.d2k.parkle.repository.UserRepository;
import ru.d2k.parkle.utils.mapper.UserMapper;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDao {
    private final UserRepository userRepository;
    private final RedisTemplate<String, UserCache> redisUserTemplate;
    private final UserMapper userMapper;

    // CRUD.
    // Create.
    public UserCache create(User entity) {
        User createdEntity = this.saveToDatabase(entity);
        UserCache cache = userMapper.toCache(createdEntity);

        this.setToCache(
                RedisCacheKeys.USER_SLICE_KEY + cache.login(),
                cache,
                Duration.ofHours(1)
        );

        return cache;
    }

    // Read.
    // TODO: Придумать и реализовать поиск списка пользователей (возможно, по срезу ключей).
    public Set<UserCache> getAll() {
        List<User> entities = this.getAllFromDatabase();

        return entities.stream()
                .map(userMapper::toCache)
                .collect(Collectors.toSet());
    }

    /**
     * Get {@link User} entity from database or cache by ID as {@link UserCache}.
     * @param id ID of user.
     * @return entity as {@link UserCache} object.
     * */
    // TODO: сделать поиск по ID (нюанс в том, что ключ состоит из Name).
    public Optional<UserCache> getById(UUID id) {
        Optional<User> entityFromDb = this.getFromDatabaseById(id);

        if (entityFromDb.isPresent()) {
            return Optional.of(userMapper.toCache(entityFromDb.get()));
        }

        return Optional.empty();
    }

    /**
     * Get {@link User} entity from database or cache by name as {@link UserCache}.
     * @param login login of user.
     * @return entity as {@link UserCache} object.
     * */
    public Optional<UserCache> getByLogin(String login) {
        Optional<UserCache> cache = this.getFromCache(RedisCacheKeys.USER_SLICE_KEY + login);

        if (cache.isPresent()) {
            log.info("User with login '{}' taken from cache!", login);
            return cache;
        }
        else {
            Optional<User> entityFromDb = this.getFromDatabaseByLogin(login);

            if (entityFromDb.isPresent()) {
                UserCache cacheFromEntity = userMapper.toCache(entityFromDb.get());

                this.setToCache(
                        RedisCacheKeys.USER_SLICE_KEY + cacheFromEntity.login(),
                        cacheFromEntity,
                        Duration.ofHours(1)
                );

                log.info("User with login '{}' taken from database!", login);
                return Optional.of(cacheFromEntity);
            }
        }

        return Optional.empty();
    }

    public User getReferenceById(UUID id) {
        return userRepository.getReferenceById(id);
    }

    // Update.
    /**
     * Update {@link User} entity in database and cache.
     * @param login user's login.
     * @param udto {@link UserUpdateDto} DTO for update entity.
     * @return updated entity as {@link UserCache} object.
     * */
    // TODO: Не обновляется роль. Роль сохраняется не в захэшированном виде.
    public Optional<UserCache> update(String login, UserUpdateDto udto) {
        Optional<User> entity = this.getFromDatabaseByLogin(login);

        if (entity.isPresent()) {
            userMapper.updateByDto(entity.get(), udto, entity.get().getRole());

            User updatedEntity = this.saveToDatabase(entity.get());
            UserCache cache = userMapper.toCache(updatedEntity);

            // Delete old entity information from Redis cache.
            this.deleteFromCache(RedisCacheKeys.USER_SLICE_KEY + entity.get().getLogin());
            // На всякий случай и по ID.
            this.deleteFromCache(RedisCacheKeys.USER_SLICE_KEY + entity.get().getId());

            this.setToCache(
                    RedisCacheKeys.USER_SLICE_KEY + entity.get().getLogin(),
                    cache,
                    Duration.ofHours(1)
            );

            return Optional.of(cache);
        }

        return Optional.empty();
    }

    // Delete.
    /**
     * Delete {@link User} entity from database and cache by ID.
     * @param login user's login.
     * @return is entity was deleted.
     * */
    public boolean deleteByLogin(String login) {
        Optional<User> entityToDelete = this.getFromDatabaseByLogin(login);

        if (entityToDelete.isPresent()) {
            this.deleteFromCache(RedisCacheKeys.USER_SLICE_KEY + entityToDelete.get().getLogin());
            // На всякий случай и по ID.
            this.deleteFromCache(RedisCacheKeys.USER_SLICE_KEY + entityToDelete.get().getId());

            this.deleteFromDatabaseByLogin(login);

            return !this.existInDatabaseByLogin(login);
        }

        return true;
    }

    /**
     * Check is exist {@link User} in database by name (ONLY DATABASE AS SOURCE OF TRUTH).
     * @param login user's login.
     * @return is user exist in database.
     * */
    public boolean existsByLogin(String login) {
        return this.userRepository.existsByLogin(login);
    }

    private void setToCache(String key, UserCache value, Duration duration) {
        redisUserTemplate.opsForValue().set(key, value, duration);
    }

    private Optional<UserCache> getFromCache(String key) {
        return Optional.ofNullable(redisUserTemplate.opsForValue().get(key));
    }

    private void deleteFromCache(String key) { redisUserTemplate.delete(key); }

    private boolean existInCache(String key) {
        return this.getFromCache(key).isPresent();
    }

    private User saveToDatabase(User entity) {
        return userRepository.save(entity);
    }

    private List<User> getAllFromDatabase() {
        return userRepository.findAll();
    }

    private Optional<User> getFromDatabaseById(UUID id) {
        return userRepository.findById(id);
    }

    private Optional<User> getFromDatabaseByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    private void deleteFromDatabaseById(UUID id) {
        userRepository.deleteById(id);
    }

    private void deleteFromDatabaseByLogin(String login) {
        userRepository.deleteByLogin(login);
    }

    private boolean existInDatabaseById(UUID id) {
        return userRepository.existsById(id);
    }

    private boolean existInDatabaseByLogin(String login) {
        return userRepository.existsByLogin(login);
    }
}
