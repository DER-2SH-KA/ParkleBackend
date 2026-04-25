package ru.d2k.parkle.dao.cache.website;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.entity.cache.WebsiteCache;
import java.time.Duration;
import java.util.Optional;

@Primary
@Component
@RequiredArgsConstructor
public class WebsiteRedisCache implements WebsiteCacheSource {

    @Autowired
    private final RedisTemplate<String, WebsiteCache> redisWebsiteTemplate;

    @Override
    public void set(String key, WebsiteCache cache, Duration duration) {
        redisWebsiteTemplate.opsForValue().set(key, cache, duration);
    }

    @Override
    public Optional<WebsiteCache> get(String key) {
        return Optional.ofNullable(redisWebsiteTemplate.opsForValue().get(key));
    }

    @Override
    public void delete(String key) {
        redisWebsiteTemplate.delete(key);
    }
}