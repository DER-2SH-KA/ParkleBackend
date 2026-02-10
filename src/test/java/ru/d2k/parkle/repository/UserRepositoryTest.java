package ru.d2k.parkle.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.User;

import java.util.Optional;

import org.assertj.core.api.Assertions.*;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

@Disabled
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByLogin_shouldFindUserByLogin() {
        Role newRole = Role.create("Test", 10);
        User newUser = User.create(newRole, "Login", "Mail@mail.ru", "Password");

        testEntityManager.persistAndFlush(newRole);
        testEntityManager.persistAndFlush(newUser);

        Optional<User> savedUser = userRepository.findByLogin("Login");

        Assertions
                .assertThat(savedUser)
                .isPresent();
        Assertions
                .assertThat(savedUser.get().getLogin())
                .isEqualTo("Login");
    }

    @Test
    void findByLogin_shouldReturnEmptyWhenFindUserByLogin() {
        Role newRole = Role.create("Test", 10);
        User newUser = User.create(newRole, "Login1", "Mail@mail.ru", "Password");

        testEntityManager.persistAndFlush(newRole);
        testEntityManager.persistAndFlush(newUser);

        Optional<User> savedUser = userRepository.findByLogin("Login");

        Assertions
                .assertThat(savedUser)
                .isEmpty();
    }

    @Test
    void existsById_shouldReturnTrueWhenUserExistById() {
        Role newRole = Role.create("Test", 10);
        User newUser = User.create(newRole, "Login", "Mail@mail.ru", "Password");

        testEntityManager.persistAndFlush(newRole);
        testEntityManager.persistAndFlush(newUser);

        boolean isUserExists = userRepository.existsById(newUser.getId());

        Assertions
                .assertThat(isUserExists)
                .isTrue();
    }

    @Test
    void existsById_shouldReturnFalseWhenUserNotExistById() {
        Role newRole = Role.create("Test", 10);
        User newUser = User.create(newRole, "Login", "Mail@mail.ru", "Password");

        testEntityManager.persistAndFlush(newRole);
        testEntityManager.persistAndFlush(newUser);

        boolean isUserExists = userRepository.existsById(Uuid7Generator.generateNewUUID());

        Assertions
                .assertThat(isUserExists)
                .isFalse();
    }

    @Test
    void existsByLogin_shouldReturnTrueWhenUserExistByLogin() {
        Role newRole = Role.create("Test", 10);
        User newUser = User.create(newRole, "Login", "Mail@mail.ru", "Password");

        testEntityManager.persistAndFlush(newRole);
        testEntityManager.persistAndFlush(newUser);

        boolean isUserExists = userRepository.existsByLogin("Login");

        Assertions
                .assertThat(isUserExists)
                .isTrue();
    }

    @Test
    void existsByLogin_shouldReturnFalseWhenUserNotExistByLogin() {
        Role newRole = Role.create("Test", 10);
        User newUser = User.create(newRole, "Login1", "Mail@mail.ru", "Password");

        testEntityManager.persistAndFlush(newRole);
        testEntityManager.persistAndFlush(newUser);

        boolean isUserExists = userRepository.existsByLogin("Login");

        Assertions
                .assertThat(isUserExists)
                .isFalse();
    }

    @Test
    void deleteByLogin_shouldSaveAndExistsByLoginNewUser() {
        Role newRole = Role.create("Test", 10);
        User newUser = User.create(newRole, "Login", "Mail@mail.ru", "Password");

        testEntityManager.persistAndFlush(newRole);
        testEntityManager.persistAndFlush(newUser);

        userRepository.deleteByLogin("Login");

        Assertions
                .assertThat(userRepository.existsByLogin("Login"))
                .isFalse();
    }
}
