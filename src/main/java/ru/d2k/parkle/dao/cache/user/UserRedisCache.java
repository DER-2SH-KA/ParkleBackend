package ru.d2k.parkle.dao.cache.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.entity.cache.UserCache;
import java.time.Duration;
import java.util.Optional;

@Primary
@Component
@RequiredArgsConstructor
public class UserRedisCache implements UserCacheSource {

    @Autowired
    private final RedisTemplate<String, UserCache> redisUserTemplate;

    @Override
    public void set(String key, UserCache value, Duration duration) {
        redisUserTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public Optional<UserCache> get(String key) {
        return Optional.ofNullable(redisUserTemplate.opsForValue().get(key));
    }

    @Override
    public void delete(String key) {
        redisUserTemplate.delete(key);
    }
}