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
import java.util.UUID;
import java.util.stream.Stream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoleEntityUpdateDtoTest {

    private static final UUID uuid = UuidGeneratorUtil.generateNewUuidV7();
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("hashCode/equals - return true with similar objects")
    @Test
    public void shouldBeTrueWhenEqualsAndHashCodeWithSimilarObjects() {
        RoleUpdateDto roleUpdateDto1 = new RoleUpdateDto(uuid, "RoleEntity");
        RoleUpdateDto roleUpdateDto2 = new RoleUpdateDto(uuid, "RoleEntity");

        assertEquals(roleUpdateDto1.hashCode(), roleUpdateDto2.hashCode());
        assertEquals(roleUpdateDto1, roleUpdateDto2);
    }

    @DisplayName("equals - return false with null")
    @Test
    public void shouldBeFalseWhenEqualsWithNull() {
        RoleUpdateDto roleUpdateDto = new RoleUpdateDto(uuid, "RoleEntity");

        assertFalse(roleUpdateDto == null);
    }

    @DisplayName("equals - return false with object which has null fields")
    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        RoleUpdateDto roleUpdateDto = new RoleUpdateDto(uuid, "RoleEntity");

        assertNotEquals(roleUpdateDto, new RoleUpdateDto(null, null));
    }

    @DisplayName("equals - return false with objects which has different fields")
    @ParameterizedTest
    @MethodSource("shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields")
    public void shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields(RoleUpdateDto roleUpdateDto2) {
        RoleUpdateDto roleUpdateDto1 = new RoleUpdateDto(uuid, "Role1");

        assertNotEquals(roleUpdateDto1, roleUpdateDto2);
    }

    @DisplayName("validate - return false with objects which has wrong field's values")
    @ParameterizedTest
    @MethodSource("shouldBeFalseWhenValidationObjectHasWrongFieldsValues")
    public void shouldBeFalseWhenValidationObjectHasWrongFieldsValues(RoleUpdateDto roleUpdateDto,
                                                                      String testDescription) {
        Set<ConstraintViolation<RoleUpdateDto>> violations = validator.validate(roleUpdateDto);

        assertFalse(violations.isEmpty(), String.format("Violations in test '%s': %s%n", testDescription,
                Arrays.toString(violations.toArray())));
    }

    @DisplayName("validate - return true with objects which has correct field's values")
    @Test
    public void shouldBeTrueWhenValidationObjectHasCorrectFieldsValues() {
        RoleUpdateDto roleUpdateDto = new RoleUpdateDto(uuid, "Developer");
        Set<ConstraintViolation<RoleUpdateDto>> violations = validator.validate(roleUpdateDto);

        assertTrue(violations.isEmpty());
    }

    private static Stream<Arguments> shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields() {
        return Stream.of(
                Arguments.of(new RoleUpdateDto(UuidGeneratorUtil.generateNewUuidV7(), "Role1")),
                Arguments.of(new RoleUpdateDto(uuid, "Role2")),
                Arguments.of(new RoleUpdateDto(uuid, "Role1")));
    }

    private static Stream<Arguments> shouldBeFalseWhenValidationObjectHasWrongFieldsValues() {
        return Stream.of(
                Arguments.of(new RoleUpdateDto(uuid, null), "Name is null test"),
                Arguments.of(new RoleUpdateDto(uuid, ""), "Name is blank test"),
                Arguments.of(new RoleUpdateDto(uuid, "12345123451234512345123451234512345"),
                        "Name is too long test"));
    }
}