package ru.d2k.parkle.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.d2k.parkle.entity.UserEntity;
import java.util.Optional;
import static ru.d2k.parkle.test.consts.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserEntityRepositoryTest extends AbstractPostgresContainer {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserEntityRepository repository;

    // save.
    @Test
    void save_shouldReturnSavedEntity() {
        this.persistAndFlushRoleEntity();

        repository.save(USER_ENTITY);

        testEntityManager.flush();
        testEntityManager.clear();

        var saved = testEntityManager.find(UserEntity.class, USER_ID);
        assertEquals(USER_ENTITY, saved);
    }

    // findById.
    @Test
    void findById_shouldReturnSavedUserEntity() {
        this.persistAndFlushRoleEntity();

        testEntityManager.persistAndFlush(USER_ENTITY);
        testEntityManager.clear();

        var savedEntity = repository.findById(USER_ID);

        assertEquals(Optional.of(USER_ENTITY), savedEntity);
    }

    @Test
    void findById_shouldReturnEmptyWhenNotPersist() {
        var savedEntity = repository.findById(USER_ID);

        assertFalse(savedEntity.isPresent());
    }

    // findByLogin.
    @Test
    void findByLogin_shouldReturnSavedUserEntity() {
        this.persistAndFlushRoleEntity();

        testEntityManager.persistAndFlush(USER_ENTITY);
        testEntityManager.clear();

        var savedEntity = repository.findByLogin(USER_LOGIN);

        assertEquals(Optional.of(USER_ENTITY), savedEntity);
    }

    @Test
    void findByLogin_shouldReturnEmptyWhenNotPersist() {
        var savedEntity = repository.findByLogin(USER_LOGIN);

        assertFalse(savedEntity.isPresent());
    }

    // existsById.
    @Test
    void existsById_shouldReturnTrue() {
        this.persistAndFlushRoleEntity();

        testEntityManager.persistAndFlush(USER_ENTITY);
        testEntityManager.clear();

        boolean isExist = repository.existsById(USER_ID);

        assertTrue(isExist);
    }

    @Test
    void existsById_shouldReturnFalseWhenNotPersist() {
        boolean isExist = repository.existsById(USER_ID);

        assertFalse(isExist);
    }

    // existsByLogin.
    @Test
    void existsByLogin_shouldReturnTrue() {
        this.persistAndFlushRoleEntity();

        testEntityManager.persistAndFlush(USER_ENTITY);
        testEntityManager.clear();

        boolean isExist = repository.existsByLogin(USER_LOGIN);

        assertTrue(isExist);
    }

    @Test
    void existsByLogin_shouldReturnFalseWhenNotPersist() {
        boolean isExist = repository.existsByLogin(USER_LOGIN);

        assertFalse(isExist);
    }

    // deleteById.
    @Test
    void deleteById_shouldReturnFalseOnCheckExist() {
        this.persistAndFlushRoleEntity();

        testEntityManager.persistAndFlush(USER_ENTITY);
        testEntityManager.clear();

        repository.deleteById(USER_ID);

        boolean isExist = repository.existsById(USER_ID);
        assertFalse(isExist);
    }

    // deleteByLogin.
    @Test
    void deleteByLogin_shouldReturnFalseOnCheckExist() {
        this.persistAndFlushRoleEntity();
        testEntityManager.persistAndFlush(USER_ENTITY);
        testEntityManager.clear();

        repository.deleteByLogin(USER_LOGIN);

        boolean isExist = repository.existsByLogin(USER_LOGIN);

        assertFalse(isExist);
    }

    private void persistAndFlushRoleEntity() {
        testEntityManager.persistAndFlush(ROLE_ENTITY);
    }
}