package ru.d2k.parkle.test.consts;

import org.testcontainers.utility.DockerImageName;
import ru.d2k.parkle.dto.RoleCreateDto;
import ru.d2k.parkle.dto.RoleResponseDto;
import ru.d2k.parkle.dto.RoleUpdateDto;
import ru.d2k.parkle.dto.UserCreateDto;
import ru.d2k.parkle.entity.RoleEntity;
import ru.d2k.parkle.entity.UserEntity;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TestConstants {

    DockerImageName POSTGRES_DOCKER_IMAGE_NAME = DockerImageName.parse("postgres:16.9");

    // Entities.
    String ROLE_NAME = "ROLE";
    RoleEntity ROLE_ENTITY = RoleEntity.create(ROLE_NAME);
    UUID ROLE_ID = ROLE_ENTITY.getId();

    String USER_LOGIN = "LOGIN";
    String USER_PASSWORD = "PASSWORD";
    Set<RoleEntity> USER_ROLES = Set.of(ROLE_ENTITY);
    UserEntity USER_ENTITY = UserEntity.create(USER_LOGIN, USER_PASSWORD, USER_ROLES);
    UUID USER_ID = USER_ENTITY.getId();

    String HEX_COLOR = "#FFFFFF";

    String WEBSITE_TAG_TITLE = "TAG";
    WebsiteTagEntity WEBSITE_TAG_ENTITY = WebsiteTagEntity.create(USER_ENTITY, WEBSITE_TAG_TITLE,
            HEX_COLOR);
    UUID WEBSITE_TAG_ID = WEBSITE_TAG_ENTITY.getId();

    String WEBSITE_TITLE = "TITLE";
    String WEBSITE_DESCRIPTION = "DESCRIPTION";
    String WEBSITE_URL = "https://example.com";
    List<WebsiteTagEntity> WEBSITE_TAGS = List.of(WEBSITE_TAG_ENTITY);
    WebsiteEntity WEBSITE_ENTITY = WebsiteEntity.create(USER_ENTITY, HEX_COLOR, WEBSITE_TITLE,
            WEBSITE_DESCRIPTION, WEBSITE_URL, WEBSITE_TAGS);
    UUID WEBSITE_ID = WEBSITE_ENTITY.getId();

    // DTO.
    RoleCreateDto ROLE_CREATE_DTO = new RoleCreateDto(ROLE_NAME);
    RoleResponseDto ROLE_RESPONSE_DTO = new RoleResponseDto(ROLE_ID, ROLE_NAME);
    RoleUpdateDto ROLE_UPDATE_DTO = new RoleUpdateDto(ROLE_ID, ROLE_NAME + "1");

    Set<RoleResponseDto> USER_RESPONSE_DTO_ROLES = Set.of(ROLE_RESPONSE_DTO);
    UserCreateDto USER_CREATE_DTO = new UserCreateDto(USER_LOGIN, USER_PASSWORD, USER_RESPONSE_DTO_ROLES);
}
