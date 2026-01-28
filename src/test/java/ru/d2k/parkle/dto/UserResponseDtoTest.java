package ru.d2k.parkle.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.util.UUID;
import java.util.stream.Stream;

public class UserResponseDtoTest {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static final UUID UUID = Uuid7Generator.generateNewUUID();
    private static final String ROLE_NAME = "role";
    private static final Integer ROLE_PRIORITY = 1;
    private static final String LOGIN = "login";
    private static final String EMAIL = "email@email.ru";

    @DisplayName("equals/hashCode - Should be True with similar objects")
    @Test
    public void shouldBeTrueWhenEqualsAndHashCodeWithSimilarObjects() {
        UserResponseDto dto1 = new UserResponseDto(UUID, ROLE_NAME, ROLE_PRIORITY, LOGIN, EMAIL);
        UserResponseDto dto2 = new UserResponseDto(UUID, ROLE_NAME, ROLE_PRIORITY, LOGIN, EMAIL);

        Assertions.assertEquals(dto1.hashCode(), dto2.hashCode());
        Assertions.assertEquals(dto1, dto2);
    }

    @DisplayName("equals - return false with null")
    @Test
    public void shouldBeFalseWhenEqualsWithNull() {
        UserResponseDto dto = new UserResponseDto(UUID, ROLE_NAME, ROLE_PRIORITY, LOGIN, EMAIL);

        Assertions.assertFalse(dto.equals(null));
    }

    @DisplayName("equals - return false with object which has null fields")
    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        UserResponseDto dto = new UserResponseDto(UUID, ROLE_NAME, ROLE_PRIORITY, LOGIN, EMAIL);

        Assertions.assertNotEquals(dto, new UserAuthDto(null, null));
    }

    @DisplayName("equals - return false with objects which has different fields")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields(UserResponseDto dto2) {
        UserResponseDto dto1 = new UserResponseDto(UUID, ROLE_NAME, ROLE_PRIORITY, LOGIN, EMAIL);

        Assertions.assertNotEquals(dto1, dto2);
    }

    private static Stream<UserResponseDto> shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields() {
        return Stream.of(
                new UserResponseDto(Uuid7Generator.generateNewUUID(), ROLE_NAME, ROLE_PRIORITY, LOGIN, EMAIL),
                new UserResponseDto(UUID, "role2", 2, LOGIN, EMAIL),
                new UserResponseDto(UUID, ROLE_NAME, ROLE_PRIORITY, "login2", EMAIL),
                new UserResponseDto(UUID, ROLE_NAME, ROLE_PRIORITY, LOGIN, "email2@mail.ru")
        );
    }
}
