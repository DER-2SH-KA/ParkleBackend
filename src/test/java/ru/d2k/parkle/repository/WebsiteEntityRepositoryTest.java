package ru.d2k.parkle.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.d2k.parkle.entity.UserEntity;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.entity.WebsiteTagEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.d2k.parkle.test.consts.TestConstants.ROLE_ENTITY;
import static ru.d2k.parkle.test.consts.TestConstants.ROLE_ID;
import static ru.d2k.parkle.test.consts.TestConstants.USER_ENTITY;
import static ru.d2k.parkle.test.consts.TestConstants.USER_ID;
import static ru.d2k.parkle.test.consts.TestConstants.WEBSITE_ENTITY;
import static ru.d2k.parkle.test.consts.TestConstants.WEBSITE_ID;
import static ru.d2k.parkle.test.consts.TestConstants.WEBSITE_TAG_ENTITY;
import static ru.d2k.parkle.test.consts.TestConstants.WEBSITE_TAG_ID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WebsiteEntityRepositoryTest extends AbstractPostgresContainer {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private WebsiteEntityRepository repository;

    // save.
    @Test
    void save_shouldReturnSavedEntity() {
        this.persistAndFlushUserEntityAndWebsiteTag();

        repository.save(WEBSITE_ENTITY);

        testEntityManager.flush();
        testEntityManager.clear();

        var saved = testEntityManager.find(WebsiteEntity.class, WEBSITE_ID);
        assertEquals(WEBSITE_ENTITY, saved);
    }

    // findById.
    @Test
    void findById_shouldReturnSavedUserEntity() {
        this.persistAndFlushUserEntityAndWebsiteTag();
        testEntityManager.persistAndFlush(WEBSITE_ENTITY);
        testEntityManager.clear();

        var savedEntity = repository.findById(WEBSITE_ID);

        assertEquals(Optional.of(WEBSITE_ENTITY), savedEntity);
    }

    @Test
    void findById_shouldReturnEmptyWhenNotPersist() {
        var savedEntity = repository.findById(WEBSITE_ID);

        assertFalse(savedEntity.isPresent());
    }

    // findByUserIdOrderByTitleAsc.
    @Test
    void findByUserIdOrderByTitleAsc_shouldReturnUserWebsiteTags() {
        this.persistAndFlushUserEntityAndWebsiteTag();
        testEntityManager.persistAndFlush(WEBSITE_ENTITY);
        testEntityManager.clear();

        var tags = repository.findByUserIdOrderByTitleAsc(USER_ID);

        var expectedWebsites = List.of(WEBSITE_ENTITY);

        assertArrayEquals(expectedWebsites.toArray(), tags.toArray());
    }

    // existsById.
    @Test
    void existsById_shouldReturnTrue() {
        this.persistAndFlushUserEntityAndWebsiteTag();
        testEntityManager.persistAndFlush(WEBSITE_ENTITY);
        testEntityManager.clear();

        boolean isExist = repository.existsById(WEBSITE_ID);

        assertTrue(isExist);
    }

    @Test
    void existsById_shouldReturnFalse() {
        boolean isExist = repository.existsById(WEBSITE_ID);

        assertFalse(isExist);
    }

    // deleteById.
    @Test
    void deleteById_shouldReturnFalseOnCheckExist() {
        this.persistAndFlushUserEntityAndWebsiteTag();
        testEntityManager.persistAndFlush(WEBSITE_ENTITY);
        testEntityManager.clear();

        repository.deleteById(WEBSITE_ID);

        boolean isExist = repository.existsById(WEBSITE_ID);
        assertFalse(isExist);
    }

    private void persistAndFlushUserEntityAndWebsiteTag() {
        testEntityManager.persistAndFlush(ROLE_ENTITY);
        testEntityManager.persistAndFlush(USER_ENTITY);
        testEntityManager.persistAndFlush(WEBSITE_TAG_ENTITY);
    }
}