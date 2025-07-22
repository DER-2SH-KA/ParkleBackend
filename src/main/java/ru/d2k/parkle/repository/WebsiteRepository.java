package ru.d2k.parkle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.d2k.parkle.entity.Website;

import java.util.List;
import java.util.UUID;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, UUID> {
    List<Website> findByUserIdOrderByTitleAsc(UUID userId);
}

