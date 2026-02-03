package ru.d2k.parkle.dao.cache.user;

import ru.d2k.parkle.entity.cache.UserCache;

import java.time.Duration;
import java.util.Optional;

public interface UserCacheSource {
    void set(String key, UserCache value, Duration duration);
    Optional<UserCache> get(String key);
    void delete(String key);
}
