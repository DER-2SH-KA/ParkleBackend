package ru.d2k.parkle.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.d2k.parkle.entity.RoleEntity;
import java.util.Optional;
import static ru.d2k.parkle.test.consts.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

// @Disabled
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleEntityRepositoryTest extends AbstractPostgresContainer {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RoleEntityRepository repository;

    // save.
    @Test
    void save_shouldReturnSavedEntity() {
        repository.save(ROLE_ENTITY);

        testEntityManager.flush();
        testEntityManager.clear();

        var saved = testEntityManager.find(RoleEntity.class, ROLE_ID);
        assertEquals(ROLE_ENTITY, saved);
    }

    // findById.
    @Test
    void findById_shouldReturnSavedEntity() {
        testEntityManager.persistAndFlush(ROLE_ENTITY);
        testEntityManager.clear();

        var saved = repository.findById(ROLE_ID);

        assertEquals(Optional.of(ROLE_ENTITY), saved);
    }

    @Test
    void findById_shouldReturnEmptyWhenNotPersist() {
        var saved = repository.findById(ROLE_ID);

        assertFalse(saved.isPresent());
    }

    // findByName.
    @Test
    void findByName_shouldReturnSavedEntity() {
        testEntityManager.persistAndFlush(ROLE_ENTITY);
        testEntityManager.clear();

        var saved = repository.findByName(ROLE_NAME);

        assertEquals(Optional.of(ROLE_ENTITY), saved);
    }

    @Test
    void findByName_shouldReturnEmptyWhenNotPersist() {
        var saved = repository.findByName(ROLE_NAME);

        assertFalse(saved.isPresent());
    }

    // existsById.
    @Test
    void existsById_shouldReturnTrue() {
        testEntityManager.persistAndFlush(ROLE_ENTITY);
        testEntityManager.clear();

        boolean isExist = repository.existsById(ROLE_ID);

        assertTrue(isExist);
    }

    @Test
    void existsById_shouldReturnFalse() {
        boolean isExist = repository.existsById(ROLE_ID);

        assertFalse(isExist);
    }

    // existsByName.
    @Test
    void existsByName_shouldReturnTrue() {
        testEntityManager.persistAndFlush(ROLE_ENTITY);
        testEntityManager.clear();

        boolean isExist = repository.existsByName(ROLE_NAME);

        assertTrue(isExist);
    }

    @Test
    void existsByName_shouldReturnFalseWhenNotSaved() {
        boolean isExist = repository.existsByName(ROLE_NAME);

        assertFalse(isExist);
    }

    // deleteById.
    @Test
    void deleteById_shouldReturnFalseOnCheckExist() {
        testEntityManager.persistAndFlush(ROLE_ENTITY);
        testEntityManager.clear();

        repository.deleteById(ROLE_ID);

        boolean isExist = repository.existsById(ROLE_ID);
        assertFalse(isExist);
    }
}