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

public class UserAuthenticationDtoTest {

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("equals/hashCode - Should be True with similar objects")
    @Test
    public void shouldBeTrueWhenEqualsAndHashCodeWithSimilarObjects() {
        UserAuthenticationDto dto1 = new UserAuthenticationDto(LOGIN, PASSWORD);
        UserAuthenticationDto dto2 = new UserAuthenticationDto(LOGIN, PASSWORD);

        Assertions.assertEquals(dto1.hashCode(), dto2.hashCode());
        Assertions.assertEquals(dto1, dto2);
    }

    @DisplayName("equals - return false with null")
    @Test
    public void shouldBeFalseWhenEqualsWithNull() {
        UserAuthenticationDto dto = new UserAuthenticationDto(LOGIN, PASSWORD);

        Assertions.assertFalse(dto.equals(null));
    }

    @DisplayName("equals - return false with object which has null fields")
    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        UserAuthenticationDto dto = new UserAuthenticationDto(LOGIN, PASSWORD);

        Assertions.assertNotEquals(dto, new UserAuthenticationDto(null, null));
    }

    @DisplayName("validate - return true with objects which has correct field's values")
    @Test
    public void shouldBeTrueWhenValidationObjectHasCorrectFieldsValues() {
        UserAuthenticationDto dto = new UserAuthenticationDto("Developer", "5GdS4FaVtgS");
        Set<ConstraintViolation<UserAuthenticationDto>> violations = validator.validate(dto);

        Assertions.assertTrue(violations.isEmpty());
    }

    @DisplayName("equals - return false with objects which has different fields")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields(UserAuthenticationDto dto2) {
        UserAuthenticationDto dto1 = new UserAuthenticationDto(LOGIN, PASSWORD);

        Assertions.assertNotEquals(dto1, dto2);
    }

    @DisplayName("validate - return false with objects which has wrong field's values")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenValidationObjectHasWrongFieldsValues(UserAuthenticationDto dto) {
        Set<ConstraintViolation<UserAuthenticationDto>> violations = validator.validate(dto);

        Assertions.assertFalse(violations.isEmpty());
    }

    private static Stream<UserAuthenticationDto> shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields() {
        return Stream.of(
                new UserAuthenticationDto("Login2", PASSWORD),
                new UserAuthenticationDto("Login2", "Password2"));
    }

    private static Stream<UserAuthenticationDto> shouldBeFalseWhenValidationObjectHasWrongFieldsValues() {
        char[] overMaxLoginChars = new char[101];
        char[] overMaxPasswordChars = new char[73];

        Arrays.fill(overMaxLoginChars, 'l');
        Arrays.fill(overMaxPasswordChars, 'p');

        String overMaxLogin = new String(overMaxLoginChars);
        String overMaxPassword = new String(overMaxPasswordChars);

        return Stream.of(
                new UserAuthenticationDto(null, PASSWORD),
                new UserAuthenticationDto("", PASSWORD),
                new UserAuthenticationDto(overMaxLogin, PASSWORD),
                new UserAuthenticationDto(LOGIN, null),
                new UserAuthenticationDto(LOGIN, ""),
                new UserAuthenticationDto(LOGIN, "1234567"),
                new UserAuthenticationDto(LOGIN, overMaxPassword));
    }
}