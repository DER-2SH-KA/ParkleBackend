package ru.d2k.parkle.repository;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.postgresql.PostgreSQLContainer;
import static ru.d2k.parkle.test.consts.TestConstants.POSTGRES_DOCKER_IMAGE_NAME;

public abstract class AbstractPostgresContainer {

    @Container
    public static final PostgreSQLContainer postgres;

    static {
        postgres = new PostgreSQLContainer(POSTGRES_DOCKER_IMAGE_NAME)
                .withReuse(true);

        postgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
