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

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public class RoleCreateDtoTest {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static final String ROLE_NAME = "USER";
    private static final Integer ROLE_PRIORITY = 10;

    private static final String ROLE_MAX_NAME = "12345678901234567890123456789012";

    @ParameterizedTest
    @DisplayName("Validation of RoleCreateDto. Positive.")
    @MethodSource("positiveData")
    void shouldPassValidations(RoleCreateDto dto, String testDescription) {
        Set<ConstraintViolation<RoleCreateDto>> violations = validator.validate(dto);

        Assertions.assertTrue(
                violations.isEmpty(),
                String.format(
                        "Violations in test '%s': %s%n",
                        testDescription,
                        Arrays.toString(violations.toArray())
                )
        );
    }

    @ParameterizedTest
    @DisplayName("Validation of RoleCreateDto. Negative.")
    @MethodSource("negativeData")
    void shouldFailValidations(RoleCreateDto dto, String testDescription) {
        Set<ConstraintViolation<RoleCreateDto>> violations = validator.validate(dto);

        Assertions.assertFalse(
                violations.isEmpty(),
                String.format(
                        "Violations in test '%s': %s%n",
                        testDescription,
                        Arrays.toString(violations.toArray())
                )
        );
    }


    static Stream<Arguments> positiveData() {
        final Integer ROLE_MIN_PRIORITY = 1;

        return Stream.of(
                Arguments.of(new RoleCreateDto(ROLE_NAME, ROLE_PRIORITY), "Ordinary positive test"),
                Arguments.of(new RoleCreateDto(ROLE_MAX_NAME, ROLE_PRIORITY), "Name 32 symbols test"),
                Arguments.of(new RoleCreateDto(ROLE_NAME, ROLE_MIN_PRIORITY), "Min priority test")
        );
    }

    static Stream<Arguments> negativeData() {


        return Stream.of(
                Arguments.of(new RoleCreateDto(null, ROLE_PRIORITY), "Name is null"),
                Arguments.of(new RoleCreateDto("", ROLE_PRIORITY), "Name is blank"),
                Arguments.of(new RoleCreateDto(ROLE_MAX_NAME + "3", ROLE_PRIORITY), "Name bigger than 32 symbols"),
                Arguments.of(new RoleCreateDto(ROLE_NAME, null), "Priority is null"),
                Arguments.arguments(new RoleCreateDto(ROLE_NAME, 0), "Priority non-positive value")
        );
    }
}
