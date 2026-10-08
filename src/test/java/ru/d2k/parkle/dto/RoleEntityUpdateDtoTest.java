package ru.d2k.parkle.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.Set;
import static ru.d2k.parkle.test.consts.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoleEntityUpdateDtoTest {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void equals_shouldBeFalseWhenEqualsWithNullObject() {
        RoleUpdateDto dto = new RoleUpdateDto(ROLE_ID, ROLE_NAME);

        assertNotEquals(null, dto);
    }

    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        RoleUpdateDto roleUpdateDto = new RoleUpdateDto(ROLE_ID, "RoleEntity");

        assertNotEquals(roleUpdateDto, new RoleUpdateDto(null, null));
    }

    @ParameterizedTest
    @MethodSource("shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields")
    public void equals_shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields(RoleUpdateDto roleUpdateDto2) {
        RoleUpdateDto roleUpdateDto1 = new RoleUpdateDto(ROLE_ID, "Role1");

        assertNotEquals(roleUpdateDto1, roleUpdateDto2);
    }

    // validation.
    @ParameterizedTest
    @MethodSource("shouldBeFalseWhenValidationObjectHasWrongFieldsValues")
    public void validation_shouldBeFalseWhenValidationObjectHasWrongFieldsValues(RoleUpdateDto roleUpdateDto,
                                                                      String testDescription) {
        Set<ConstraintViolation<RoleUpdateDto>> violations = validator.validate(roleUpdateDto);

        assertFalse(violations.isEmpty(), String.format("Violations in test '%s': %s%n", testDescription,
                Arrays.toString(violations.toArray())));
    }

    @Test
    public void shouldBeTrueWhenValidationObjectHasCorrectFieldsValues() {
        RoleUpdateDto roleUpdateDto = new RoleUpdateDto(ROLE_ID, "Developer");
        Set<ConstraintViolation<RoleUpdateDto>> violations = validator.validate(roleUpdateDto);

        assertTrue(violations.isEmpty());
    }

    private static Stream<Arguments> shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields() {
        return Stream.of(
                Arguments.of(new RoleUpdateDto(UuidGeneratorUtil.generateNewUuidV7(), "Role1")),
                Arguments.of(new RoleUpdateDto(ROLE_ID, "Role2")),
                Arguments.of(new RoleUpdateDto(ROLE_ID, "Role3")));
    }

    private static Stream<Arguments> shouldBeFalseWhenValidationObjectHasWrongFieldsValues() {
        return Stream.of(
                Arguments.of(new RoleUpdateDto(ROLE_ID, null), "Name is null test"),
                Arguments.of(new RoleUpdateDto(ROLE_ID, ""), "Name is blank test"),
                Arguments.of(new RoleUpdateDto(ROLE_ID, "12345123451234512345123451234512345"),
                        "Name is too long test"));
    }
}