package ru.d2k.parkle.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dao.database.website.WebsiteDatabase;
import ru.d2k.parkle.dto.WebsiteUpdateDto;
import ru.d2k.parkle.entity.UserEntity;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.entity.cache.WebsiteCache;
import ru.d2k.parkle.exception.UserNotFoundException;
import ru.d2k.parkle.exception.WebsiteNotFoundException;
import ru.d2k.parkle.utils.mapper.WebsiteMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebsiteDao {

    @Autowired
    private final WebsiteDatabase database;

    @Autowired
    private final WebsiteMapper mapper;

    @Autowired
    private final UserDao userDao;

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
            throw new WebsiteNotFoundException(String.format("WebsiteEntity not found by ID '%s'!", id));
        } else {
            WebsiteCache cache = mapper.toCache(fromDatabase.get());

            log.debug("WebsiteEntity by id '{}' was taken from database!", id);

            return Optional.ofNullable(cache);
        }
    }

    public WebsiteEntity getReferenceById(UUID id) {
        return database.getReferenceById(id);
    }

    public Optional<WebsiteCache> updateById(UUID id, WebsiteUpdateDto updateWebsiteDto, String userLogin) {
        Optional<WebsiteEntity> entity = this.getByIdFromDatabase(id);
        Optional<UserEntity> userEntity = userDao.getByLoginFromDatabase(userLogin); // TODO: переделать в будущем без публичного login метода.


        if (entity.isPresent() && userEntity.isPresent()) {
            log.debug("Entity and website is present");

            mapper.updateEntityByDto(entity.get(), updateWebsiteDto, userEntity.get());

            WebsiteEntity updatedEntity = this.saveToDatabase(entity.get());
            WebsiteCache cache = mapper.toCache(updatedEntity);

            log.debug("WebsiteEntity with id {} was updated", id);

            return Optional.of(cache);
        } else {
            if (entity.isEmpty()) {
                log.error("WebsiteEntity not found with ID: {}", id);

                throw new WebsiteNotFoundException(String.format("WebsiteEntity not found by ID '%s'!", id));
            } else if (userEntity.isEmpty()) {
                log.error("UserEntity not found with login: {}", userLogin);

                throw new UserNotFoundException(String.format("UserEntity not found by login '%s'!", updateWebsiteDto.userLogin()));
            }
        }

        log.error("WebsiteEntity with id {} wasn't updated", id);

        return Optional.empty();
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