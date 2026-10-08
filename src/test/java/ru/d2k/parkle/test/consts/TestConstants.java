package ru.d2k.parkle.test.consts;

import org.testcontainers.utility.DockerImageName;
import ru.d2k.parkle.entity.RoleEntity;
import ru.d2k.parkle.entity.UserEntity;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import java.util.Set;
import java.util.UUID;

public interface TestConstants {

    DockerImageName POSTGRES_DOCKER_IMAGE_NAME = DockerImageName.parse("postgres:16.9");

    String ROLE_NAME = "ROLE";
    RoleEntity ROLE_ENTITY = RoleEntity.create(ROLE_NAME);
    UUID ROLE_ID = ROLE_ENTITY.getId();

    String USER_LOGIN = "LOGIN";
    String USER_PASSWORD = "PASSWORD";
    UserEntity USER_ENTITY = UserEntity.create(USER_LOGIN, USER_PASSWORD, Set.of(ROLE_ENTITY));
    UUID USER_ID = USER_ENTITY.getId();

    String HEX_COLOR = "#FFFFFF";

    String WEBSITE_TAG_TITLE = "TAG";
    WebsiteTagEntity WEBSITE_TAG_ENTITY = WebsiteTagEntity.create(USER_ENTITY, WEBSITE_TAG_TITLE,
            HEX_COLOR);
    UUID WEBSITE_TAG_ID = WEBSITE_TAG_ENTITY.getId();

    String WEBSITE_TITLE = "TITLE";
    String WEBSITE_DESCRIPTION = "DESCRIPTION";
    String WEBSITE_URL = "https://example.com";
    WebsiteEntity WEBSITE_ENTITY = WebsiteEntity.create(USER_ENTITY, HEX_COLOR, WEBSITE_TITLE,
            WEBSITE_DESCRIPTION, WEBSITE_URL, Set.of(WEBSITE_TAG_ENTITY));
    UUID WEBSITE_ID = WEBSITE_ENTITY.getId();
}
