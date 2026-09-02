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
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;

import java.util.List;

// @Disabled
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WebsiteEntityRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16.9");

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private WebsiteRepository websiteRepository;

    @Test
    void findByUserIdOrderByTitleAsc_shouldReturnListSizeThreeOfWebsites() {
        RoleEntity newRole = RoleEntity.create("Test", 10);
        UserEntity newUser = UserEntity.create(newRole, "Login", "Email@mail.ru", "Password");

        WebsiteEntity website1 = WebsiteEntity.create(newUser, "#000", "Website1", null,
                "http://example.com");
        WebsiteEntity website2 = WebsiteEntity.create(newUser, "#F00", "Website2", "Description 1",
                "http://example2.com");
        WebsiteEntity website3 = WebsiteEntity.create(newUser, "#0F0", "Website3", null,
                "http://example3.com");

        testEntityManager.persistAndFlush(newRole);
        testEntityManager.persistAndFlush(newUser);

        testEntityManager.persistAndFlush(website1);
        testEntityManager.persistAndFlush(website2);
        testEntityManager.persistAndFlush(website3);

        List<WebsiteEntity> websites = websiteRepository.findByUserIdOrderByTitleAsc(newUser.getId());

        Assertions.assertThat(websites.size()).isEqualTo(3);
    }

    @Test
    void findByUserIdOrderByTitleAsc_shouldReturnEmptyListOfWebsites() {
        RoleEntity newRole = RoleEntity.create("Test", 10);
        UserEntity newUser = UserEntity.create(newRole, "Login", "Email@mail.ru", "Password");

        WebsiteEntity website1 = WebsiteEntity.create(newUser, "#000", "Website1", null,
                "http://example.com");
        WebsiteEntity website2 = WebsiteEntity.create(newUser, "#F00", "Website2", "Description 1",
                "http://example2.com");
        WebsiteEntity website3 = WebsiteEntity.create(newUser, "#0F0", "Website3", null,
                "http://example3.com");

        testEntityManager.persistAndFlush(newRole);
        testEntityManager.persistAndFlush(newUser);

        testEntityManager.persistAndFlush(website1);
        testEntityManager.persistAndFlush(website2);
        testEntityManager.persistAndFlush(website3);

        List<WebsiteEntity> websites = websiteRepository.findByUserIdOrderByTitleAsc(UuidGeneratorUtil.generateNewUuidV7());

        Assertions.assertThat(websites.size()).isEqualTo(0);
    }

    @Test
    void existsById_shouldBeTrueWhenWebsiteByIdExists() {
        RoleEntity newRole = RoleEntity.create("Test", 10);
        UserEntity newUser = UserEntity.create(newRole, "Login", "Email@mail.ru", "Password");
        WebsiteEntity website = WebsiteEntity.create(newUser, "#000", "Website1", null,
                "http://example.com");

        testEntityManager.persistAndFlush(newRole);
        testEntityManager.persistAndFlush(newUser);
        testEntityManager.persistAndFlush(website);

        boolean isWebsiteExist = websiteRepository.existsById(website.getId());

        Assertions.assertThat(isWebsiteExist).isTrue();
    }

    @Test
    void existsById_shouldBeFalseWhenWebsiteNotExistById() {
        RoleEntity newRole = RoleEntity.create("Test", 10);
        UserEntity newUser = UserEntity.create(newRole, "Login", "Email@mail.ru", "Password");
        WebsiteEntity website = WebsiteEntity.create(newUser, "#000", "Website1", null,
                "http://example.com");

        testEntityManager.persistAndFlush(newRole);
        testEntityManager.persistAndFlush(newUser);
        testEntityManager.persistAndFlush(website);

        boolean isWebsiteExist = websiteRepository.existsById(UuidGeneratorUtil.generateNewUuidV7());

        Assertions.assertThat(isWebsiteExist).isFalse();
    }
}