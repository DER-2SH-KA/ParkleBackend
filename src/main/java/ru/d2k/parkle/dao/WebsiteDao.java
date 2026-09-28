package ru.d2k.parkle.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dao.database.website.WebsiteDatabase;
import ru.d2k.parkle.dto.WebsiteUpdateDto;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.entity.cache.WebsiteCache;
import ru.d2k.parkle.exception.entity.WebsiteEntityNotFoundException;
import ru.d2k.parkle.utils.mapper.WebsiteMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebsiteDao {

    private final WebsiteDatabase database;
    private final WebsiteMapper mapper;
    private final WebsiteTagDao websiteTagDao;

    public WebsiteCache create(WebsiteEntity entity) {
        WebsiteEntity createdEntity = this.saveToDatabase(entity);

        return mapper.toCache(createdEntity);
    }

    public List<WebsiteCache> getAll() {
        log.debug("Websites was taken from database!");

        return database.getAll().stream()
                .map(mapper::toCache)
                .toList();
    }

    public List<WebsiteCache> getAllByUserLogin(UserCache userCache) {
        List<WebsiteCache> websiteCaches = new ArrayList<>();

        List<UUID> websiteIds = userCache.websiteIds();
        Optional<WebsiteCache> cache = Optional.empty();

        for (UUID websiteId : websiteIds) {
            cache = this.getById(websiteId);

            if (cache.isPresent()) {
                websiteCaches.add(cache.get());
            } else {
                log.error("WebsiteEntity not found by ID {}!", websiteId);
            }
        }

        return websiteCaches;
    }

    public Optional<WebsiteCache> getById(UUID id) {
        Optional<WebsiteEntity> fromDatabase = this.getByIdFromDatabase(id);

        if (fromDatabase.isEmpty()) {
            throw new WebsiteEntityNotFoundException(String.format("WebsiteEntity not found by ID '%s'!", id));
        } else {
            WebsiteCache cache = mapper.toCache(fromDatabase.get());

            log.debug("WebsiteEntity by id '{}' was taken from database!", id);

            return Optional.ofNullable(cache);
        }
    }

    public WebsiteEntity getReferenceById(UUID id) {
        return database.getReferenceById(id);
    }

    public WebsiteCache updateById(UUID id, WebsiteUpdateDto updateWebsiteDto) {
        WebsiteEntity entity = this.getByIdFromDatabase(id)
                .orElseThrow(() ->
                        new WebsiteEntityNotFoundException(String.format("WebsiteEntity not found by ID '%s'!", id)));

        log.debug("Entity and website is present");

        Set<WebsiteTagEntity> tagEntities = updateWebsiteDto.tags().stream()
                .map(dto -> websiteTagDao.getReferenceById(dto.id()))
                .collect(Collectors.toSet());

        entity.setTitle(updateWebsiteDto.title());
        entity.setDescription(updateWebsiteDto.description());
        entity.setUrl(updateWebsiteDto.url());
        entity.setHexColor(updateWebsiteDto.hexColor());
        entity.setTags(tagEntities);

        WebsiteEntity updatedEntity = this.saveToDatabase(entity);
        WebsiteCache cache = mapper.toCache(updatedEntity);

        log.debug("WebsiteEntity with id {} was updated", id);

        return cache;
    }

    public boolean deleteById(UUID id) {
        this.deleteByIdFromDatabase(id);

        return !this.existsByIdInDatabase(id);
    }

    public boolean existsById(UUID id) {
        return this.existsByIdInDatabase(id);
    }

    private WebsiteEntity saveToDatabase(WebsiteEntity entity) {
        return database.save(entity);
    }

    private Optional<WebsiteEntity> getByIdFromDatabase(UUID id) {
        return database.getById(id);
    }

    private void deleteByIdFromDatabase(UUID id) {
        database.deleteById(id);
    }

    private boolean existsByIdInDatabase(UUID id) {
        return database.existsById(id);
    }
}