package ru.d2k.parkle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import java.util.List;
import java.util.UUID;

public interface WebsiteTagEntityRepository extends JpaRepository<WebsiteTagEntity, UUID> {

    List<WebsiteTagEntity> findByUserIdOrderByTitleAsc(UUID userId);
}
