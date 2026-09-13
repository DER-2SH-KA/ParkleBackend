package ru.d2k.parkle.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import ru.d2k.parkle.entity.RoleEntity;
import ru.d2k.parkle.entity.UserEntity;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;

// @Disabled
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserEntityRepositoryTest {

    private static final RoleEntity ROLE = RoleEntity.create("Test");
    private static final Set<RoleEntity> ROLES = Set.of(ROLE);

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16.9");

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByLogin_shouldFindUserByLogin() {
        UserEntity newUser = UserEntity.create("Login", "Password", ROLES);

        testEntityManager.persistAndFlush(ROLE);
        testEntityManager.persistAndFlush(newUser);

        Optional<UserEntity> savedUser = userRepository.findByLogin("Login");

        Assertions.assertThat(savedUser).isPresent();
        Assertions.assertThat(savedUser.get().getLogin()).isEqualTo("Login");
    }

    @Test
    void findByLogin_shouldReturnEmptyWhenFindUserByLogin() {
        UserEntity newUser = UserEntity.create("Login1", "Password", ROLES);

        testEntityManager.persistAndFlush(ROLE);
        testEntityManager.persistAndFlush(newUser);

        Optional<UserEntity> savedUser = userRepository.findByLogin("Login");

        Assertions.assertThat(savedUser).isEmpty();
    }

    @Test
    void existsById_shouldReturnTrueWhenUserExistById() {
        UserEntity newUser = UserEntity.create("Login", "Password", ROLES);

        testEntityManager.persistAndFlush(ROLE);
        testEntityManager.persistAndFlush(newUser);

        boolean isUserExists = userRepository.existsById(newUser.getId());

        Assertions.assertThat(isUserExists).isTrue();
    }

    @Test
    void existsById_shouldReturnFalseWhenUserNotExistById() {
        UserEntity newUser = UserEntity.create("Login", "Password", ROLES);

        testEntityManager.persistAndFlush(ROLE);
        testEntityManager.persistAndFlush(newUser);

        boolean isUserExists = userRepository.existsById(UuidGeneratorUtil.generateNewUuidV7());

        Assertions.assertThat(isUserExists).isFalse();
    }

    @Test
    void existsByLogin_shouldReturnTrueWhenUserExistByLogin() {
        UserEntity newUser = UserEntity.create("Login", "Password", ROLES);

        testEntityManager.persistAndFlush(ROLE);
        testEntityManager.persistAndFlush(newUser);

        boolean isUserExists = userRepository.existsByLogin("Login");

        Assertions.assertThat(isUserExists).isTrue();
    }

    @Test
    void existsByLogin_shouldReturnFalseWhenUserNotExistByLogin() {
        UserEntity newUser = UserEntity.create("Login1", "Password", ROLES);

        testEntityManager.persistAndFlush(ROLE);
        testEntityManager.persistAndFlush(newUser);

        boolean isUserExists = userRepository.existsByLogin("Login");

        Assertions.assertThat(isUserExists).isFalse();
    }

    @Test
    void deleteByLogin_shouldSaveAndExistsByLoginNewUser() {
        UserEntity newUser = UserEntity.create("Login", "Password", ROLES);

        testEntityManager.persistAndFlush(ROLE);
        testEntityManager.persistAndFlush(newUser);

        userRepository.deleteByLogin("Login");

        Assertions.assertThat(userRepository.existsByLogin("Login")).isFalse();
    }
}