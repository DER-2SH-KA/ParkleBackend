package ru.d2k.parkle.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.entity.Website;

import org.assertj.core.api.Assertions.*;

import java.util.List;

@DataJpaTest
public class WebsiteRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private WebsiteRepository websiteRepository;

    @Test
    void findByUserIdOrderByTitleAsc_shouldReturnListSizeThreeOfWebsites() {
        Role newRole = Role.create("Test", 10);
        User newUser = User.create(newRole, "Login", "Email@mail.ru", "Password");

        Website website1 = Website.create(newUser, "#000", "Website1", null, "http://example.com");
        Website website2 = Website.create(newUser, "#F00", "Website2", "Description 1", "http://example2.com");
        Website website3 = Website.create(newUser, "#0F0", "Website3", null, "http://example3.com");

        testEntityManager.persistAndFlush(newRole);
        testEntityManager.persistAndFlush(newUser);

        testEntityManager.persistAndFlush(website1);
        testEntityManager.persistAndFlush(website2);
        testEntityManager.persistAndFlush(website3);

        List<Website> websites = websiteRepository.findByUserIdOrderByTitleAsc(newUser.getId());

        Assertions
                .assertThat(websites.size())
                .isEqualTo(3);
    }

    @Test
    void existsById_shouldBeTrueWhenWebsiteByIdExists() {
        Role newRole = Role.create("Test", 10);
        User newUser = User.create(newRole, "Login", "Email@mail.ru", "Password");
        Website website = Website.create(newUser, "#000", "Website1", null, "http://example.com");

        testEntityManager.persistAndFlush(newRole);
        testEntityManager.persistAndFlush(newUser);
        testEntityManager.persistAndFlush(website);

        boolean isWebsiteExist = websiteRepository.existsById(website.getId());

        Assertions
                .assertThat(isWebsiteExist)
                .isTrue();
    }
}
