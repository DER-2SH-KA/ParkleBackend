package ru.d2k.parkle.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class UserUpdateDtoTest {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static final String ROLE_NAME = "role";
    private static final String LOGIN = "login";
    private static final String EMAIL = "email@mail.ru";
    private static final String PASSWORD = "sdfSDF322fSdJfG";


    @DisplayName("hashCode/equals - return true with similar objects")
    @Test
    public void shouldBeTrueWhenEqualsAndHashCodeWithSimilarObjects() {
        UserUpdateDto dto1 = new UserUpdateDto(ROLE_NAME, LOGIN, EMAIL, PASSWORD);
        UserUpdateDto dto2 = new UserUpdateDto(ROLE_NAME, LOGIN, EMAIL, PASSWORD);

        Assertions.assertEquals(dto1.hashCode(), dto2.hashCode());
        Assertions.assertEquals(dto1, dto2);
    }

    @DisplayName("equals - return false with null")
    @Test
    public void shouldBeFalseWhenEqualsWithNull() {
        UserUpdateDto dto = new UserUpdateDto(ROLE_NAME, LOGIN, EMAIL, PASSWORD);

        Assertions.assertFalse(dto.equals(null));
    }

    @DisplayName("equals - return false with object which has null fields")
    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        UserUpdateDto dto = new UserUpdateDto(ROLE_NAME, LOGIN, EMAIL, PASSWORD);

        Assertions.assertNotEquals(dto, new RoleDto(null, null, null));
    }

    @DisplayName("equals - return false with objects which has different fields")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields(UserUpdateDto dto2) {
        UserUpdateDto dto1 = new UserUpdateDto(ROLE_NAME, LOGIN, EMAIL, PASSWORD);

        Assertions.assertNotEquals(dto1, dto2);
    }

    @DisplayName("validate - return true with objects which has correct field's values")
    @Test
    public void shouldBeTrueWhenValidationObjectHasCorrectFieldsValues() {
        UserUpdateDto dto = new UserUpdateDto(
                "Dev", "Developer",
                "dev@mail.ru", "devPassword"
        );
        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(dto);

        Assertions.assertTrue(violations.isEmpty());
    }

    @DisplayName("validate - return false with objects which has wrong field's values")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenValidationObjectHasWrongFieldsValues(UserUpdateDto roleDto) {
        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(roleDto);

        Assertions.assertFalse(violations.isEmpty());
    }

    private static Stream<UserUpdateDto> shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields() {
        return Stream.of(
                new UserUpdateDto("role2", LOGIN, EMAIL, PASSWORD),
                new UserUpdateDto(ROLE_NAME, "login2", EMAIL, PASSWORD),
                new UserUpdateDto(ROLE_NAME, LOGIN, "email2@email.ru", PASSWORD)
        );
    }

    private static Stream<UserUpdateDto> shouldBeFalseWhenValidationObjectHasWrongFieldsValues() {
        final char[] overMaxRoleNameChars = new char[33];
        final char[] overMaxLoginChars = new char[101];
        final char[] overMaxEmailChars = new char[321];
        final char[] overMaxPasswordChars = new char[321];

        Arrays.fill(overMaxRoleNameChars, 'r');
        Arrays.fill(overMaxLoginChars, 'l');
        Arrays.fill(overMaxEmailChars, 'e');
        Arrays.fill(overMaxPasswordChars, 'p');

        final String overMaxRoleName = new String(overMaxRoleNameChars);
        final String overMaxLogin = new String(overMaxLoginChars);
        final String overMaxEmail = new String(overMaxEmailChars);
        final String overMaxPassword = new String(overMaxPasswordChars);

        return Stream.of(
                new UserUpdateDto(overMaxRoleName, LOGIN, EMAIL, PASSWORD),
                new UserUpdateDto(ROLE_NAME, overMaxLogin, EMAIL, PASSWORD),
                new UserUpdateDto(ROLE_NAME, LOGIN, overMaxEmail, PASSWORD),
                new UserUpdateDto(ROLE_NAME, LOGIN, EMAIL, "1234567"),
                new UserUpdateDto(ROLE_NAME, LOGIN, EMAIL, overMaxPassword)
        );
    }
}
