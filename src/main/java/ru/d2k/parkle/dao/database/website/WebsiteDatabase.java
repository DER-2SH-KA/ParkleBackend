package ru.d2k.parkle.dao.database.website;

import ru.d2k.parkle.entity.WebsiteEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WebsiteDatabase {

    WebsiteEntity save(WebsiteEntity entity);
    List<WebsiteEntity> getAll();
    Optional<WebsiteEntity> getById(UUID id);
    List<WebsiteEntity> getByUserIdSortedByTitleAsc(UUID userId);
    WebsiteEntity getReferenceById(UUID id);
    void deleteById(UUID id);
    boolean existsById(UUID id);
}