package ru.d2k.parkle.dao;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dao.database.user.UserDatabase;
import ru.d2k.parkle.dao.database.websitetag.WebsiteTagDatabase;
import ru.d2k.parkle.dto.WebsiteTagUpdateDto;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.entity.cache.WebsiteTagCache;
import ru.d2k.parkle.exception.WebsiteNotFoundException;
import ru.d2k.parkle.utils.mapper.WebsiteTagMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebsiteTagDao {

    private final WebsiteTagDatabase database;
    private final WebsiteTagMapper mapper;

    public WebsiteTagCache create(WebsiteTagEntity entity) {
        WebsiteTagEntity createdEntity = this.saveToDatabase(entity);

        return mapper.toCache(createdEntity);
    }

    public List<WebsiteTagCache> getAll() {
        log.debug("Website tags was taken from database!");

        return database.getAll().stream()
                .map(mapper::toCache)
                .toList();
    }

    public List<WebsiteTagCache> getAllByUserLogin(UserCache userCache) {
        List<WebsiteTagEntity> tags = database.getByUserIdSortedByTitleAsc(userCache.id());

        return tags.stream()
                .map(mapper::toCache)
                .toList();
    }

    public Optional<WebsiteTagCache> getById(UUID id) {
        WebsiteTagEntity entity = this.getByIdFromDatabase(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("WebsiteTagEntity not found by ID '%s'!", id)));

        return Optional.ofNullable(mapper.toCache(entity));
    }

    public WebsiteTagEntity getReferenceById(UUID id) {
        return database.getReferenceById(id);
    }

    public Optional<WebsiteTagCache> updateById(UUID id, WebsiteTagUpdateDto updateDto) {
        WebsiteTagEntity entity = this.getByIdFromDatabase(id)
                .orElseThrow(() ->
                        new WebsiteNotFoundException(String.format("WebsiteTagEntity not found by ID '%s'!", id)));

        log.debug("Entity and website is present");

        entity.setTitle(updateDto.title());
        entity.setHex(updateDto.hex());

        WebsiteTagEntity updatedEntity = this.saveToDatabase(entity);
        WebsiteTagCache cache = mapper.toCache(updatedEntity);

        log.debug("WebsiteTagEntity with id {} was updated", id);

        return Optional.of(cache);
    }

    public boolean deleteById(UUID id) {
        this.deleteByIdFromDatabase(id);

        return !this.existsByIdInDatabase(id);
    }

    public boolean existsById(UUID id) {
        return this.existsByIdInDatabase(id);
    }

    private WebsiteTagEntity saveToDatabase(WebsiteTagEntity entity) {
        return database.save(entity);
    }

    private Optional<WebsiteTagEntity> getByIdFromDatabase(UUID id) {
        return database.getById(id);
    }

    private void deleteByIdFromDatabase(UUID id) {
        database.deleteById(id);
    }

    private boolean existsByIdInDatabase(UUID id) {
        return database.existsById(id);
    }
}
