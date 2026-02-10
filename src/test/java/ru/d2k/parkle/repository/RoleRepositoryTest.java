package ru.d2k.parkle.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.d2k.parkle.entity.Role;

import org.assertj.core.api.Assertions.*;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.util.Optional;

@Disabled
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByName_shouldSaveAndFindByNameNewRole() {
        Role newRole = Role.create("Test", 10);

        testEntityManager.persistAndFlush(newRole);

        Optional<Role> savedRole = roleRepository.findByName("Test");

        Assertions
                .assertThat(savedRole)
                .isPresent();
        Assertions
                .assertThat(savedRole.get().getName())
                .isEqualTo("Test");
    }

    @Test
    void findByName_shouldReturnOptionalEmptyWhenUserByLoginNotExists() {
        Role newRole = Role.create("Test1", 10);

        testEntityManager.persistAndFlush(newRole);

        Optional<Role> savedRole = roleRepository.findByName("Test");

        Assertions
                .assertThat(savedRole)
                .isEmpty();
    }

    @Test
    void existsByName_shouldBeTrueWhenSaveAndExistByNameNewRole() {
        Role newRole = Role.create("Test", 10);

        testEntityManager.persistAndFlush(newRole);

        boolean isRoleExists = roleRepository.existsByName("Test");

        Assertions
                .assertThat(isRoleExists)
                .isTrue();
    }

    @Test
    void existsByName_shouldBeFalseWhenSaveAndExistByNameNewRole() {
        Role newRole = Role.create("Test1", 10);

        testEntityManager.persistAndFlush(newRole);

        boolean isRoleExists = roleRepository.existsByName("Test");

        Assertions
                .assertThat(isRoleExists)
                .isFalse();
    }

    @Test
    void existsById_shouldSaveAndExistsByIdNewRole() {
        Role newRole = Role.create("Test", 10);

        testEntityManager.persistAndFlush(newRole);

        boolean isRoleExists = roleRepository.existsById(newRole.getId());

        Assertions
                .assertThat(isRoleExists)
                .isTrue();
    }

    @Test
    void existsById_shouldBeFalseWhenSaveAndExistByIdNewRole() {
        Role newRole = Role.create("Test1", 10);

        testEntityManager.persistAndFlush(newRole);

        boolean isRoleExists = roleRepository.existsById(Uuid7Generator.generateNewUUID());

        Assertions
                .assertThat(isRoleExists)
                .isFalse();
    }
}
