package ru.d2k.parkle.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.d2k.parkle.entity.RoleEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class UserEntityCreateDtoTest {

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final Set<RoleResponseDto> ROLES = new HashSet<>();

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("equals/hashCode - Should be True with similar objects")
    @Test
    public void shouldBeTrueWhenEqualsAndHashCodeWithSimilarObjects() {
        UserCreateDto dto1 = new UserCreateDto(LOGIN, PASSWORD, ROLES);
        UserCreateDto dto2 = new UserCreateDto(LOGIN, PASSWORD, ROLES);

        Assertions.assertEquals(dto1.hashCode(), dto2.hashCode());
        Assertions.assertEquals(dto1, dto2);
    }

    @DisplayName("equals - return false with null")
    @Test
    public void shouldBeFalseWhenEqualsWithNull() {
        UserCreateDto dto = new UserCreateDto(LOGIN, PASSWORD, ROLES);

        Assertions.assertFalse(dto.equals(null));
    }

    @DisplayName("equals - return false with object which has null fields")
    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        UserCreateDto dto = new UserCreateDto(LOGIN, PASSWORD, ROLES);

        Assertions.assertNotEquals(dto, new UserAuthenticationDto(null, null));
    }

    @DisplayName("validate - return true with objects which has correct field's values")
    @Test
    public void shouldBeTrueWhenValidationObjectHasCorrectFieldsValues() {
        UserCreateDto dto = new UserCreateDto("Developer", "5GdS4FaVtgS", ROLES);
        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(dto);

        Assertions.assertTrue(violations.isEmpty());
    }

    @DisplayName("equals - return false with objects which has different fields")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields(UserCreateDto dto2) {
        UserCreateDto dto1 = new UserCreateDto(LOGIN, PASSWORD, ROLES);

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
                new UserCreateDto(LOGIN, PASSWORD, ROLES),
                new UserCreateDto("login2", PASSWORD, ROLES));
    }

    private static Stream<UserCreateDto> shouldBeFalseWhenValidationObjectHasWrongFieldsValues() {
        char[] overMaxLoginChars = new char[101];
        char[] overMaxPasswordChars = new char[73];

        Arrays.fill(overMaxLoginChars, 'l');
        Arrays.fill(overMaxPasswordChars, 'p');

        String overMaxLogin = new String(overMaxLoginChars);
        String overMaxPassword = new String(overMaxPasswordChars);

        return Stream.of(
                new UserCreateDto(null, PASSWORD, ROLES),
                new UserCreateDto("", PASSWORD, ROLES),
                new UserCreateDto(overMaxLogin, PASSWORD, ROLES),
                new UserCreateDto(LOGIN, PASSWORD, ROLES),
                new UserCreateDto(LOGIN, null, ROLES),
                new UserCreateDto(LOGIN, "", ROLES),
                new UserCreateDto(LOGIN, "1234567", ROLES),
                new UserCreateDto(LOGIN, overMaxPassword, ROLES));
    }
}