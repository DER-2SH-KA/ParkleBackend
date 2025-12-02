package ru.d2k.parkle.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.User;

import java.util.Optional;

import org.assertj.core.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByLogin_shouldFindNewUserByLogin() {
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
    void existsById_shouldSaveAndExistsByIdNewUser() {
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
    void existsByLogin_shouldSaveAndExistsByLoginNewUser() {
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
