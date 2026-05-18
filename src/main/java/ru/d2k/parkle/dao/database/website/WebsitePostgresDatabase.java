package ru.d2k.parkle.dao.database.website;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.entity.Website;
import ru.d2k.parkle.repository.WebsiteRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Primary
@Component
@RequiredArgsConstructor
public class WebsitePostgresDatabase implements WebsiteDatabaseSource {

    @Autowired
    private final WebsiteRepository websiteRepository;

    @Override
    public Website save(Website entity) {
        return websiteRepository.save(entity);
    }

    @Override
    public List<Website> getAll() {
        return websiteRepository.findAll();
    }

    @Override
    public Optional<Website> getById(UUID id) {
        return websiteRepository.findById(id);
    }

    @Override
    public List<Website> getByUserIdSortedByTitleAsc(UUID userId) {
        return websiteRepository.findByUserIdOrderByTitleAsc(userId);
    }

    @Override
    public Website getReferenceById(UUID id) {
        return websiteRepository.getReferenceById(id);
    }

    @Override
    public void deleteById(UUID id) {
        websiteRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return websiteRepository.existsById(id);
    }
}