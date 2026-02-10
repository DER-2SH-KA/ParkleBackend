package ru.d2k.parkle.dao.cache.role;

import ru.d2k.parkle.entity.cache.RoleCache;

import java.time.Duration;
import java.util.Optional;

public interface RoleCacheSource {
    void set(String key, RoleCache value, Duration duration);
    Optional<RoleCache> get(String key);
    void delete(String key);
}
