package ru.d2k.parkle.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dao.database.website.WebsiteDatabase;
import ru.d2k.parkle.dto.WebsiteUpdateDto;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import ru.d2k.parkle.model.domain.User;
import ru.d2k.parkle.model.domain.Website;
import ru.d2k.parkle.exception.entity.WebsiteEntityNotFoundException;
import ru.d2k.parkle.utils.mapper.WebsiteMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// TODO: Пересмотреть сигнатуры методов (они не должны ничего знать про DTO-объекты).
@Slf4j
@RequiredArgsConstructor
@Component
public class WebsiteDao {

    private final WebsiteDatabase database;
    private final WebsiteMapper mapper;
    private final WebsiteTagDao websiteTagDao;

    public Website create(WebsiteEntity entity) {
        WebsiteEntity createdEntity = this.saveToDatabase(entity);

        return mapper.toDomain(createdEntity);
    }

    public List<Website> getAll() {
        log.debug("Websites was taken from database!");

        return database.getAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    public List<Website> getAllByUserLogin(User user) {
        List<Website> websiteCaches = new ArrayList<>();

        List<UUID> websiteIds = user.websiteIds();
        Optional<Website> cache = Optional.empty();

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

    public Optional<Website> getById(UUID id) {
        Optional<WebsiteEntity> fromDatabase = this.getByIdFromDatabase(id);

        if (fromDatabase.isEmpty()) {
            throw new WebsiteEntityNotFoundException(String.format("WebsiteEntity not found by ID '%s'!", id));
        } else {
            Website cache = mapper.toDomain(fromDatabase.get());

            log.debug("WebsiteEntity by id '{}' was taken from database!", id);

            return Optional.ofNullable(cache);
        }
    }

    public WebsiteEntity getReferenceById(UUID id) {
        return database.getReferenceById(id);
    }

    // TODO: Преобразовать в Application/Domain-сущность вместо DTO.
    public Website updateById(UUID id, WebsiteUpdateDto updateWebsiteDto) {
        WebsiteEntity entity = this.getByIdFromDatabase(id)
                .orElseThrow(() ->
                        new WebsiteEntityNotFoundException(String.format("WebsiteEntity not found by ID '%s'!", id)));

        log.debug("Entity and website is present");

        List<WebsiteTagEntity> tagEntities = updateWebsiteDto.tags().stream()
                .map(dto -> websiteTagDao.getReferenceById(dto.id()))
                .toList();

        entity.setTitle(updateWebsiteDto.title());
        entity.setDescription(updateWebsiteDto.description());
        entity.setUrl(updateWebsiteDto.url());
        entity.setHexColor(updateWebsiteDto.hexColor());
        entity.setTags(tagEntities);

        WebsiteEntity updatedEntity = this.saveToDatabase(entity);
        Website cache = mapper.toDomain(updatedEntity);

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