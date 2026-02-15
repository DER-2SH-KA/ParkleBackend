package ru.d2k.parkle.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dao.cache.user.UserCacheSource;
import ru.d2k.parkle.dao.database.user.UserDatabaseSource;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.entity.cache.RoleCache;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.exception.UserNotFoundException;
import ru.d2k.parkle.redis.RedisCacheKeys;
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
    private final UserDatabaseSource userDatabase;
    private final UserCacheSource userCache;
    private final UserMapper userMapper;

    private final RoleDao roleDao;

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

        log.info("Users was taken from database!");

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
            log.info("User by id {} was taken from database!", id);

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
        return userDatabase.getReferenceById(id);
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

        Optional<Role> role = roleDao.getFromDatabaseByName(udto.getRoleName());

        if (entity.isPresent() && role.isPresent()) {
            userMapper.updateByDto(entity.get(), udto, role.get());

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

        log.error("User to delete with login '{}' not exist!", login);
        return true;
    }

    public void refreshCacheByWebsiteCache(String key, Duration duration, String userLogin) {
        this.deleteFromCache(RedisCacheKeys.USER_SLICE_KEY + userLogin);

        UserCache newUserCache = this.getByLogin(userLogin)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format("User not found by login '%s'!", userLogin))
                );

        this.setToCache(key, newUserCache, duration);

        log.debug("User's cache with login '{}' was refreshed!", userLogin);
    }

    /**
     * Check is exist {@link User} in database by name (ONLY DATABASE AS SOURCE OF TRUTH).
     * @param login user's login.
     * @return is user exist in database.
     * */
    public boolean existsByLogin(String login) {
        return this.userDatabase.existsByLogin(login);
    }

    private void setToCache(String key, UserCache value, Duration duration) {
        userCache.set(key, value, duration);
    }

    private Optional<UserCache> getFromCache(String key) {
        return userCache.get(key);
    }

    private void deleteFromCache(String key) { userCache.delete(key); }

    private User saveToDatabase(User entity) {
        return userDatabase.save(entity);
    }

    private List<User> getAllFromDatabase() {
        return userDatabase.getAll();
    }

    private Optional<User> getFromDatabaseById(UUID id) {
        return userDatabase.getById(id);
    }

    public Optional<User> getFromDatabaseByLogin(String login) {
        return userDatabase.getByLogin(login);
    }

    private void deleteFromDatabaseByLogin(String login) {
        userDatabase.deleteByLogin(login);
    }

    private boolean existInDatabaseByLogin(String login) {
        return userDatabase.existsByLogin(login);
    }
}
