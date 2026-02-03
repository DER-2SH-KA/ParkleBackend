package ru.d2k.parkle.dao.cache.website;

import ru.d2k.parkle.entity.cache.WebsiteCache;

import java.time.Duration;
import java.util.Optional;

public interface WebsiteCacheSource {
    void set(String key, WebsiteCache cache, Duration duration);
    Optional<WebsiteCache> get(String key);
    void delete(String key);
}
