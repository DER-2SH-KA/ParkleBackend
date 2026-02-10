package ru.d2k.parkle.dao.database.website;

import ru.d2k.parkle.entity.Website;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WebsiteDatabaseSource {
    Website save(Website entity);
    List<Website> getAll();
    Optional<Website> getById(UUID id);
    List<Website> getByUserIdSortedByTitleAsc(UUID userId);
    Website getReferenceById(UUID id);
    void deleteById(UUID id);
    boolean existsById(UUID id);
}
