package ru.d2k.parkle.datasource.cache;

import java.util.Optional;

public interface CacheProvider<C> {
    void setToCache(String key, C value);
    Optional<C> get(String key);
    void deleteFromCache(String key);
    boolean existsInCache(String key);
}
