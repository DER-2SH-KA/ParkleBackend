package ru.d2k.parkle.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class UserUpdateDtoTest {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static final UUID ID = Uuid7Generator.generateNewUUID();
    private static final String ROLE_NAME = "role";
    private static final String LOGIN = "login";
    private static final String EMAIL = "email@mail.ru";
    private static final String PASSWORD = "sdfSDF322fSdJfG";


    @DisplayName("hashCode/equals - return true with similar objects")
    @Test
    public void shouldBeTrueWhenEqualsAndHashCodeWithSimilarObjects() {
        UserUpdateDto dto1 = new UserUpdateDto(ID, ROLE_NAME, LOGIN, EMAIL, PASSWORD);
        UserUpdateDto dto2 = new UserUpdateDto(ID, ROLE_NAME, LOGIN, EMAIL, PASSWORD);

        Assertions.assertEquals(dto1.hashCode(), dto2.hashCode());
        Assertions.assertEquals(dto1, dto2);
    }

    @DisplayName("equals - return false with null")
    @Test
    public void shouldBeFalseWhenEqualsWithNull() {
        UserUpdateDto dto = new UserUpdateDto(ID, ROLE_NAME, LOGIN, EMAIL, PASSWORD);

        Assertions.assertFalse(dto.equals(null));
    }

    @DisplayName("equals - return false with object which has null fields")
    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        UserUpdateDto dto = new UserUpdateDto(ID, ROLE_NAME, LOGIN, EMAIL, PASSWORD);

        Assertions.assertNotEquals(dto, new RoleUpdateDto(null, null, null));
    }

    @DisplayName("equals - return false with objects which has different fields")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields(UserUpdateDto dto2) {
        UserUpdateDto dto1 = new UserUpdateDto(ID, ROLE_NAME, LOGIN, EMAIL, PASSWORD);

        Assertions.assertNotEquals(dto1, dto2);
    }

    @DisplayName("validate - return true with objects which has correct field's values")
    @ParameterizedTest
    @MethodSource
    public void shouldBeTrueWhenValidationObjectsHasCorrectFieldsValues(UserUpdateDto dto) {
        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(dto);

        Assertions.assertTrue(violations.isEmpty(), "Violation founds: " + violations);
    }

    @DisplayName("validate - return false with objects which has wrong field's values")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenValidationObjectsHasWrongFieldsValues(UserUpdateDto roleDto) {
        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(roleDto);

        Assertions.assertFalse(violations.isEmpty());
    }

    private static Stream<UserUpdateDto> shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields() {
        return Stream.of(
                new UserUpdateDto(null, ROLE_NAME, LOGIN, EMAIL, PASSWORD),
                new UserUpdateDto(ID, "role2", LOGIN, EMAIL, PASSWORD),
                new UserUpdateDto(ID, ROLE_NAME, "login2", EMAIL, PASSWORD),
                new UserUpdateDto(ID, ROLE_NAME, LOGIN, "email2@email.ru", PASSWORD)
        );
    }

    private static Stream<Arguments> shouldBeTrueWhenValidationObjectsHasCorrectFieldsValues() {
        final char[] maxRoleNameChars = new char[32];
        final char[] lowMinLoginChars = new char[3];
        final char[] maxLoginChars = new char[50];
        final char[] maxEmailChars = new char[312];
        final char[] maxPasswordChars = new char[72];

        Arrays.fill(maxRoleNameChars, 'r');
        Arrays.fill(lowMinLoginChars, 'l');
        Arrays.fill(maxLoginChars, 'l');
        Arrays.fill(maxEmailChars, 'e');
        Arrays.fill(maxPasswordChars, 'p');

        final String maxRoleName = new String(maxRoleNameChars);
        final String lowMinLogin = new String(lowMinLoginChars);
        final String maxLogin = new String(maxLoginChars);
        final String maxEmail = new String(maxEmailChars) + "@mail.ru";
        final String maxPassword = new String(maxPasswordChars);

        return Stream.of(
                Arguments.of(new UserUpdateDto(ID, maxRoleName, LOGIN, EMAIL, PASSWORD), "Valid maxRoleName"),
                Arguments.of(new UserUpdateDto(ID, ROLE_NAME, lowMinLogin, EMAIL, PASSWORD), "Valid lowMinLogin"),
                Arguments.of(new UserUpdateDto(ID, ROLE_NAME, maxLogin, EMAIL, PASSWORD), "Valid maxLogin"),
                Arguments.of(new UserUpdateDto(ID, ROLE_NAME, LOGIN, maxEmail, PASSWORD), "Valid maxEmail"),
                Arguments.of(new UserUpdateDto(ID, ROLE_NAME, LOGIN, EMAIL, "12345678"), "Valid lowMinPassword"),
                Arguments.of(new UserUpdateDto(ID, ROLE_NAME, LOGIN, EMAIL, maxPassword), "Valid maxPassword")
        );
    }

    private static Stream<Arguments> shouldBeFalseWhenValidationObjectsHasWrongFieldsValues() {
        final char[] overMaxRoleNameChars = new char[33];
        final char[] lowerMinLoginChars = new char[2];
        final char[] overMaxLoginChars = new char[51];
        final char[] overMaxEmailChars = new char[321];
        final char[] overMaxPasswordChars = new char[73];

        Arrays.fill(overMaxRoleNameChars, 'r');
        Arrays.fill(lowerMinLoginChars, 'l');
        Arrays.fill(overMaxLoginChars, 'l');
        Arrays.fill(overMaxEmailChars, 'e');
        Arrays.fill(overMaxPasswordChars, 'p');

        final String overMaxRoleName = new String(overMaxRoleNameChars);
        final String lowerMinLogin = new String(lowerMinLoginChars);
        final String overMaxLogin = new String(overMaxLoginChars);
        final String overMaxEmail = new String(overMaxEmailChars);
        final String overMaxPassword = new String(overMaxPasswordChars);

        return Stream.of(
                Arguments.of(new UserUpdateDto(ID, overMaxRoleName, LOGIN, EMAIL, PASSWORD), "Valid overMaxRoleName"),
                Arguments.of(new UserUpdateDto(ID, ROLE_NAME, lowerMinLogin, EMAIL, PASSWORD), "Valid lowerMinLogin"),
                Arguments.of(new UserUpdateDto(ID, ROLE_NAME, overMaxLogin, EMAIL, PASSWORD), "Valid overMaxLogin"),
                Arguments.of(new UserUpdateDto(ID, ROLE_NAME, LOGIN, overMaxEmail, PASSWORD), "Valid overMaxEmail"),
                Arguments.of(new UserUpdateDto(ID, ROLE_NAME, LOGIN, EMAIL, "1234567"), "Valid lowerMinRoleName"),
                Arguments.of(new UserUpdateDto(ID, ROLE_NAME, LOGIN, EMAIL, overMaxPassword), "Valid overMaxPassword")
        );
    }
}
