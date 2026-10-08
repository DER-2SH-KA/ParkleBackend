package ru.d2k.parkle.dao.database.websitetag;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import ru.d2k.parkle.repository.WebsiteTagEntityRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Primary
@Component
public class WebsiteTagPostgresDatabase implements WebsiteTagDatabase {

    private final WebsiteTagEntityRepository repository;

    @Override
    public List<WebsiteTagEntity> getByUserIdSortedByTitleAsc(UUID userId) {
        return repository.findByUserIdOrderByTitleAsc(userId);
    }

    @Override
    public WebsiteTagEntity save(WebsiteTagEntity entity) {
        return repository.save(entity);
    }

    @Override
    public List<WebsiteTagEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<WebsiteTagEntity> getById(UUID uuid) {
        return repository.findById(uuid);
    }

    @Override
    public WebsiteTagEntity getReferenceById(UUID uuid) {
        return repository.getReferenceById(uuid);
    }

    @Override
    public void deleteById(UUID uuid) {
        repository.deleteById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return repository.existsById(uuid);
    }
}
