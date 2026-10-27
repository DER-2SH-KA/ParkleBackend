package ru.d2k.parkle.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import static ru.d2k.parkle.test.consts.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserCreateDtoTest {

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final Set<RoleResponseDto> ROLES = new HashSet<>();

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void shouldBeTrueWhenEqualsAndHashCodeWithSimilarObjects() {
        UserCreateDto dto1 = new UserCreateDto(LOGIN, PASSWORD, ROLES);
        UserCreateDto dto2 = new UserCreateDto(LOGIN, PASSWORD, ROLES);

        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1, dto2);
    }

    @Test
    public void shouldBeFalseWhenEqualsWithNull() {
        UserCreateDto dto = new UserCreateDto(LOGIN, PASSWORD, ROLES);

        assertFalse(dto.equals(null));
    }

    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        UserCreateDto dto = new UserCreateDto(LOGIN, PASSWORD, ROLES);

        assertNotEquals(dto, new UserAuthenticationDto(null, null));
    }

    @Test
    public void shouldBeTrueWhenValidationObjectHasCorrectFieldsValues() {
        UserCreateDto dto = new UserCreateDto("Developer", "5GdS4FaVtgS", USER_RESPONSE_DTO_ROLES);
        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), () -> Arrays.toString(violations.toArray()));
    }

    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields(UserCreateDto dto2) {
        UserCreateDto dto1 = new UserCreateDto(USER_LOGIN, PASSWORD, USER_RESPONSE_DTO_ROLES);

        assertNotEquals(dto1, dto2);
    }

    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenValidationObjectHasWrongFieldsValues(UserCreateDto dto) {
        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    private static Stream<UserCreateDto> shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields() {
        return Stream.of(new UserCreateDto("login2", PASSWORD, USER_RESPONSE_DTO_ROLES));
    }

    private static Stream<UserCreateDto> shouldBeFalseWhenValidationObjectHasWrongFieldsValues() {
        char[] overMaxLoginChars = new char[101];
        char[] overMaxPasswordChars = new char[73];

        Arrays.fill(overMaxLoginChars, 'l');
        Arrays.fill(overMaxPasswordChars, 'p');

        String overMaxLogin = new String(overMaxLoginChars);
        String overMaxPassword = new String(overMaxPasswordChars);

        return Stream.of(
                new UserCreateDto(null, PASSWORD, USER_RESPONSE_DTO_ROLES),
                new UserCreateDto("", PASSWORD, USER_RESPONSE_DTO_ROLES),
                new UserCreateDto(overMaxLogin, PASSWORD, USER_RESPONSE_DTO_ROLES),
                new UserCreateDto(LOGIN, null, USER_RESPONSE_DTO_ROLES),
                new UserCreateDto(LOGIN, "", USER_RESPONSE_DTO_ROLES),
                new UserCreateDto(LOGIN, "1234567", USER_RESPONSE_DTO_ROLES),
                new UserCreateDto(LOGIN, overMaxPassword, USER_RESPONSE_DTO_ROLES),
                new UserCreateDto(LOGIN, USER_PASSWORD, null));
    }
}