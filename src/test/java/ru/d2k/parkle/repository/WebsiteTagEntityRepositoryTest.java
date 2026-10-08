package ru.d2k.parkle.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static ru.d2k.parkle.test.consts.TestConstants.*;

// @Disabled
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WebsiteTagEntityRepositoryTest extends AbstractPostgresContainer {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private WebsiteTagEntityRepository repository;

    // save.
    @Test
    void save_shouldReturnSavedEntity() {
        this.persistAndFlushUserEntity();

        repository.save(WEBSITE_TAG_ENTITY);

        testEntityManager.flush();
        testEntityManager.clear();

        var saved = testEntityManager.find(WebsiteTagEntity.class, WEBSITE_TAG_ID);
        assertEquals(WEBSITE_TAG_ENTITY, saved);
    }

    // findById.
    @Test
    void findById_shouldReturnSavedUserEntity() {
        this.persistAndFlushUserEntity();

        testEntityManager.persistAndFlush(WEBSITE_TAG_ENTITY);
        testEntityManager.clear();

        var savedEntity = repository.findById(WEBSITE_TAG_ID);

        assertEquals(Optional.of(WEBSITE_TAG_ENTITY), savedEntity);
    }

    @Test
    void findById_shouldReturnEmptyWhenNotPersist() {
        var savedEntity = repository.findById(WEBSITE_TAG_ID);

        assertFalse(savedEntity.isPresent());
    }

    // findByUserIdOrderByTitleAsc.
    @Test
    void findByUserIdOrderByTitleAsc_shouldReturnUserWebsiteTags() {
        this.persistAndFlushUserEntity();
        testEntityManager.persistAndFlush(WEBSITE_TAG_ENTITY);
        testEntityManager.clear();

        var tags = repository.findByUserIdOrderByTitleAsc(USER_ID);

        var expectedTags = List.of(WEBSITE_TAG_ENTITY);

        assertArrayEquals(expectedTags.toArray(), tags.toArray());
    }

    // deleteById.
    @Test
    void deleteById_shouldReturnFalseOnCheckExist() {
        this.persistAndFlushUserEntity();
        testEntityManager.persistAndFlush(WEBSITE_TAG_ENTITY);
        testEntityManager.clear();

        repository.deleteById(WEBSITE_TAG_ID);

        boolean isExist = repository.existsById(WEBSITE_TAG_ID);
        assertFalse(isExist);
    }

    private void persistAndFlushUserEntity() {
        testEntityManager.persistAndFlush(ROLE_ENTITY);
        testEntityManager.persistAndFlush(USER_ENTITY);
    }
}