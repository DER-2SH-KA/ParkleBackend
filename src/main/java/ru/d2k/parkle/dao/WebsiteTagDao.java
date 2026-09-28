package ru.d2k.parkle.dao;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dao.database.websitetag.WebsiteTagDatabase;
import ru.d2k.parkle.dto.WebsiteTagUpdateDto;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.entity.cache.WebsiteTagCache;
import ru.d2k.parkle.exception.entity.UserEntityNotFoundException;
import ru.d2k.parkle.exception.entity.WebsiteEntityNotFoundException;
import ru.d2k.parkle.utils.mapper.WebsiteTagMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebsiteTagDao {

    private final WebsiteTagDatabase database;
    private final WebsiteTagMapper mapper;
    private final UserDao userDao;

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

    public List<WebsiteTagCache> getAllByUserLogin(String login) {
        UserCache user = userDao.getByLogin(login)
                .orElseThrow(() -> new UserEntityNotFoundException("User entity not found by login: " + login));
        List<WebsiteTagEntity> tags = database.getByUserIdSortedByTitleAsc(user.id());

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

    public WebsiteTagCache updateById(UUID id, WebsiteTagUpdateDto updateDto) {
        WebsiteTagEntity entity = this.getByIdFromDatabase(id)
                .orElseThrow(() ->
                        new WebsiteEntityNotFoundException(String.format("WebsiteTagEntity not found by ID '%s'!", id)));

        log.debug("Entity and website is present");

        entity.setTitle(updateDto.title());
        entity.setHex(updateDto.hex());

        WebsiteTagEntity updatedEntity = this.saveToDatabase(entity);
        WebsiteTagCache cache = mapper.toCache(updatedEntity);

        log.debug("WebsiteTagEntity with id {} was updated", id);

        return cache;
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
