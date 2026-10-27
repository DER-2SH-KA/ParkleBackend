package ru.d2k.parkle.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import static ru.d2k.parkle.test.consts.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserEntityUpdateDtoTest {

    private static final UUID ID = UuidGeneratorUtil.generateNewUuidV7();
    private static final String LOGIN = "login";
    private static final String PASSWORD = "sdfSDF322fSdJfG";
    private static final Set<RoleResponseDto> ROLES = new HashSet<>();

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("hashCode/equals - return true with similar objects")
    @Test
    public void shouldBeTrueWhenEqualsAndHashCodeWithSimilarObjects() {
        UserUpdateDto dto1 = new UserUpdateDto(ID, LOGIN, PASSWORD, ROLES);
        UserUpdateDto dto2 = new UserUpdateDto(ID, LOGIN, PASSWORD, ROLES);

        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1, dto2);
    }

    @DisplayName("equals - return false with null")
    @Test
    public void shouldBeFalseWhenEqualsWithNull() {
        UserUpdateDto dto = new UserUpdateDto(ID, LOGIN, PASSWORD, ROLES);

        assertNotEquals(null, dto);
    }

    @DisplayName("equals - return false with object which has null fields")
    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        UserUpdateDto dto = new UserUpdateDto(ID, LOGIN, PASSWORD, ROLES);

        assertNotEquals(dto, new RoleUpdateDto(null, null));
    }

    @DisplayName("equals - return false with objects which has different fields")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields(UserUpdateDto dto2) {
        UserUpdateDto dto1 = new UserUpdateDto(ID, LOGIN, PASSWORD, ROLES);

        assertNotEquals(dto1, dto2);
    }

    @DisplayName("validate - return true with objects which has correct field's values")
    @ParameterizedTest
    @MethodSource
    public void shouldBeTrueWhenValidationObjectsHasCorrectFieldsValues(UserUpdateDto dto,
                                                                        String description) {
        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Violation in scenario '" + description + "': " +
                violations);
    }

    @DisplayName("validate - return false with objects which has wrong field's values")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenValidationObjectsHasWrongFieldsValues(UserUpdateDto dto,
                                                                       String description) {
        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Violation in scenario '" + description + "': " +
                violations);
    }

    private static Stream<UserUpdateDto> shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields() {
        return Stream.of(
                new UserUpdateDto(null, LOGIN, PASSWORD, ROLES),
                new UserUpdateDto(ID, "login2", PASSWORD, ROLES));
    }

    private static Stream<Arguments> shouldBeTrueWhenValidationObjectsHasCorrectFieldsValues() {
        char[] lowMinLoginChars = new char[3];
        char[] maxLoginChars = new char[50];
        char[] maxPasswordChars = new char[72];

        Arrays.fill(lowMinLoginChars, 'l');
        Arrays.fill(maxLoginChars, 'l');
        Arrays.fill(maxPasswordChars, 'p');

        String lowMinLogin = new String(lowMinLoginChars);
        String maxLogin = new String(maxLoginChars);
        String maxPassword = new String(maxPasswordChars);

        return Stream.of(
                Arguments.of(new UserUpdateDto(ID, lowMinLogin, PASSWORD,
                        USER_RESPONSE_DTO_ROLES), "Valid lowMinLogin"),
                Arguments.of(new UserUpdateDto(ID, maxLogin, PASSWORD,
                        USER_RESPONSE_DTO_ROLES), "Valid maxLogin"),
                Arguments.of(new UserUpdateDto(ID, LOGIN, "12345678",
                        USER_RESPONSE_DTO_ROLES), "Valid lowMinPassword"),
                Arguments.of(new UserUpdateDto(ID, LOGIN, maxPassword,
                        USER_RESPONSE_DTO_ROLES), "Valid maxPassword"));
    }

    private static Stream<Arguments> shouldBeFalseWhenValidationObjectsHasWrongFieldsValues() {
        char[] lowerMinLoginChars = new char[2];
        char[] overMaxLoginChars = new char[51];
        char[] overMaxPasswordChars = new char[73];

        Arrays.fill(lowerMinLoginChars, 'l');
        Arrays.fill(overMaxLoginChars, 'l');
        Arrays.fill(overMaxPasswordChars, 'p');

        String lowerMinLogin = new String(lowerMinLoginChars);
        String overMaxLogin = new String(overMaxLoginChars);
        String overMaxPassword = new String(overMaxPasswordChars);

        return Stream.of(
                Arguments.of(new UserUpdateDto(ID, lowerMinLogin, PASSWORD,
                        USER_RESPONSE_DTO_ROLES), "Valid lowerMinLogin"),
                Arguments.of(new UserUpdateDto(ID, overMaxLogin, PASSWORD,
                        USER_RESPONSE_DTO_ROLES), "Valid overMaxLogin"),
                Arguments.of(new UserUpdateDto(ID, LOGIN, "1234567",
                        USER_RESPONSE_DTO_ROLES), "Valid lowerMinRoleName"),
                Arguments.of(new UserUpdateDto(ID, LOGIN, overMaxPassword,
                        USER_RESPONSE_DTO_ROLES), "Valid overMaxPassword"));
    }
}