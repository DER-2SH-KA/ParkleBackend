package ru.d2k.parkle.dao.database.website;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.repository.WebsiteEntityRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Primary
@Component
public class WebsitePostgresDatabase implements WebsiteDatabase {

    @Autowired
    private final WebsiteEntityRepository repository;

    @Override
    public WebsiteEntity save(WebsiteEntity entity) {
        return repository.save(entity);
    }

    @Override
    public List<WebsiteEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<WebsiteEntity> getById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<WebsiteEntity> getByUserIdSortedByTitleAsc(UUID userId) {
        return repository.findByUserIdOrderByTitleAsc(userId);
    }

    @Override
    public WebsiteEntity getReferenceById(UUID id) {
        return repository.getReferenceById(id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}