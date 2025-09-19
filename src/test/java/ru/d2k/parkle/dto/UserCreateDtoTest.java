package ru.d2k.parkle.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public class UserCreateDtoTest {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static final String ROLE = "role";
    private static final String LOGIN = "login";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @DisplayName("equals/hashCode - Should be True with similar objects")
    @Test
    public void shouldBeTrueWhenEqualsAndHashCodeWithSimilarObjects() {
        UserCreateDto dto1 = new UserCreateDto(ROLE, LOGIN, EMAIL, PASSWORD);
        UserCreateDto dto2 = new UserCreateDto(ROLE, LOGIN, EMAIL, PASSWORD);

        Assertions.assertEquals(dto1.hashCode(), dto2.hashCode());
        Assertions.assertEquals(dto1, dto2);
    }

    @DisplayName("equals - return false with null")
    @Test
    public void shouldBeFalseWhenEqualsWithNull() {
        UserCreateDto dto = new UserCreateDto(ROLE, LOGIN, EMAIL, PASSWORD);

        Assertions.assertFalse(dto.equals(null));
    }

    @DisplayName("equals - return false with object which has null fields")
    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        UserCreateDto dto = new UserCreateDto(ROLE, LOGIN, EMAIL, PASSWORD);

        Assertions.assertNotEquals(dto, new UserAuthDto(null, null));
    }

    @DisplayName("validate - return true with objects which has correct field's values")
    @Test
    public void shouldBeTrueWhenValidationObjectHasCorrectFieldsValues() {
        UserCreateDto dto = new UserCreateDto(
                "Dev",
                "Developer",
                "developer@mail.ru",
                "5GdS4FaVtgS"
        );
        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(dto);

        Assertions.assertTrue(violations.isEmpty());
    }

    @DisplayName("equals - return false with objects which has different fields")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields(UserCreateDto dto2) {
        UserCreateDto dto1 = new UserCreateDto(ROLE, LOGIN, EMAIL, PASSWORD);

        Assertions.assertNotEquals(dto1, dto2);
    }

    @DisplayName("validate - return false with objects which has wrong field's values")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenValidationObjectHasWrongFieldsValues(UserCreateDto dto) {
        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(dto);

        Assertions.assertFalse(violations.isEmpty());
    }

    private static Stream<UserCreateDto> shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields() {
        return Stream.of(
                new UserCreateDto("role2", LOGIN, EMAIL, PASSWORD),
                new UserCreateDto(ROLE, "login2", EMAIL, PASSWORD),
                new UserCreateDto(ROLE, LOGIN, "email2", PASSWORD)
        );
    }

    private static Stream<UserCreateDto> shouldBeFalseWhenValidationObjectHasWrongFieldsValues() {
        final char[] overMaxLoginChars = new char[101];
        final char[] overMaxEmailChars = new char[321];
        final char[] overMaxPasswordChars = new char[73];

        Arrays.fill(overMaxLoginChars, 'l');
        Arrays.fill(overMaxEmailChars, 'e');
        Arrays.fill(overMaxPasswordChars, 'p');

        final String overMaxLogin = new String(overMaxLoginChars);
        final String overMaxEmail = new String(overMaxEmailChars);
        final String overMaxPassword = new String(overMaxPasswordChars);

        return Stream.of(
                new UserCreateDto(null, LOGIN, EMAIL, PASSWORD),
                new UserCreateDto("", LOGIN, EMAIL, PASSWORD),
                new UserCreateDto(ROLE, null, EMAIL, PASSWORD),
                new UserCreateDto(ROLE, "", EMAIL, PASSWORD),
                new UserCreateDto(ROLE, overMaxLogin, EMAIL, PASSWORD),
                new UserCreateDto(ROLE, LOGIN, null, PASSWORD),
                new UserCreateDto(ROLE, LOGIN, "", PASSWORD),
                new UserCreateDto(ROLE, LOGIN, overMaxEmail, PASSWORD),
                new UserCreateDto(ROLE, LOGIN, "invalid.email.example.com", PASSWORD),
                new UserCreateDto(ROLE, LOGIN, EMAIL, null),
                new UserCreateDto(ROLE, LOGIN, EMAIL, ""),
                new UserCreateDto(ROLE, LOGIN, EMAIL, "1234567"),
                new UserCreateDto(ROLE, LOGIN, EMAIL, overMaxPassword)
        );
    }
}
