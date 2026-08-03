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
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;
import java.util.Optional;

// @Disabled
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16.9");

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByName_shouldSaveAndFindByNameNewRole() {
        Role newRole = Role.create("Test", 10);

        testEntityManager.persistAndFlush(newRole);

        Optional<Role> savedRole = roleRepository.findByName("Test");

        Assertions.assertThat(savedRole).isPresent();
        Assertions.assertThat(savedRole.get().getName()).isEqualTo("Test");
    }

    @Test
    void findByName_shouldReturnOptionalEmptyWhenUserByLoginNotExists() {
        Role newRole = Role.create("Test1", 10);

        testEntityManager.persistAndFlush(newRole);

        Optional<Role> savedRole = roleRepository.findByName("Test");

        Assertions.assertThat(savedRole).isEmpty();
    }

    @Test
    void existsByName_shouldBeTrueWhenSaveAndExistByNameNewRole() {
        Role newRole = Role.create("Test", 10);

        testEntityManager.persistAndFlush(newRole);

        boolean isRoleExists = roleRepository.existsByName("Test");

        Assertions.assertThat(isRoleExists).isTrue();
    }

    @Test
    void existsByName_shouldBeFalseWhenSaveAndExistByNameNewRole() {
        Role newRole = Role.create("Test1", 10);

        testEntityManager.persistAndFlush(newRole);

        boolean isRoleExists = roleRepository.existsByName("Test");

        Assertions.assertThat(isRoleExists).isFalse();
    }

    @Test
    void existsById_shouldSaveAndExistsByIdNewRole() {
        Role newRole = Role.create("Test", 10);

        testEntityManager.persistAndFlush(newRole);

        boolean isRoleExists = roleRepository.existsById(newRole.getId());

        Assertions.assertThat(isRoleExists).isTrue();
    }

    @Test
    void existsById_shouldBeFalseWhenSaveAndExistByIdNewRole() {
        Role newRole = Role.create("Test1", 10);

        testEntityManager.persistAndFlush(newRole);

        boolean isRoleExists = roleRepository.existsById(UuidGeneratorUtil.generateNewUuidV7());

        Assertions.assertThat(isRoleExists).isFalse();
    }
}