package ru.d2k.parkle.dao.cache.role;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.entity.cache.RoleCache;

import java.time.Duration;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class RoleRedisCache implements RoleCacheSource {
    private final RedisTemplate<String, RoleCache> redisRoleTemplate;

    @Override
    public void set(String key, RoleCache value, Duration duration) {
        redisRoleTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public Optional<RoleCache> get(String key) {
        return Optional.ofNullable(redisRoleTemplate.opsForValue().get(key));
    }

    @Override
    public void delete(String key) {
        redisRoleTemplate.delete(key);
    }
}
