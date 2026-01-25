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
    public WebsiteCache create(Website entity) {
        Website createdEntity = this.saveToDatabase(entity);
        WebsiteCache cache = websiteMapper.toCache(createdEntity);

        this.setToCache(
                RedisCacheKeys.WEBSITE_SLICE_KEY + cache.id().toString(),
                cache,
                Duration.ofMinutes(15)
        );

        return cache;
    }

    // Read.
    public List<WebsiteCache> getAll(String userLogin) {
        Optional<UserCache> userCache = userDao.getByLogin(userLogin);
        List<Website> websites = new ArrayList<>();

        if (userCache.isPresent()) {
            List<UUID> websiteIds = userCache.get().websiteIds();
            Optional<Website> entity = Optional.empty();

            for (UUID websiteId : websiteIds) {
                entity = this.getFromDatabaseById(websiteId);

                if (entity.isPresent()) {
                    websites.add(entity.get());
                }
            }
        }
        else {
            throw new UserNotFoundException(String.format("User not found by login '%s'!", userLogin));
        }

        return websites.stream()
                .map(websiteMapper::toCache)
                .toList();
    }

    public Optional<WebsiteCache> getById(UUID id) {
        Optional<WebsiteCache> fromCahce = this.getFromCache(RedisCacheKeys.WEBSITE_SLICE_KEY + id.toString());

        if (fromCahce.isPresent()) return fromCahce;

        Optional<Website> fromDatabase = this.getFromDatabaseById(id);

        if (fromDatabase.isEmpty()) {
            throw new WebsiteNotFoundException(String.format("Website not found by ID '%s'!", id));
        }

        return Optional.ofNullable(websiteMapper.toCache(fromDatabase.get()));
    }

    public Website getReferenceById(UUID id) {
        return websiteRepository.getReferenceById(id);
    }

    // Update.
    public Optional<WebsiteCache> update(UUID id, WebsiteUpdateDto udto) {
        Optional<Website> entity = this.getFromDatabaseById(id);
        Optional<User> userEntity = userDao.getFromDatabaseByLogin(udto.userLogin()); // TODO: переделать в будущем без публичного login метода.


        if (entity.isPresent() && userEntity.isPresent()) {
            websiteMapper.updateByDto(entity.get(), udto, userEntity.get());

            Website updatedEntity = this.saveToDatabase(entity.get());
            WebsiteCache cache = websiteMapper.toCache(updatedEntity);

            this.deleteFromCache(RedisCacheKeys.WEBSITE_SLICE_KEY + id.toString());

            this.setToCache(
                    RedisCacheKeys.WEBSITE_SLICE_KEY + id,
                    cache,
                    Duration.ofMinutes(15)
            );

            return Optional.of(cache);
        }
        else {
            if (entity.isEmpty())
                throw new WebsiteNotFoundException(String.format("Website not found by ID '%s'!", id));
            else if (userEntity.isEmpty()) {
                throw new UserNotFoundException(String.format("User not found by login '%s'!", udto.userLogin()));
            }
        }

        return Optional.empty();
    }

    public boolean deleteById(UUID id) {
        this.deleteFromCache(RedisCacheKeys.WEBSITE_SLICE_KEY + id);
        this.deleteFromDatabase(id);

        return !this.existInDatabaseById(id);
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
