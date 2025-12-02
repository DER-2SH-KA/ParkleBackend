package ru.d2k.parkle.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.d2k.parkle.entity.Role;

import org.assertj.core.api.Assertions.*;

import java.util.Optional;

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
    void existsByName_shouldSaveAndExistsByNameNewRole() {
        Role newRole = Role.create("Test", 10);

        testEntityManager.persistAndFlush(newRole);

        boolean isRoleExists = roleRepository.existsByName("Test");

        Assertions
                .assertThat(isRoleExists)
                .isTrue();
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
}
