package ru.d2k.parkle.datasource.cache.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.datasource.RoleDataProvider;
import ru.d2k.parkle.datasource.cache.CacheProvider;
import ru.d2k.parkle.entity.cache.RoleCache;

import java.time.Duration;
import java.util.Optional;

@Component
public class RoleRedisCacheProvider implements CacheProvider<RoleCache> {
    private final RedisTemplate<String, RoleCache> redisRoleTemplate;
    private final Duration duration = Duration.ofMinutes(15);

    public RoleRedisCacheProvider(RedisTemplate<String, RoleCache> redisRoleTemplate) {
        this.redisRoleTemplate = redisRoleTemplate;
    }

    @Override
    public void setToCache(String key, RoleCache value) {
        redisRoleTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public Optional<RoleCache> get(String key) {
        return Optional.ofNullable(redisRoleTemplate.opsForValue().get(key));
    }

    @Override
    public void deleteFromCache(String key) {
        redisRoleTemplate.delete(key);
    }
}
