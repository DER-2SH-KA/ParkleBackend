package ru.d2k.parkle.repository;

import org.springframework.data.repository.CrudRepository;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import java.util.Set;
import java.util.UUID;

public interface WebsiteTagRepository extends CrudRepository<WebsiteTagEntity, UUID> {

    Set<WebsiteTagEntity> findByUserIdOrderByTitleAsc(UUID userId);
}
