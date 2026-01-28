package ru.d2k.parkle.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dto.WebsiteUpdateDto;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.entity.Website;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.entity.cache.WebsiteCache;
import ru.d2k.parkle.exception.UserNotFoundException;
import ru.d2k.parkle.exception.WebsiteNotFoundException;
import ru.d2k.parkle.redis.RedisCacheKeys;
import ru.d2k.parkle.repository.WebsiteRepository;
import ru.d2k.parkle.utils.mapper.WebsiteMapper;

import java.time.Duration;
import java.util.*;

@Component
@RequiredArgsConstructor
public class WebsiteDao {
    private final WebsiteRepository websiteRepository;
    private final RedisTemplate<String, WebsiteCache> redisWebsiteTemplate;
    private final WebsiteMapper websiteMapper;
    private final UserDao userDao;

    // CRUD.
    // Create.
    public WebsiteCache create(Website entity, String userLogin) {
        Website createdEntity = this.saveToDatabase(entity);
        WebsiteCache cache = websiteMapper.toCache(createdEntity);

        this.setToCache(
                RedisCacheKeys.WEBSITE_SLICE_KEY + cache.id().toString(),
                cache,
                Duration.ofMinutes(15)
        );

        userDao.refreshCacheByWebsiteCache(
                RedisCacheKeys.USER_SLICE_KEY + userLogin,
                Duration.ofMinutes(15),
                userLogin
        );

        return cache;
    }

    // Read.
    public List<WebsiteCache> getAll() {
        return websiteRepository.findAll().stream()
                .map(websiteMapper::toCache)
                .toList();
    }

    public List<WebsiteCache> getAllByUserLogin(UserCache userCache) {
        List<WebsiteCache> websiteCaches = new ArrayList<>();

        List<UUID> websiteIds = userCache.websiteIds();
        Optional<WebsiteCache> cache = Optional.empty();

        System.out.println("Websites Ids: " + Arrays.toString(websiteCaches.toArray()));

        for (UUID websiteId : websiteIds) {
            cache = this.getById(websiteId);

            if (cache.isPresent()) {
                websiteCaches.add(cache.get());
            }
            else {
                System.err.printf("Website not found by ID '%s'!%n", websiteId);
            }
        }

        System.out.println("Websites taken: " + Arrays.toString(websiteCaches.toArray()));

        return websiteCaches;
    }

    public Optional<WebsiteCache> getById(UUID id) {
        Optional<WebsiteCache> fromCache = this.getFromCache(RedisCacheKeys.WEBSITE_SLICE_KEY + id.toString());

        if (fromCache.isPresent()) {
            System.out.println("Get website from Cache by ID: " + id);
            return fromCache;
        }

        Optional<Website> fromDatabase = this.getFromDatabaseById(id);

        if (fromDatabase.isEmpty()) {
            throw new WebsiteNotFoundException(String.format("Website not found by ID '%s'!", id));
        }
        else {
            WebsiteCache cache = websiteMapper.toCache(fromDatabase.get());

            this.setToCache(
                    RedisCacheKeys.WEBSITE_SLICE_KEY + id,
                    cache,
                    Duration.ofMinutes(15)
            );

            System.out.println("Get website from Database by ID: " + id);
            return Optional.ofNullable(cache);
        }
    }

    public Website getReferenceById(UUID id) {
        return websiteRepository.getReferenceById(id);
    }

    // Update.
    public Optional<WebsiteCache> update(UUID id, WebsiteUpdateDto udto, String userLogin) {
        System.out.println("Start updating website with ID: " + id);
        Optional<Website> entity = this.getFromDatabaseById(id);
        Optional<User> userEntity = userDao.getFromDatabaseByLogin(userLogin); // TODO: переделать в будущем без публичного login метода.


        if (entity.isPresent() && userEntity.isPresent()) {
            System.out.println("Entity and website is present");
            websiteMapper.updateByDto(entity.get(), udto, userEntity.get());

            Website updatedEntity = this.saveToDatabase(entity.get());
            WebsiteCache cache = websiteMapper.toCache(updatedEntity);

            this.deleteFromCache(RedisCacheKeys.WEBSITE_SLICE_KEY + id.toString());

            this.setToCache(
                    RedisCacheKeys.WEBSITE_SLICE_KEY + id,
                    cache,
                    Duration.ofMinutes(15)
            );

            userDao.refreshCacheByWebsiteCache(
                    RedisCacheKeys.USER_SLICE_KEY + userLogin,
                    Duration.ofMinutes(15),
                    userLogin
            );

            System.out.println("Website with ID was updated: " + id);
            return Optional.of(cache);
        }
        else {
            if (entity.isEmpty()) {
                System.out.println("Website not found with ID: " + id);
                throw new WebsiteNotFoundException(String.format("Website not found by ID '%s'!", id));
            }
            else if (userEntity.isEmpty()) {
                System.out.println("User not found with login: " + userLogin);
                throw new UserNotFoundException(String.format("User not found by login '%s'!", udto.userLogin()));
            }
        }

        System.out.println("Website wasn't updated with ID: " + id);
        return Optional.empty();
    }

    public boolean deleteById(UUID id, String userLogin) {
        Optional<WebsiteCache> cache = this.getFromCache(RedisCacheKeys.WEBSITE_SLICE_KEY + id);

        if (cache.isEmpty()) return true;

        this.deleteFromCache(RedisCacheKeys.WEBSITE_SLICE_KEY + id);
        this.deleteFromDatabase(id);

        userDao.refreshCacheByWebsiteCache(
                RedisCacheKeys.USER_SLICE_KEY + userLogin,
                Duration.ofMinutes(15),
                userLogin
        );

        return !this.existInDatabaseById(id);
    }

    public boolean existsById(UUID id) {
        return this.existInDatabaseById(id);
    }

    private void setToCache(String key, WebsiteCache cache, Duration duration) {
        redisWebsiteTemplate.opsForValue().set(key, cache, duration);
    }

    private Optional<WebsiteCache> getFromCache(String key) {
        return Optional.ofNullable(
                redisWebsiteTemplate.opsForValue().get(key)
        );
    }

    private void deleteFromCache(String key) {
        redisWebsiteTemplate.delete(key);
    }

    private boolean existsInCacheById(UUID id) {
        return getFromCache(RedisCacheKeys.WEBSITE_SLICE_KEY + id.toString()).isPresent();
    }

    private Website saveToDatabase(Website entity) {
        return websiteRepository.save(entity);
    }

    private Optional<Website> getFromDatabaseById(UUID id) {
        return websiteRepository.findById(id);
    }

    private List<Website> getFromDatabaseByUserIdSortedByTitleAsc(UUID userId) {
        return websiteRepository.findByUserIdOrderByTitleAsc(userId);
    }

    private void deleteFromDatabase(UUID id) {
        websiteRepository.deleteById(id);
    }

    private boolean existInDatabaseById(UUID id) {
        return websiteRepository.existsById(id);
    }
}
