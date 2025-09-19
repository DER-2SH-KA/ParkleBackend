package ru.d2k.parkle.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public class UserAuthDtoTest {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    @DisplayName("equals/hashCode - Should be True with similar objects")
    @Test
    public void shouldBeTrueWhenEqualsAndHashCodeWithSimilarObjects() {
        UserAuthDto dto1 = new UserAuthDto(LOGIN, PASSWORD);
        UserAuthDto dto2 = new UserAuthDto(LOGIN, PASSWORD);

        Assertions.assertEquals(dto1.hashCode(), dto2.hashCode());
        Assertions.assertEquals(dto1, dto2);
    }

    @DisplayName("equals - return false with null")
    @Test
    public void shouldBeFalseWhenEqualsWithNull() {
        UserAuthDto dto = new UserAuthDto(LOGIN, PASSWORD);

        Assertions.assertFalse(dto.equals(null));
    }

    @DisplayName("equals - return false with object which has null fields")
    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        UserAuthDto dto = new UserAuthDto(LOGIN, PASSWORD);

        Assertions.assertNotEquals(dto, new UserAuthDto(null, null));
    }

    @DisplayName("validate - return true with objects which has correct field's values")
    @Test
    public void shouldBeTrueWhenValidationObjectHasCorrectFieldsValues() {
        UserAuthDto dto = new UserAuthDto("Developer", "5GdS4FaVtgS");
        Set<ConstraintViolation<UserAuthDto>> violations = validator.validate(dto);

        Assertions.assertTrue(violations.isEmpty());
    }

    @DisplayName("equals - return false with objects which has different fields")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields(UserAuthDto dto2) {
        UserAuthDto dto1 = new UserAuthDto(LOGIN, PASSWORD);

        Assertions.assertNotEquals(dto1, dto2);
    }

    @DisplayName("validate - return false with objects which has wrong field's values")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenValidationObjectHasWrongFieldsValues(UserAuthDto dto) {
        Set<ConstraintViolation<UserAuthDto>> violations = validator.validate(dto);

        Assertions.assertFalse(violations.isEmpty());
    }

    private static Stream<UserAuthDto> shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields() {
        return Stream.of(
                new UserAuthDto("Login2", PASSWORD),
                new UserAuthDto("Login2", "Password2")
        );
    }

    private static Stream<UserAuthDto> shouldBeFalseWhenValidationObjectHasWrongFieldsValues() {
        final char[] overMaxLoginChars = new char[101];
        final char[] overMaxPasswordChars = new char[73];

        Arrays.fill(overMaxLoginChars, 'l');
        Arrays.fill(overMaxPasswordChars, 'p');

        final String overMaxLogin = new String(overMaxLoginChars);
        final String overMaxPassword = new String(overMaxPasswordChars);

        return Stream.of(
                new UserAuthDto(null, PASSWORD),
                new UserAuthDto("", PASSWORD),
                new UserAuthDto(overMaxLogin, PASSWORD),
                new UserAuthDto(LOGIN, null),
                new UserAuthDto(LOGIN, ""),
                new UserAuthDto(LOGIN, "1234567"),
                new UserAuthDto(LOGIN, overMaxPassword)
        );
    }
}
